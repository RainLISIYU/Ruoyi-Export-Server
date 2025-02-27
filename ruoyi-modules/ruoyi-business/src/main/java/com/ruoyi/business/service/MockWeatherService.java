package com.ruoyi.business.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.function.Function;

/**
 * 天气接口
 */
public interface MockWeatherService extends Function<MockWeatherService.Request, MockWeatherService.Response> {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Request(
            @JsonProperty(required = false, value = "cityName") @JsonPropertyDescription("市名称,比如太原市") String cityName,
            @JsonProperty(required = true, value = "countyName") @JsonPropertyDescription("区/县名称,比如太原") String countyName
    ) {

    }

    public record Response(String description) {

    }

}
