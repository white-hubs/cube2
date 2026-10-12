package com.guhecloud.cube.srv.gen.device.supervise.server.analysis.application.service;

import com.guhecloud.platform.base.util.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Sets;
import com.guhecloud.cube.base.device.center.metadata.dto.DeviceBaseArchivesInfoDTO;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceBimSceneStdClient;
import com.guhecloud.cube.base.device.center.std.devcenter.DeviceCenterStdClient;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.dao.DeviceHealthValueMapper;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.infrastructure.po.DeviceHealthValuePO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo.DeviceAvgHealthVO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo.HealthLevelVO;
import com.guhecloud.cube.srv.gen.device.supervise.server.analysis.interfaces.vo.HealthPreviewVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author pengjian
 * @date 2022/8/1 16:23
 * @description
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DeviceHealthValueService extends ServiceImpl<DeviceHealthValueMapper, DeviceHealthValuePO> {
    private final DeviceBimSceneStdClient deviceBimSceneClient;
    private final DeviceCenterStdClient deviceCenterStdClient;
    private final DeviceHealthValueMapper healthValueMapper;

    public HealthLevelVO getHealthLevelRate(Set<Long> deviceIds) {
        if (CollectionUtils.isEmpty(deviceIds)) {
            return null;
        }
        HealthLevelVO healthLevelVO = healthValueMapper.selectHealthLevelNumber(deviceIds);
        if (Objects.isNull(healthLevelVO)) {
            return null;
        }
        // 计算占比
        int size = deviceIds.size();
        float bestRate = healthLevelVO.getBestNumber() * 1.0f / size;
        float normalRate = healthLevelVO.getNormalNumber() * 1.0f / size;
        float subNormalRate = healthLevelVO.getSubNormalNumber() * 1.0f / size;
        float unhealthyRate = healthLevelVO.getUnhealthyNumber() * 1.0f / size;
        healthLevelVO.setBestRate(new BigDecimal(bestRate).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
        healthLevelVO.setNormalRate(new BigDecimal(normalRate).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
        healthLevelVO.setSubNormalRate(new BigDecimal(subNormalRate).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
        healthLevelVO.setUnhealthyRate(new BigDecimal(unhealthyRate).setScale(2, BigDecimal.ROUND_HALF_UP).floatValue());
        return healthLevelVO;
    }

    public Integer getHealthValueBySceneType(Long sceneType) {
        /**
         * 计算健康等级的数量和
         */
        Set<Long> bimDeviceIds = deviceBimSceneClient.getBimDeviceIds(sceneType, null);

        if ((bimDeviceIds.isEmpty())) {
            return null;
        }
        return getAvgHealth(bimDeviceIds);
    }

    public DeviceAvgHealthVO getHealthValueByDeviceTag(String deviceTag) {
        List<DeviceBaseArchivesInfoDTO> devices = deviceCenterStdClient.getListByTag(Collections.singleton(deviceTag));
        if (CollectionUtils.isEmpty(devices)) {
            return null;
        }
        Set<Long> deviceIds = devices.stream().map(DeviceBaseArchivesInfoDTO::getId).collect(Collectors.toSet());
        Integer avgHealth = getAvgHealth(deviceIds);
        //低于60数量
        LocalDate yesterday = LocalDate.now().plusDays(-1);
        Long lowCount = healthValueMapper.selectCount(
                Wrappers.<DeviceHealthValuePO>lambdaQuery()
                        .in(DeviceHealthValuePO::getDeviceId, deviceIds)
                        .lt(DeviceHealthValuePO::getHealthValue, 60)
                        .between(DeviceHealthValuePO::getCreateTime, yesterday, LocalDateTime.now())
                                                     );
        return DeviceAvgHealthVO.builder()
                .totalDeviceNumber(deviceIds.size())
                .avgHealthValue(avgHealth.doubleValue())
                .belowSixtyNumber(lowCount)
                .belowSixtyRate(BigDecimal.valueOf(lowCount).divide(BigDecimal.valueOf(deviceIds.size()), 2, RoundingMode.HALF_UP)
                                        .doubleValue())
                .build();
    }

    private Integer getAvgHealth(Set<Long> deviceIds) {
        LocalDate yesterday = LocalDate.now().plusDays(-1);
        List<DeviceHealthValuePO> deviceHealthValuePOS = healthValueMapper.selectList(
                Wrappers.<DeviceHealthValuePO>lambdaQuery()
                        .in(DeviceHealthValuePO::getDeviceId, deviceIds)
                        .between(DeviceHealthValuePO::getCreateTime, yesterday, LocalDateTime.now()));
        int sum = deviceHealthValuePOS.stream().mapToInt(DeviceHealthValuePO::getHealthValue).sum();
        if (deviceHealthValuePOS.isEmpty()) {
            return 0;
        }
        return sum / deviceHealthValuePOS.size();
    }

    public HealthPreviewVO getHealthPreviewBySceneType(Long sceneType) {
        /**
         * 计算健康等级的数量和
         */
        Set<Long> bimDeviceIds = deviceBimSceneClient.getBimDeviceIds(sceneType, null);

        if ((bimDeviceIds.isEmpty())) {
            return null;
        }

        // 昨日开始时间
        LocalDate yesterday = LocalDate.now().plusDays(-1);
        // 昨日结束时间

        List<DeviceHealthValuePO> deviceHealthValuePOS = healthValueMapper.selectList(
                Wrappers.<DeviceHealthValuePO>lambdaQuery()
                        .in(DeviceHealthValuePO::getDeviceId, bimDeviceIds)
                        .between(DeviceHealthValuePO::getCreateTime, yesterday, yesterday)
                                                                                     );
        if (CollectionUtils.isEmpty(deviceHealthValuePOS)) {
            return HealthPreviewVO.builder()
                    .bestNumber(0)
                    .bestRate(0.0f)
                    .healthValue(0)
                    .normalNumber(0)
                    .normalRate(0.0f)
                    .subNormalNumber(0)
                    .subNormalRate(0.0f)
                    .totalNumber(0)
                    .unhealthyNumber(0)
                    .unhealthyRate(0.0f)
                    .build();
        }

        if (CollectionUtils.isEmpty(deviceHealthValuePOS)) {
            return HealthPreviewVO.builder()
                    .bestNumber(0)
                    .bestRate(0.0f)
                    .healthValue(0)
                    .normalNumber(0)
                    .normalRate(0.0f)
                    .subNormalNumber(0)
                    .subNormalRate(0.0f)
                    .totalNumber(0)
                    .unhealthyNumber(0)
                    .unhealthyRate(0.0f)
                    .build();
        }

        int sum = deviceHealthValuePOS.stream().mapToInt(value -> value.getHealthValue()).sum();
        int avgHealthValue = 0;
        if (deviceHealthValuePOS.size() != 0) {
            avgHealthValue = sum / deviceHealthValuePOS.size();
        }

        HealthLevelVO healthLevelRate = getHealthLevelRate(Sets.newHashSet(bimDeviceIds));
        if (Objects.isNull(healthLevelRate)) {
            return null;
        }
        HealthPreviewVO healthPreviewVO = ObjectUtils.copying(healthLevelRate, HealthPreviewVO.class);

        healthPreviewVO.setHealthValue(avgHealthValue);
        healthPreviewVO.setTotalNumber(bimDeviceIds.size());
        return healthPreviewVO;
    }
}
