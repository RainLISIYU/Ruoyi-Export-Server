package com.ruoyi.chat.service;

import com.alibaba.cloud.commons.lang.StringUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

/**
 * @author lsy
 * @description 天气查询服务类
 * @date 2025/5/15
 */
@Service
@Slf4j
public class OpenMeteoService {

    private final WebClient webClient;

    @Value("${weather.base-url:https://api.open-meteo.com/v1}")
    private String BASE_URL;

    public OpenMeteoService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl(BASE_URL == null ? "https://api.open-meteo.com/v1" : BASE_URL)
                .defaultHeader("Accept", "application/json")
                .defaultHeader("User-Agent", "OpenMeteoClient/1.0")
                .build();
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record WeatherData(
            @JsonProperty("latitude") Double latitude,
            @JsonProperty("longitude") Double longitude,
            @JsonProperty("timezone") String timezone,
            @JsonProperty("current") CurrentWeather current,
            @JsonProperty("daily") DailyForecast daily,
            @JsonProperty("current_units") CurrentUnits currentUnits
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record CurrentWeather(
                @JsonProperty("time") String time,
                @JsonProperty("temperature_2m") Double temperature,
                @JsonProperty("apparent_temperature") Double feelsLike,
                @JsonProperty("relative_humidity_2m") Integer humidity,
                @JsonProperty("precipitation") Double precipitation,
                @JsonProperty("weather_code") Integer weatherCode,
                @JsonProperty("wind_speed_10m") Double windSpeed,
                @JsonProperty("wind_direction_10m") Integer windDirection
        ) {}

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record CurrentUnits(
                @JsonProperty("time") String timeUnit,
                @JsonProperty("temperature_2m") String temperatureUnit,
                @JsonProperty("relative_humidity_2m") String humidityUnit,
                @JsonProperty("wind_speed_10m") String windSpeedUnit) {
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        public record DailyForecast(
                @JsonProperty("time") List<String> time,
                @JsonProperty("temperature_2m_max") List<Double> tempMax,
                @JsonProperty("temperature_2m_min") List<Double> tempMin,
                @JsonProperty("precipitation_sum") List<Double> precipitationSum,
                @JsonProperty("weather_code") List<Integer> weatherCode,
                @JsonProperty("wind_speed_10m_max") List<Double> windSpeedMax,
                @JsonProperty("wind_direction_10m_dominant") List<Integer> windDirection) {
        }
    }

    /**
     * 获取天气代码对应的描述
     */
    private String getWeatherDescription(int code) {
        return switch (code) {
            case 0 -> "晴朗";
            case 1, 2, 3 -> "多云";
            case 45, 48 -> "雾";
            case 51, 53, 55 -> "毛毛雨";
            case 56, 57 -> "冻雨";
            case 61, 63, 65 -> "雨";
            case 66, 67 -> "冻雨";
            case 71, 73, 75 -> "雪";
            case 77 -> "雪粒";
            case 80, 81, 82 -> "阵雨";
            case 85, 86 -> "阵雪";
            case 95 -> "雷暴";
            case 96, 99 -> "雷暴伴有冰雹";
            default -> "未知天气";
        };
    }

    /**
     * 获取风向描述
     */
    private String getWindDirection(int degrees) {
        if (degrees >= 337.5 || degrees < 22.5)
            return "北风";
        if (degrees >= 22.5 && degrees < 67.5)
            return "东北风";
        if (degrees >= 67.5 && degrees < 112.5)
            return "东风";
        if (degrees >= 112.5 && degrees < 157.5)
            return "东南风";
        if (degrees >= 157.5 && degrees < 202.5)
            return "南风";
        if (degrees >= 202.5 && degrees < 247.5)
            return "西南风";
        if (degrees >= 247.5 && degrees < 292.5)
            return "西风";
        return "西北风";
    }

    /**
     * 根据指定经纬度获取天气情况
     * @param latitude 纬度
     * @param longitude 经度
     * @return 天气情况
     */
    @Tool(description = "根据经纬度获取天气情况")
    public String getWeatherForecastByLocation(
            @ToolParam(description = "纬度，例如：39.9042") String latitude,
            @ToolParam(description = "经度，例如：116.4074") String longitude
    ) {
        log.info(BASE_URL);
        var weatherData = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/forecast")
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam("current", "temperature_2m,apparent_temperature,relative_humidity_2m,precipitation,weather_code,wind_speed_10m,wind_direction_10m")
                        .queryParam("daily", "temperature_2m_max,temperature_2m_min,precipitation_sum,weather_code,wind_speed_10m_max,wind_direction_10m_dominant")
                        .queryParam("forecast_days", "7")
                        .queryParam("timezone", "auto")
                        .build())
                .retrieve()
                .bodyToMono(WeatherData.class)
                .block();

        if (Objects.isNull(weatherData)) {
            return StringUtils.EMPTY;
        }

        // 拼接天气信息
        StringBuilder weatherInfo = new StringBuilder();

        // 当前天气
        WeatherData.CurrentWeather current = weatherData.current();
        // 天气单位
        String temperatureUnit = weatherData.currentUnits() != null ? weatherData.currentUnits().temperatureUnit() : "℃";
        // 风速单位
        String windSpeedUnit = weatherData.currentUnits() != null ? weatherData.currentUnits().windSpeedUnit() : "km/h";
        // 湿度单位
        String humidityUnit = weatherData.currentUnits() != null ? weatherData.currentUnits().humidityUnit() : "%";
        // 天气信息
        weatherInfo.append(String.format("""
                当前天气：
                温度：%.1f%s（体感湿度：%d%s）
                天气：%s
                风向：%s（%.1f %s)
                湿度：%d%s
                降水量：%.1f 毫米
                
                """,
                current.temperature(),
                temperatureUnit,
                current.humidity(),
                humidityUnit,
                getWeatherDescription(current.weatherCode()),
                getWindDirection(current.windDirection()),
                current.windSpeed(),
                windSpeedUnit,
                current.humidity(),
                humidityUnit,
                current.precipitation()));
        // 添加未来天气预报
        weatherInfo.append("未来天气预报:\n");
        WeatherData.DailyForecast daily = weatherData.daily();

        for (int i = 0; i < daily.time().size(); i++) {
            String date = daily.time().get(i);
            double tempMin = daily.tempMin().get(i);
            double tempMax = daily.tempMax().get(i);
            int weatherCode = daily.weatherCode().get(i);
            double windSpeed = daily.windSpeedMax().get(i);
            int windDir = daily.windDirection().get(i);
            double precip = daily.precipitationSum().get(i);

            // 格式化日期
            LocalDate localDate = LocalDate.parse(date);
            String formattedDate = localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd (EEE)"));

            weatherInfo.append(String.format("""
                %s:
                温度: %.1f%s ~ %.1f%s
                天气: %s
                风向: %s (%.1f %s)
                降水量: %.1f 毫米

                """,
                    formattedDate,
                    tempMin, temperatureUnit,
                    tempMax, temperatureUnit,
                    getWeatherDescription(weatherCode),
                    getWindDirection(windDir),
                    windSpeed, windSpeedUnit,
                    precip));
        }

        return weatherInfo.toString();
    }

}
