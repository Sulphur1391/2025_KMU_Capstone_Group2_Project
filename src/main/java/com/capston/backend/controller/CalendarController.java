package com.capston.backend.controller;

import com.capston.backend.service.CalendarService;
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
}
