package com.guhecloud.cube.srv.gen.securitymanagement.server.medicalgas.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceBimSceneStdClient;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.SecurityDevice;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.SecurityCommonService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.medicalgas.dao.MedicalGasDataMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.medicalgas.entity.MedicalGasData;
import com.guhecloud.cube.srv.gen.securitymanagement.server.medicalgas.param.GasStatisticsParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.medicalgas.service.MedicalGasDataService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.medicalgas.vo.MedicalGasDataPageVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.medicalgas.vo.MedicalGasDataVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MedicalGasDataServiceImpl extends ServiceImpl<MedicalGasDataMapper, MedicalGasData> implements MedicalGasDataService {
    private final MedicalGasDataMapper medicalGasDataMapper;
    private final DeviceBimSceneStdClient deviceBimSceneClient;
    private final SecurityCommonService securityCommonService;

    @Override
    public List<MedicalGasDataVo> getTodayGasUsage(GasStatisticsParam param) {
        Set<Long> bimDeviceIds = deviceBimSceneClient.getBimDeviceIds(param.getSceneType(), param.getAreaId());
        List<MedicalGasDataVo> medicalGasDataVos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(bimDeviceIds)) {
            List<String> thirdIds = securityCommonService
                    .list(Wrappers.<SecurityDevice>lambdaQuery().in(SecurityDevice::getDeviceId, bimDeviceIds))
                    .stream().map(SecurityDevice::getThirdPartyId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(thirdIds)) {
                param.setThirdDeviceIds(thirdIds);
                List<MedicalGasDataVo> todayGasUsage = medicalGasDataMapper.getTodayGasUsage(param, param.getPeriod().getBegin(), param
                        .getPeriod().getEnd());
                var oldBegin = DateUtils.offset(param.getPeriod().getBegin(), ChronoUnit.DAYS, -1);
                var oldEnd = DateUtils.offset(param.getPeriod().getEnd(), ChronoUnit.DAYS, -1);
                List<MedicalGasDataVo> todayOldGasUsage = medicalGasDataMapper.getTodayGasUsage(param, oldBegin, oldEnd);
                Map<String, MedicalGasDataVo> collect = null;
                if (CollectionUtils.isNotEmpty(todayOldGasUsage)) {
                    collect = todayOldGasUsage.stream().collect(Collectors.toMap(MedicalGasDataVo::getName, s -> s));
                    for (MedicalGasDataVo medicalGasDataVo : todayGasUsage) {
                        MedicalGasDataVo medicalGasDataVo1 = collect.get(medicalGasDataVo.getName());
                        if (Objects.nonNull(medicalGasDataVo1)) {
                            BigDecimal rate = getRate(String.valueOf(medicalGasDataVo1.getNum()), String.valueOf(medicalGasDataVo.getNum()));
                            medicalGasDataVo.setRatio(medicalGasDataVo.getRatio().add(rate));
                        }
                    }

                }

                medicalGasDataVos = todayGasUsage;

            }

        }

        return medicalGasDataVos;
    }

    @Override
    public List<MedicalGasDataVo> getTodayGasUsageTop(GasStatisticsParam param) {
        Set<Long> bimDeviceIds = deviceBimSceneClient.getBimDeviceIds(param.getSceneType(), param.getAreaId());
        List<MedicalGasDataVo> medicalGasDataVos = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(bimDeviceIds)) {
            List<String> thirdIds = securityCommonService
                    .list(Wrappers.<SecurityDevice>lambdaQuery().in(SecurityDevice::getDeviceId, bimDeviceIds))
                    .stream().map(SecurityDevice::getThirdPartyId).collect(Collectors.toList());
            param.setThirdDeviceIds(thirdIds);
            medicalGasDataVos = medicalGasDataMapper.getGasUsageTop(param);

        }

        return medicalGasDataVos;
    }

    @Override
    public Map<String, Object> getGasUsageTrend(GasStatisticsParam param) {
        Map<String, Object> remap = new HashMap<>();
        Set<Long> bimDeviceIds = deviceBimSceneClient.getBimDeviceIds(param.getSceneType(), param.getAreaId());
        List<String> xtitle = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(bimDeviceIds)) {
            List<String> thirdIds = securityCommonService
                    .list(Wrappers.<SecurityDevice>lambdaQuery().in(SecurityDevice::getDeviceId, bimDeviceIds))
                    .stream().map(SecurityDevice::getThirdPartyId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(thirdIds)) {
                String timeFlag = "";
                if (param.getType().equals("TODAY")) {
                    timeFlag = "'HH'";
                    for (int i = 0; i < 24; i++) {
                        var offset = DateUtils.offset(param.getPeriod().getBegin(), ChronoUnit.HOURS, i);
                        int hour = offset.getHour();
                        if (hour < 10) {
                            xtitle.add("0" + hour);
                        } else {
                            xtitle.add(String.valueOf(hour));
                        }

                    }
                } else if (param.getType().equals("THIS_WEEK")) {
                    timeFlag = "'yyyy-MM-dd'";
                    int between = (int) DateUtils.between(param.getPeriod().getBegin(), param.getPeriod().getEnd(), ChronoUnit.DAYS);
                    for (int i = 0; i < between; i++) {
                        var offset = DateUtils.offset(param.getPeriod().getBegin(), ChronoUnit.DAYS, i);
                        xtitle.add(DateUtils.format(offset, "yyyy-MM-dd"));
                    }
                } else if (param.getType().equals("THIS_MONTH")) {
                    timeFlag = "'yyyy-MM-dd'";
                    int between = (int) DateUtils.between(param.getPeriod().getBegin(), param.getPeriod().getEnd(), ChronoUnit.DAYS);
                    for (int i = 0; i < between; i++) {
                        var offset = DateUtils.offset(param.getPeriod().getBegin(), ChronoUnit.DAYS, i);
                        xtitle.add(DateUtils.format(offset, "yyyy-MM-dd"));
                    }
                } else if (param.getType().equals("THIS_YEAR")) {
                    timeFlag = "'yyyy-MM'";
                    for (int i = 1; i < 12; i++) {
                        var offset = DateUtils.offset(param.getPeriod().getBegin(), ChronoUnit.MONTHS, i);
                        xtitle.add(DateUtils.format(offset, "yyyy-MM"));
                    }
                }
                param.setTimeFormat(timeFlag);
                param.setThirdDeviceIds(thirdIds);
                List<MedicalGasDataVo> medicalGasDataVos = medicalGasDataMapper.getGasUsageTrend(param);
                Map<String, List<MedicalGasDataVo>> collect = medicalGasDataVos.stream()
                        .collect(Collectors.groupingBy(MedicalGasDataVo::getName));
                remap.put("medicalGasDataVos", collect);
            }

        }
        remap.put("xtitle", xtitle);
        return remap;
    }

    @Override
    public Pagination<MedicalGasDataPageVo> getGasUsagePage(GasStatisticsParam param) {
        Page<MedicalGasDataPageVo> gasUsagePage = medicalGasDataMapper.getGasUsagePage(PagingUtils.convert(param), param);
        return PagingUtils.convert(gasUsagePage);
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
