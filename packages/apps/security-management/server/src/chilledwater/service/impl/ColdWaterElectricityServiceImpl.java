package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao.ColdWaterElectricityMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.ColdWaterElectricity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.StatisticsParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service.ColdWaterElectricityService;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ColdWaterElectricityServiceImpl extends ServiceImpl<ColdWaterElectricityMapper, ColdWaterElectricity> implements ColdWaterElectricityService {

    private final ColdWaterElectricityMapper coldWaterElectricityMapper;

    @Override
    public Map<String, Object> getColdWaterElectricity(StatisticsParam param) {
        if (param.getTimeType().equals("Year")) {
            var dateTime = DateUtils.endOfYear(DateUtils.parse(param.getStartTime()));
            param.setEndTime(DateUtils.format(dateTime, "yyyy-MM-dd"));
        }
        BigDecimal coldWaterElectricity = coldWaterElectricityMapper.getColdWaterElectricity(param.getStartTime(), param.getEndTime());
        BigDecimal electricityRateQoq = BigDecimal.ZERO;
        BigDecimal electricityRateYoy = BigDecimal.ZERO;
        if (param.getTimeType().equals("Month")) {
            String startTimeQoq = DateUtils.formatDate(DateUtils.offset(DateUtils.parse(param.getStartTime()), ChronoUnit.MONTHS, -1));
            String endTimeQoq = DateUtils.formatDate(DateUtils.offset(DateUtils.parse(param.getEndTime()), ChronoUnit.MONTHS, -1));
            BigDecimal coldWaterElectricityQoq = coldWaterElectricityMapper.getColdWaterElectricity(startTimeQoq, endTimeQoq);
            String startTimeYoy = DateUtils.formatDate(DateUtils.offset(DateUtils.parse(param.getStartTime()), ChronoUnit.YEARS, -1));
            String endTimeYoy = DateUtils.formatDate(DateUtils.offset(DateUtils.parse(param.getEndTime()), ChronoUnit.YEARS, -1));
            BigDecimal coldWaterElectricityYoy = coldWaterElectricityMapper.getColdWaterElectricity(startTimeYoy, endTimeYoy);
            BigDecimal rateQoq = getRate(coldWaterElectricityQoq.toString(), coldWaterElectricity.toString());
            electricityRateQoq = electricityRateQoq.add(rateQoq);
            BigDecimal rateYoy = getRate(coldWaterElectricityYoy.toString(), coldWaterElectricity.toString());
            electricityRateYoy = electricityRateYoy.add(rateYoy);

        } else if (param.getTimeType().equals("Day")) {
            String startTimeQoq = DateUtils.formatDate(DateUtils.offset(DateUtils.parse(param.getStartTime()), ChronoUnit.DAYS, -1));
            String endTimeQoq = DateUtils.formatDate(DateUtils.offset(DateUtils.parse(param.getEndTime()), ChronoUnit.DAYS, -1));
            BigDecimal coldWaterElectricityQoq = coldWaterElectricityMapper.getColdWaterElectricity(startTimeQoq, endTimeQoq);
            String startTimeYoy = DateUtils.formatDate(DateUtils.offset(DateUtils.parse(param.getStartTime()), ChronoUnit.MONTHS, -1));
            String endTimeYoy = DateUtils.formatDate(DateUtils.offset(DateUtils.parse(param.getEndTime()), ChronoUnit.MONTHS, -1));
            BigDecimal coldWaterElectricityYoy = coldWaterElectricityMapper.getColdWaterElectricity(startTimeYoy, endTimeYoy);
            BigDecimal rateQoq = getRate(coldWaterElectricityQoq.toString(), coldWaterElectricity.toString());
            electricityRateQoq = electricityRateQoq.add(rateQoq);
            BigDecimal rateYoy = getRate(coldWaterElectricityYoy.toString(), coldWaterElectricity.toString());
            electricityRateYoy = electricityRateYoy.add(rateYoy);
        }
        Map<String, Object> remap = new HashMap<>();
        remap.put("coldWaterElectricity", coldWaterElectricity);
        remap.put("electricityRateQoq", electricityRateQoq);
        remap.put("electricityRateYoy", electricityRateYoy);
        return remap;
    }

    private BigDecimal getRate(String a, String b) {
        BigDecimal previous = new BigDecimal(a);
        BigDecimal postValue = new BigDecimal(b);
        BigDecimal subtract = postValue.subtract(previous);
        BigDecimal divide = BigDecimal.ZERO;
        if (previous.compareTo(BigDecimal.ZERO) == 0) {
            divide = divide.add(new BigDecimal(100));
        } else {
            divide = subtract.divide(previous, 2, RoundingMode.HALF_UP);
        }
        return divide;
    }
}
