package com.ruoyi.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.business.domain.CityCode;
import com.ruoyi.business.service.CityCodeService;
import com.ruoyi.business.mapper.CityCodeMapper;
import com.ruoyi.common.core.utils.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class CityCodeServiceImpl extends ServiceImpl<CityCodeMapper, CityCode>
    implements CityCodeService{

    @Override
    public CityCode getCityCode(String cityName, String countyName) {
        LambdaQueryWrapper<CityCode> queryWrapper = new LambdaQueryWrapper<CityCode>().eq(CityCode::getCounty, countyName);
        if (StringUtils.isNotEmpty(cityName)) {
            queryWrapper.eq(CityCode::getCity, cityName);
        }
        List<CityCode> list = this.list(queryWrapper);
        if (list.size() == 1) {
            return list.getFirst();
        }
        return null;
    }
}




