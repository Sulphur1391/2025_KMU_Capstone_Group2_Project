package com.outfit.ai.cloth_app.controller;

import com.outfit.ai.cloth_app.dto.WeatherDto;
import com.outfit.ai.cloth_app.service.WeatherService;
<<<<<<< HEAD
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// 날씨 컨트롤러
@RestController
@RequestMapping("/api/weather")
public class WeatherController {
    private final WeatherService weatherService;
    private final Logger log = LoggerFactory.getLogger(WeatherController.class);
=======
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;
>>>>>>> origin/main

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

<<<<<<< HEAD
    // 현재 지역의 최근 날씨 불러오가
    @GetMapping
    public ResponseEntity<WeatherDto> getLatestWeatherByLocation(
            @RequestParam("location") String location) {
        log.info("API 요청 수신: 위치 {}의 날씨 정보 조회", location);

        WeatherDto weatherDto = weatherService.getWeather(location);

        if (weatherDto != null && weatherDto.getDescription() != null &&
                (weatherDto.getDescription().contains("실패") || weatherDto.getDescription().contains("오류"))) {
            log.warn("날씨 정보 조회 실패 또는 오류 발생: {}", location);
            return ResponseEntity.status(500).body(weatherDto);
        }

        log.info("날씨 정보 조회 성공 및 반환: {}", location);
        return ResponseEntity.ok(weatherDto);
=======
    @GetMapping(produces = "application/json; charset=UTF-8")
    public WeatherDto getWeather(@RequestParam String location) {
        return weatherService.getWeather(location);
>>>>>>> origin/main
    }
}
