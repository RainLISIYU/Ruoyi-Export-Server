package com.ruoyi.business.service;

import com.ruoyi.business.domain.CityCode;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 */
public interface CityCodeService extends IService<CityCode> {

    /**
     * 查询城市编码
     *
     * @param cityName 城市名称
     * @return 城市编码
     */
    CityCode getCityCode(String cityName, String countyName);

}
