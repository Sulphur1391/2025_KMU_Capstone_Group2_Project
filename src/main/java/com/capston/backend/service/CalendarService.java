package com.capston.backend.service;

import com.capston.backend.entity.RefreshToken;
import com.capston.backend.repository.RefreshTokenRepository;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Date;

@Service
public class CalendarService {

    private final RefreshTokenRepository tokenRepository;

    @Value("${google.calendar.client-id}")
    private String clientId;

    @Value("${google.calendar.client-secret}")
    private String clientSecret;

    @Value("${google.calendar.redirect-uri}")
    private String redirectUri;

    private static final List<String> SCOPES = Collections.singletonList(
            "https://www.googleapis.com/auth/calendar"
    );

    private static final JacksonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    public CalendarService(RefreshTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    private static Calendar.Builder createCalendarBuilder(Credential credential) throws GeneralSecurityException, IOException {
        return new Calendar.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                credential
        ).setApplicationName("Capston Backend");
    }

    // Authorization Code → Credential + DB 저장
    public Credential exchangeCodeForCredential(String code, String userEmail) throws GeneralSecurityException, IOException {
        GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                GoogleNetHttpTransport.newTrustedTransport(),
                JSON_FACTORY,
                clientId,
                clientSecret,
                code,
                redirectUri
        ).setScopes(SCOPES)
                .execute();

        String refreshToken = tokenResponse.getRefreshToken();
        if (refreshToken != null) {
            RefreshToken token = tokenRepository.findByUserEmail(userEmail)
                    .orElse(RefreshToken.builder()
                            .userEmail(userEmail)
                            .createdAt(LocalDateTime.now())
                            .build());
            token.setRefreshToken(refreshToken);
            token.setUpdatedAt(LocalDateTime.now());
            tokenRepository.save(token);
            System.out.println("✅ Refresh Token 저장/갱신 완료");
        }

        return new GoogleCredential.Builder()
                .setTransport(GoogleNetHttpTransport.newTrustedTransport())
                .setJsonFactory(JSON_FACTORY)
                .setClientSecrets(clientId, clientSecret)
                .build()
                .setFromTokenResponse(tokenResponse);
    }

    // DB에서 토큰 가져와 Credential 생성
    public Credential getCredentialFromRefreshToken(String userEmail) throws GeneralSecurityException, IOException {
        RefreshToken token = tokenRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new IllegalStateException("Refresh Token이 DB에 없습니다."));

        return new GoogleCredential.Builder()
                .setTransport(GoogleNetHttpTransport.newTrustedTransport())
                .setJsonFactory(JSON_FACTORY)
                .setClientSecrets(clientId, clientSecret)
                .build()
                .setRefreshToken(token.getRefreshToken());
    }

    // 이벤트 조회
    public Events listEvents(Credential credential) throws GeneralSecurityException, IOException {
        Calendar service = createCalendarBuilder(credential).build();
        try {
            return service.events().list("primary")
                    .setMaxResults(10)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
        } catch (IOException e) {
            System.err.println("이벤트 조회 실패: " + e.getMessage());
            throw e;
        }
    }

    // 이벤트 생성
    public Event createEvent(Credential credential, String summary, String description,
                             ZonedDateTime start, ZonedDateTime end) throws GeneralSecurityException, IOException {

        Calendar service = createCalendarBuilder(credential).build();

        Event event = new Event()
                .setSummary(summary)
                .setDescription(description);

        Date startDate = Date.from(start.toInstant());
        Date endDate = Date.from(end.toInstant());

        event.setStart(new EventDateTime().setDateTime(new DateTime(startDate)));
        event.setEnd(new EventDateTime().setDateTime(new DateTime(endDate)));

        try {
            return service.events().insert("primary", event).execute();
        } catch (IOException e) {
            System.err.println("이벤트 생성 실패: " + e.getMessage());
            throw e;
        }
    }
}
