package com.guhecloud.cube.base.device.center.statistics.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.device.center.statistics.mapper.BizDeviceStatisticsMapper;
import com.guhecloud.cube.base.device.center.statistics.model.BizDeviceStatistics;
import com.guhecloud.cube.base.device.center.statistics.service.BizDeviceStatisticsService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BizDeviceStatisticsServiceImpl extends ServiceImpl<BizDeviceStatisticsMapper, BizDeviceStatistics> implements BizDeviceStatisticsService {

    @Override
    public void saveStateLog(Long iotDeviceId, int value) {
        BizDeviceStatistics one = this.getById(iotDeviceId);
        if (ObjectUtils.isNull(one)) {
            one = BizDeviceStatistics.builder()
                    .id(iotDeviceId)
                    .deviceState(value)
                    .reportTime(LocalDateTime.now())
                    .onlineNum(0)
                    .onlineTime(0L)
                    .offlineNum(0)
                    .offlineTime(0L)
                    .build();
            this.save(one);
        } else {
            Integer state = one.getDeviceState();
            if (state != value) {
                int onlineNum = one.getOnlineNum();
                int offlineNum = one.getOfflineNum();
                if (value == 1) {
                    one.setOnlineNum(++onlineNum);
                    if (state == 0) {
                        one.setOfflineTime(
                                one.getOfflineTime() + DateUtils.between(one.getReportTime(), LocalDateTime.now(), ChronoUnit.MINUTES));
                    }
                } else {
                    one.setOfflineNum(++offlineNum);
                    if (state == 1) {
                        one.setOnlineTime(
                                one.getOnlineTime() + DateUtils.between(one.getReportTime(), LocalDateTime.now(), ChronoUnit.MINUTES));
                    }
                }
                one.setDeviceState(value);
                one.setReportTime(LocalDateTime.now());
                this.updateById(one);
            }
        }
    }

    @Override
    public Long countOnlineTime(Long id) {
        BizDeviceStatistics one = this.getById(id);
        return ObjectUtils.isNull(one) ? 0 : (one.getOnlineTime() / 60);
    }

    @Override
    public Map<Long, BizDeviceStatistics> deviceStatistics(Set<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyMap();
        }
        return this.list(Wrappers.<BizDeviceStatistics>lambdaQuery()
                                 .in(BizDeviceStatistics::getId, ids))
                .stream().collect(Collectors.toMap(BizDeviceStatistics::getId, s -> s));
    }

    @Override
    public Integer countOffLineNum(Long id) {
        BizDeviceStatistics one = this.getById(id);
        return ObjectUtils.isNull(one) ? 0 : one.getOfflineNum();
    }
}
