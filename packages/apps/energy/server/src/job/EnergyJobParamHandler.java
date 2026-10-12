package com.guhecloud.cube.srv.gen.energymanagement.server.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.dictionary.client.SysDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.SysDictionaryDTO;
import com.guhecloud.cube.srv.gen.energymanagement.server.dao.MeterDeviceMapper;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.MeterDevice;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import com.guhecloud.platform.job.jobtask.enums.JobParamEnums;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class EnergyJobParamHandler {

    @Autowired
    private SysDictionaryClient sysDictionaryClient;
    @Autowired
    private MeterDeviceMapper meterDeviceMapper;

    /**
     * 获取job参数
     *
     * @param code 字典项(跟job名对应)
     *
     * @return 按租户的参数
     */
    public String getJobParam(String code) {
        /*
         * 获取job参数：
         *   租户之间用“;”分割，租户内参数用“,”分割
         *   例如：58197260,2025-09-22 00:12:12;51436584,2025-09-23 00:00:00
         *   租户内的参数可以只传租户不要后面的日期，例如：58197260;51436584
         */
        SessionContextHolder.clearSessionContext();
        SysDictionaryDTO sysDictionaryDTO = null;
        try {
            sysDictionaryDTO = sysDictionaryClient.getSysDictionaryByCodeAndItemCode(JobParamEnums.JOB_PARAM_ITEM_CODE.name(), code);
        } catch (Exception e) {
            log.error("能碳获取租户参数异常", e);
        }
        if (sysDictionaryDTO != null && StringUtils.isNotBlank(sysDictionaryDTO.getVariable())) {
            return sysDictionaryDTO.getVariable();
        }
        log.warn("能碳采集未获取到字典租户参数，{}", code);
        var tenantList = meterDeviceMapper.selectList(Wrappers.<MeterDevice>lambdaQuery().select(MeterDevice::getTenant)
                                                              .groupBy(MeterDevice::getTenant));
        return Optional.ofNullable(tenantList).orElse(new ArrayList<>())
                .stream()
                .map(BasicEnhancedTenantEntity::getTenant)
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .collect(Collectors.joining(";"));
    }
}
