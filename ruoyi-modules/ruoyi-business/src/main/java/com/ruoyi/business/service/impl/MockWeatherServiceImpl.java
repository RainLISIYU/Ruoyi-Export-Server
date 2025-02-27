package com.ruoyi.business.service.impl;

import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.business.domain.CityCode;
import com.ruoyi.business.domain.WeatherResult;
import com.ruoyi.business.service.CityCodeService;
import com.ruoyi.business.service.MockWeatherService;
import com.ruoyi.common.core.utils.StringUtils;
import com.ruoyi.common.core.utils.http.HttpUtils;
import jakarta.annotation.Resource;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @author lsy
 * @description 获取天气接口服务
 * @date 2025/2/24
 */
@Service
@Data
@Slf4j
public class MockWeatherServiceImpl implements MockWeatherService {

    @Value("${weather.url:http://t.weather.itboy.net/api/weather/city/}")
    private String weatherUrl;

    @Resource
    private CityCodeService cityCodeService;

    @Override
    public Response apply(Request request) {
        // 查询城市编码
        CityCode cityCode = cityCodeService.getCityCode(request.cityName(), request.countyName());
        if (cityCode == null) {
            return new Response("您输入的地区天气查询异常");
        }
        // 调用远程api
        String apiUrl = weatherUrl + cityCode.getId();
        Map<String, Object> resultMap = HttpUtils.doJsonGet(apiUrl, null);
        if (ObjectUtils.isEmpty(resultMap)) {
            return new Response("你输入的地区天气查询异常");
        }
        String result = (String) Optional.ofNullable(resultMap.get("result")).orElse("");
        if (StringUtils.isEmpty(result)) {
            return new Response("你输入的地区天气查询异常");
        }
        /**
         * 处理查询结果
         * {"message":"success感谢又拍云(upyun.com)提供CDN赞助",
         * "status":200,"date":"20250224","time":"2025-02-24 16:07:17",
         * "cityInfo":{"city":"太原市","citykey":"101100101","parent":"山西",
         * "updateTime":"15:20"},
         * "data":{"shidu":"16%","pm25":42.0,"pm10":82.0,"quality":"良","wendu":"1.8"
         * ,"ganmao":"极少数敏感人群应减少户外活动","forecast":[{"date":"24","high":"高温 5℃","low":"低温 -6℃"
         * ,"ymd":"2025-02-24","week":"星期一","sunrise":"07:08","sunset":"18:17","aqi":70,"fx":"南风","fl"
         * :"1级","type":"阴","notice":"不要被阴云遮挡住好心情"}
          */
        JSONObject jsonObject = JSONObject.parseObject(result);
        Integer status = (Integer) Optional.ofNullable(jsonObject.get("status")).orElse(0);
        if (!status.equals(200)) {
            log.error("天气接口查询异常：{}", Optional.ofNullable(jsonObject.get("message")).orElse(""));
            return new Response("你输入的地区天气查询异常");
        }
        Object dataObj = jsonObject.get("data");
        Object forecastObj;
        if (Objects.isNull(dataObj) || (forecastObj = ((JSONObject) dataObj).get("forecast")) == null) {
            return new Response("你输入的地区天气查询异常");
        }
        String weather = (String) ((List<JSONObject>) forecastObj).getFirst().get("type");
//        List<WeatherResult.Forecast> forecasts = weatherData.forecast();
//        if (forecasts.isEmpty()) {
//            return new Response("你输入的地区天气查询异常");
//        }
//        WeatherResult.Forecast weather = forecasts.getFirst();
        return new Response(request.countyName() + "当前天气为：" + weather);
    }

}
