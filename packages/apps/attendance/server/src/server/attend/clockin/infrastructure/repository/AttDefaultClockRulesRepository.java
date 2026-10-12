package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.repository;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.aggregate.AttClockRulesAggregate;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttClockRulesRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttClockDeviceMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttClockLocationMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttClockRulesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockDevicePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockLocationPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockRulePO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.utils.AttPoAttributeSet;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttDefaultClockRulesRepository implements AttClockRulesRepository {
    private final AttClockRulesMapper attClockRulesMapper;
    private final AttClockDeviceMapper attClockDeviceMapper;
    private final AttClockLocationMapper attClockLocationMapper;

    @Override
    public void insertClockRules(AttClockRulesAggregate clockRulesAggregate) {
        // 保存规则
        AttClockRulePO attClockRulePO = AttClockRulePO.builder()
                .id(clockRulesAggregate.getId())
                .name(clockRulesAggregate.getName())
                .ways(clockRulesAggregate.getWays())
                .outClock(clockRulesAggregate.getOutClock())
                .faceRecognition(clockRulesAggregate.getFaceRecognition())
                .build();
        AttPoAttributeSet.initAddBasePoAttribute(attClockRulePO);
        attClockRulesMapper.insert(attClockRulePO);

        this.insertRulesWay(clockRulesAggregate);
    }

    @Override
    public void updateClockRules(AttClockRulesAggregate clockRulesAggregate) {
        // 获取当前登录用户
        Principal principal = SecurityContextHolder.getPrincipal();
        String loginUserId = principal == null ? "" : String.valueOf(principal.getId());
        // 修改规则
        attClockRulesMapper.update(null, Wrappers.<AttClockRulePO>lambdaUpdate().eq(AttClockRulePO::getId, clockRulesAggregate.getId())
                                           .set(StringUtils.isNotBlank(clockRulesAggregate.getName()), AttClockRulePO::getName, clockRulesAggregate.getName())
                                           .set(StringUtils.isNotBlank(clockRulesAggregate.getWays()), AttClockRulePO::getWays, clockRulesAggregate.getWays())
                                           .set(clockRulesAggregate.getOutClock() != null, AttClockRulePO::getOutClock, clockRulesAggregate.getOutClock())
                                           .set(clockRulesAggregate.getFaceRecognition() !=
                                                null, AttClockRulePO::getFaceRecognition, clockRulesAggregate.getFaceRecognition())
                                           .set(AttClockRulePO::getUpdateTime, LocalDateTime.now())
                                           .set(AttClockRulePO::getUpdateBy, loginUserId)

                                  );
        // 删除打卡方式，再重新插入
        attClockLocationMapper.delete(Wrappers.<AttClockLocationPO>lambdaQuery()
                                              .eq(AttClockLocationPO::getRulesId, clockRulesAggregate.getId()));
        attClockDeviceMapper.delete(Wrappers.<AttClockDevicePO>lambdaQuery().eq(AttClockDevicePO::getRulesId, clockRulesAggregate.getId()));

        this.insertRulesWay(clockRulesAggregate);
    }

    @Override
    public void deleteClockRules(Long id) {
        // 删除打卡方式
        attClockLocationMapper.removeByRulesId(DateUtils.toTimestamp(LocalDateTime.now()), id);
        attClockDeviceMapper.removeByRulesId(DateUtils.toTimestamp(LocalDateTime.now()), id);
        // 删除规则
        attClockRulesMapper.removeById(DateUtils.toTimestamp(LocalDateTime.now()), id);
    }

    private void insertRulesWay(AttClockRulesAggregate clockRulesAggregate) {
        // 保存gps
        if (clockRulesAggregate.getLocationList() != null && !clockRulesAggregate.getLocationList().isEmpty()) {
            // 这里应该先遍历组装PO集合，后用批量插入
            clockRulesAggregate.getLocationList().forEach(locationPO -> {
                AttClockLocationPO attClockLocationPO = AttClockLocationPO.builder()
                        .id(IdWorker.getId())
                        .rulesId(clockRulesAggregate.getId())
                        .address(locationPO.getAddress())
                        .latitude(locationPO.getLatitude())
                        .longitude(locationPO.getLongitude())
                        .ranges(locationPO.getRanges())
                        .build();
                AttPoAttributeSet.initAddBasePoAttribute(attClockLocationPO);
                attClockLocationMapper.insert(attClockLocationPO);
            });
        }
        // 保存考勤机
        if (clockRulesAggregate.getDeviceList() != null && !clockRulesAggregate.getDeviceList().isEmpty()) {
            // 这里应该先遍历组装PO集合，后用批量插入
            clockRulesAggregate.getDeviceList().forEach(device -> {
                AttClockDevicePO attClockDevicePO = AttClockDevicePO.builder()
                        .id(IdWorker.getId())
                        .deviceId(device.getId())
                        .rulesId(clockRulesAggregate.getId())
                        .name(device.getName())
                        .genreName(device.getGenreName())
                        .modelName(device.getModelName())
                        .code(device.getCode())
                        .build();
                AttPoAttributeSet.initAddBasePoAttribute(attClockDevicePO);
                attClockDeviceMapper.insert(attClockDevicePO);
            });
        }
    }
}
