package com.ruoyi.business.domain;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author lsy
 * @description 天气查询结果
 * @date 2025/2/24
 */
@Data
public class WeatherResult implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String message;

    private String status;

    private String date;

    private String time;

    private WeatherResult.CityInfo cityInfo;

    private WeatherResult.WeatherData data;

    public record CityInfo (String city, String cityKey, String parent, String updateTime){

    }

    public record WeatherData (String shidu, Double pm25, Double pm10, String quality, List<WeatherResult.Forecast> forecast) {}

    public record Forecast (String date, String hegh, String low, String ymd, String week, String type, String fx, String fl){}

}
