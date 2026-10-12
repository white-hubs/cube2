package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration.ClockInEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttClockInLedgerDetailsRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttClockInRecordsRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttClockInLedgerDetailsMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInLedgerDetailsPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInLedgerPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInRecordsPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.AttClockInDetailsParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.application.querygateway.AttClassesQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesRecordPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.dto.AttSchedulingClassesDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.querygateway.AttSchedulingQueryService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-27  16:58
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttClockInLedgerDetailsImpl extends ServiceImpl<AttClockInLedgerDetailsMapper, AttClockInLedgerDetailsPO> implements AttClockInLedgerDetailsRepository {
    private final AttSchedulingQueryService schedulingQueryService;
    private final AttClockInRecordsRepository clockInRecordsRepository;
    private final UserClient userClient;
    private final AttClassesQueryService classesQueryService;

    @Override
    public int addLedgerDetails(AttClockInLedgerPO iedger, String month) {
        log.info("att:开始生成{}{}{}月的台账详情", iedger.getUserId(), iedger.getUserName(), month);
        List<AttClockInLedgerDetailsPO> ledgerDetailsList = new ArrayList<>();
        LocalDateTime date = DateUtils.parseDate(month + "-01");
        LocalDateTime startTiem = DateUtils.beginOfMonth(date);
        LocalDateTime endTiem = DateUtils.endOfMonth(date);
        int today = date.getDayOfMonth();
        // 每月最多31天
        for (int i = 0; i < endTiem.getDayOfMonth(); i++) {
            AttClockInLedgerDetailsPO details = new AttClockInLedgerDetailsPO();
            if (i + 1 < today) {
                details.setClassesDetails(ClockInEnum.REST.getRemarks());
                details.setState(ClockInEnum.REST.name());
            } else {
                details.setState(ClockInEnum.NO_CLOCK.name());
            }
            details.setId(IdWorker.getId());
            details.setIedgerId(iedger.getId());
            details.setUserId(iedger.getUserId());
            details.setUserName(iedger.getUserName());
            details.setLedgerBelongDate(DateUtils.offsetDay(startTiem, i));
            details.setWeek(DateUtils.dayOfWeekFormat(details.getLedgerBelongDate()));
            details.setToday(details.getLedgerBelongDate().getDayOfMonth());
            details.setCreateTime(LocalDateTime.now());
            details.setTenant(SessionContextHolder.getRequester().getTenant());
            ledgerDetailsList.add(details);
        }
        return this.baseMapper.addLedgerDetails(ledgerDetailsList);
    }

    @Override
    public int addLedgerDetails(Long iedgerId, AttClockInRecordsPO attClockInRecords) {
        AttClockInLedgerDetailsPO details = new AttClockInLedgerDetailsPO();
        details.setIedgerId(iedgerId);
        details.setState(ClockInEnum.NORMAL.name().equals(attClockInRecords.getState()) ? ClockInEnum.NORMAL.name()
                                                                                        : ClockInEnum.ABNORMAL_WORK.name());
        details.setClassesId(attClockInRecords.getClassesId());
        details.setClassesVersion(attClockInRecords.getClassesVersion());
        details.setClockInUuid(attClockInRecords.getUuid());
        if (ObjectUtils.isNotEmpty(attClockInRecords.getActualClockInTime())) {
            details.setStartWorkTime(DateUtils.format(attClockInRecords.getActualClockInTime(), "HH:mm"));
        }
        details.setWeek(DateUtils.dayOfWeekFormat(attClockInRecords.getClockInDate()));
        details.setToday(attClockInRecords.getClockInDate().getDayOfMonth());
        details.setCreateTime(LocalDateTime.now());
        details.setTenant(SessionContextHolder.getRequester().getTenant());
        if (ClockInEnum.START.name().equals(attClockInRecords.getClockType())) {
            details.setStartClockInTime(attClockInRecords.getRequireClockInTime());
        }
        return this.baseMapper.insert(details);
    }

    @Override
    public Boolean updateRest() {
        log.info("开始写入{}日休息台账", LocalDateTime.now());
        // 更新今日休息
        List<AttClockInLedgerDetailsPO> ledgerDetails = this.baseMapper.selectList(
                new LambdaQueryWrapper<AttClockInLedgerDetailsPO>()
                        .lt(AttClockInLedgerDetailsPO::getLedgerBelongDate, LocalDateTime.now())
                        .eq(AttClockInLedgerDetailsPO::getState, ClockInEnum.NO_CLOCK.name()));
        ledgerDetails.parallelStream().forEach(ex -> {
            SessionContextHolder.requestTenant(ex.getTenant());
            try {
                List<AttSchedulingClassesDTO> classesList = schedulingQueryService.getStaffDayScheduling(ex.getUserId(),
                                                                                                         DateUtils.format(
                                                                                                                 ex.getLedgerBelongDate(),
                                                                                                                 "yyyy-MM-dd"));
                Boolean bool = ObjectUtils.isEmpty(classesList) ? true :
                               classesList.size() == 1 && ObjectUtils.isEmpty(classesList.get(0).getId()) ? true : false;
                if (bool) {
                    ex.setState(ClockInEnum.REST.name());
                    ex.setClassesDetails(ClockInEnum.REST.getRemarks());
                    ex.setStartClockInTime(ex.getLedgerBelongDate());
                    ex.setEndClockInTime(ex.getLedgerBelongDate());
                    this.baseMapper.updateById(ex);
                } else {
                    UserDTO user = userClient.getUser(ex.getUserId());
                    classesList.stream().forEach(classes -> {
                        AttClassesRecordPO classesversion = classesQueryService.getClassesRecordById(ex.getClassesId(),
                                                                                                     ex.getClassesVersion());
                        clockInRecordsRepository.addClock(user, classesversion, ex.getLedgerBelongDate(), null);
                    });
                }
            } catch (Exception e) {
                log.error("更新休息台账失败：{}", ex.getId(), e);
            }
        });
        log.info("写入休息数据结束");
        return true;
    }

    @Override
    public Boolean updateLedgerDetailsState() {
        List<AttClockInLedgerDetailsPO> clockInLedgerDetails = this.baseMapper.selectList(
                new LambdaQueryWrapper<AttClockInLedgerDetailsPO>()
                        .eq(AttClockInLedgerDetailsPO::getState, ClockInEnum.ABNORMAL_WORK.name())
                        .eq(AttClockInLedgerDetailsPO::getStartWorkState, ClockInEnum.LACK_CHECKED.name())
                        .eq(AttClockInLedgerDetailsPO::getEndWorkState, ClockInEnum.LACK_CHECKED.name()));
        clockInLedgerDetails.parallelStream().forEach(ex -> {
            if (ObjectUtils.isNotEmpty(ex.getClockInUuid())) {
            }
        });
        return true;
    }

    @Override
    public Integer getLedgerDetailsAbnormalNumber(AttClockInDetailsParam param) {
        return this.baseMapper.getLedgerDetailsAbnormalNumber(param);
    }

}
