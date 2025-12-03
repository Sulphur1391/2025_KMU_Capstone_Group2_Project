package com.outfit.ai.cloth_app.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.outfit.ai.cloth_app.api.weather_api.WeatherApiClient;
import com.outfit.ai.cloth_app.dto.WeatherDto;
import com.outfit.ai.cloth_app.entity.WeatherId;
import com.outfit.ai.cloth_app.repository.WeatherRepository;
import com.outfit.ai.cloth_app.tables.Weather;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;

import java.time.OffsetDateTime;
import java.util.Optional;

// 날씨 서비스
@Service
@Transactional
public class WeatherService {
    private final WeatherApiClient weatherApiClient;
    private final WeatherRepository weatherRepository;
    private final ObjectMapper objectMapper;
    private final Logger log = LoggerFactory.getLogger(WeatherService.class);

    public WeatherService(
            WeatherApiClient weatherApiClient,
            WeatherRepository weatherRepository,
            ObjectMapper objectMapper) {
        this.weatherApiClient = weatherApiClient;
        this.weatherRepository = weatherRepository;
        this.objectMapper = objectMapper;
    }

    // 날씨 데이터 받기
    // 캐시 가능하게 설정
    @Cacheable(value = "weather", key = "#location")
    @Transactional
    public WeatherDto getWeather(String location) {
        log.info("캐시 미스 발생, API 호출 및 DB 처리 시작: {}", location);
        Optional<WeatherDto> dbWeather = getWeatherFromDb(location);

        if (dbWeather.isPresent()) {
            log.info("Redis 캐시 미스, DB 2차 캐시 조회 성공: {}", location);
            return dbWeather.get();
        }

        try {
            WeatherDto dto = weatherApiClient.getWeather(location);

            if (dto != null && dto.getDescription() != null && !dto.getDescription().contains("실패")) {
                String locationKey = dto.getLocation();

                saveWeatherEntity(locationKey, dto);

                log.info("API 호출 결과 DB 저장 완료: {}", location);
                return dto;
            }

            log.warn("API 호출 실패 또는 응답 오류. 기본값 반환. Location: {}", location);
            return new WeatherDto(location, "API 조회 실패", 0.0, 0.0, 0.0);
        } catch (Exception e) {
            log.error("날씨 조회 중 예외 발생. Location: {}", location, e);
            return new WeatherDto(location, "서비스 오류 발생", 0.0, 0.0, 0.0);
        }
    }

    // DB에서 날씨 데이터 불러오기
    private Optional<WeatherDto> getWeatherFromDb(String locationKey) {
        Optional<Weather> latestWeatherEntity = weatherRepository.findTopByLocationKeyOrderByWeatherId_CreatedAtDesc(locationKey);

        if (latestWeatherEntity.isPresent()) {
            Weather weather = latestWeatherEntity.get();
            try {
                String json = weather.getWeatherData();
                return Optional.of(objectMapper.readValue(json, WeatherDto.class));
            } catch (Exception e) {
                log.error("DB 엔티티 DTO로 변환 중 오류 발생: {}", locationKey, e);
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    // 날씨 엔티티 저장
    private void saveWeatherEntity(String locationKey, WeatherDto dto) {
        Weather weather = new Weather();

        WeatherId id = new WeatherId(null, OffsetDateTime.now());
        weather.setId(id);

        weather.setLocationKey(locationKey);
        weather.setWeatherData(convertDtoToJson(dto));

        weatherRepository.save(weather);
    }

    // JSON으로 데이터 변환하는 DTO
    private String convertDtoToJson(WeatherDto dto) {
        try {
            return objectMapper.writeValueAsString(dto);
        } catch (Exception e) {
            log.error("날씨 DTO를 JSON으로 변환하는 데 실패했습니다.", e);
            throw new RuntimeException("날씨 DTO를 JSON으로 변환하는 데 실패했습니다.", e);
        }
    }
}
