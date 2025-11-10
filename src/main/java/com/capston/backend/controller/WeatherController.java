package com.capston.backend.controller;

import com.capston.backend.dto.WeatherDto;
import com.capston.backend.service.WeatherService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping(produces = "application/json; charset=UTF-8")
    public WeatherDto getWeather(@RequestParam String location) {
        return weatherService.getWeather(location);
    }
}
