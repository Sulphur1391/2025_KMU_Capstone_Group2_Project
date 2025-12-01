package com.outfit.ai.cloth_app.service;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.Calendar;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.api.services.calendar.model.Events;
import com.outfit.ai.cloth_app.repository.UserCalendarRepository;
import com.outfit.ai.cloth_app.repository.UserRepository;
import com.outfit.ai.cloth_app.tables.UserCalendar;
import com.outfit.ai.cloth_app.tables.UserTable;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
public class GoogleCalendarService {
    private static final String APPLICATION_NAME = "ClothApp AI Outfit Recommender";
    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    private final UserCalendarRepository userCalendarRepository;
    private final UserRepository userRepository;

    public GoogleCalendarService(UserCalendarRepository userCalendarRepository, UserRepository userRepository) {
        this.userCalendarRepository = userCalendarRepository;
        this.userRepository = userRepository;
    }

    private Calendar buildCalendarService(Credential credential) throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        return new Calendar.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    @Transactional
    public List<UserCalendar> fetchAndSaveSchedules(Credential credential, String userIdentifier)
        throws IOException, GeneralSecurityException {
        Optional<UserTable> userOptional = userRepository.findByEmail(userIdentifier);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found identifier: " + userIdentifier);
        }
        UserTable currentUser = userOptional.get();

        Calendar service = buildCalendarService(credential);
        String calenderId = "primary";

        DateTime now = new DateTime(System.currentTimeMillis());
        DateTime monthLater = new DateTime(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000);

        Events events = service.events().list(calenderId)
                .setMaxResults(50)
                .setTimeMin(now)
                .setTimeMax(monthLater)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();

        List<Event> items = events.getItems();
        if (items == null || items.isEmpty()) {
            return List.of();
        }

        List<UserCalendar> newSchedules = items.stream()
                .filter(event -> event.getStart() != null && event.getEnd() != null)
                .map(event -> {
                    if (userCalendarRepository.existsByGoogleEventId(event.getId())) {
                        return null;
                    }

                    UserCalendar userCalendar = new UserCalendar();

                    userCalendar.setGoogleEventId(event.getId());
                    userCalendar.setUserTable(currentUser);
                    userCalendar.setEventSummary(event.getSummary());
                    userCalendar.setLocation(event.getLocation());

                    userCalendar.setStartTime(toOffsetDateTime(event.getStart()));
                    userCalendar.setEndTime(toOffsetDateTime(event.getEnd()));

                    userCalendar.setJsonData(event.toString());

                    boolean isAllDay = event.getStart().getDate() != null && event.getStart().getDateTime() == null;
                    userCalendar.setIsAllDay(isAllDay);

                    userCalendar.setDressCodeTag(null);

                    return userCalendar;
                })
                .filter(s -> s != null)
                .toList();

        return userCalendarRepository.saveAll(newSchedules);
    }

    private OffsetDateTime toOffsetDateTime(EventDateTime eventDateTime) {
        DateTime dateTime = eventDateTime.getDateTime();
        DateTime dateOnly = eventDateTime.getDate();

        if (dateTime != null) {
            return OffsetDateTime.parse(dateTime.toStringRfc3339());
        } else if (dateOnly != null) {
            return Instant.ofEpochMilli(dateOnly.getValue())
                    .atZone(ZoneId.systemDefault())
                    .toOffsetDateTime();
        }

        throw new IllegalArgumentException("EventDateTime must contain either dateTime or date.");
    }

    public Event createCalendarEvent(Credential credential, String summary, String description, String startTime, String endTime)
        throws IOException, GeneralSecurityException {
        Calendar service = buildCalendarService(credential);

        Event event = new Event()
                .setSummary(summary)
                .setDescription(description);

        EventDateTime start = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(startTime))
                .setTimeZone("Asia/Seoul");
        event.setStart(start);

        EventDateTime end = new EventDateTime()
                .setDateTime(new com.google.api.client.util.DateTime(endTime))
                .setTimeZone("Asia/Seoul");
        event.setEnd(end);

        String calendarId = "primary";

        event = service.events().insert(calendarId, event).execute();

        return event;
    }
}
