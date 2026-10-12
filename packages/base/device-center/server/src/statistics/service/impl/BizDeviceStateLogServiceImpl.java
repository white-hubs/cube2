package com.guhecloud.cube.base.device.center.statistics.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.device.center.statistics.mapper.BizDeviceStateLogMapper;
import com.guhecloud.cube.base.device.center.statistics.model.BizDeviceStateLog;
import com.guhecloud.cube.base.device.center.statistics.service.BizDeviceStateLogService;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * @author wql
 * @describe 设备状态变更记录
 * @date 2024/1/22 16:53
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BizDeviceStateLogServiceImpl extends ServiceImpl<BizDeviceStateLogMapper, BizDeviceStateLog> implements BizDeviceStateLogService {
    @Resource
    private final BizDeviceStateLogMapper bizDeviceStateLogMapper;

    @Override
    public void saveStateLog(Long iotDeviceId, int value) {
        Integer state = bizDeviceStateLogMapper.getLastState(iotDeviceId);
        if (state == null || state != value) {
            this.save(BizDeviceStateLog.builder()
                              .deviceId(iotDeviceId)
                              .deviceState(value)
                              .createTime(LocalDateTime.now())
                              .build());
        }
    }

    @Override
    public Long countOnlineTime(Long id) {
        List<BizDeviceStateLog> list = bizDeviceStateLogMapper.selectLogByDeviceId(id);
        if (ObjectUtils.isEmpty(list)) {
            return 0L;
        }
        long onlineTime = 0;
        LocalDateTime startTime = null;
        LocalDateTime endTime;
        for (BizDeviceStateLog stateLog : list) {
            if (stateLog.getDeviceState() == 1 && startTime == null) {
                startTime = stateLog.getCreateTime();
            }
            if (stateLog.getDeviceState() == 0) {
                endTime = stateLog.getCreateTime();
                if (startTime != null && endTime != null) {
                    onlineTime += DateUtils.between(startTime, endTime, ChronoUnit.HOURS);
                    startTime = null;
                }
            }
        }
        onlineTime += startTime == null ? 0 : DateUtils.between(startTime, LocalDateTime.now(), ChronoUnit.HOURS);
        return onlineTime;
    }

    @Override
    public Integer countOffLineNum(Long id) {
        return bizDeviceStateLogMapper.countOffLineNum(id);
    }
}
