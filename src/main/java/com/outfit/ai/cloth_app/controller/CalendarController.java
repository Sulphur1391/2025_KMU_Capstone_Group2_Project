package com.outfit.ai.cloth_app.controller;

<<<<<<< HEAD
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.outfit.ai.cloth_app.service.GoogleCalendarService;
import com.outfit.ai.cloth_app.tables.UserCalendar;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

// 캘린더 컨트롤러
@RestController
@RequestMapping("/api/v1/schedules")
public class CalendarController {
    private final GoogleCalendarService calendarService;

    public CalendarController(GoogleCalendarService calendarService) {
        this.calendarService = calendarService;
    }

    // 불러오고 저장
    @GetMapping("/fetch-and-save")
    public ResponseEntity<List<UserCalendar>> fetchAndSaveSchedulesApi(
            @RegisteredOAuth2AuthorizedClient("google") OAuth2AuthorizedClient authorizedClient,
            Authentication authentication) {
        String accessToken = authorizedClient.getAccessToken().getTokenValue();

        String userIdString = authentication.getName();

        try {
            Credential credential = new GoogleCredential.Builder()
                    .setTransport(GoogleNetHttpTransport.newTrustedTransport())
                    .setJsonFactory(GsonFactory.getDefaultInstance())
                    .build()
                    .setAccessToken(accessToken);

            List<UserCalendar> schedules = calendarService.fetchAndSaveSchedules(userIdString);

            return ResponseEntity.ok(schedules);

        } catch (IllegalArgumentException e) {
            System.err.println("User not found: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
=======
import com.outfit.ai.cloth_app.service.CalendarService;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.calendar.model.Events;
import com.google.api.services.calendar.model.Event;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.ZonedDateTime;

@RestController
public class CalendarController {

    private final CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    // OAuth Callback
    @GetMapping("/oauth2callback")
    public String oauth2Callback(@RequestParam("code") String code,
                                 @RequestParam("email") String userEmail) {
        try {
            Credential credential = calendarService.exchangeCodeForCredential(code, userEmail);
            Events events = calendarService.listEvents(credential);

            return "<html><body>"
                    + "<h2>🎉 OAuth 토큰 교환 성공!</h2>"
                    + "<p>첫 번째 이벤트: <code>" + (events.getItems().isEmpty() ? "없음" : events.getItems().get(0).getSummary()) + "</code></p>"
                    + "</body></html>";

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
            return "<html><body>"
                    + "<h2>❌ API/보안 오류 발생</h2>"
                    + "<pre>" + e.getMessage() + "</pre>"
                    + "</body></html>";
        }
    }

    // 이벤트 조회
    @GetMapping("/calendar/events")
    public Events getEvents(@RequestParam String userEmail) throws GeneralSecurityException, IOException {
        Credential credential = calendarService.getCredentialFromRefreshToken(userEmail);
        return calendarService.listEvents(credential);
    }

    // 이벤트 생성
    @PostMapping("/calendar/events")
    public Event addEvent(@RequestParam String userEmail,
                          @RequestParam String summary,
                          @RequestParam String description,
                          @RequestParam String start,
                          @RequestParam String end) throws GeneralSecurityException, IOException {

        Credential credential = calendarService.getCredentialFromRefreshToken(userEmail);
        ZonedDateTime startTime = ZonedDateTime.parse(start);
        ZonedDateTime endTime = ZonedDateTime.parse(end);
        return calendarService.createEvent(credential, summary, description, startTime, endTime);
    }
>>>>>>> origin/main
}
