package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration.ClockInEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttStaffClockRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttClockInRecordsMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInRecordsPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesRecordPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingMapper;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.IdUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttStaffClockRepositoryImpl implements AttStaffClockRepository {

    private final AttClockInRecordsMapper baseMapper;
    private final AttSchedulingMapper schedulingMapper;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
    public List<AttClockInRecordsPO> insertAttClockInRecords(UserDTO user, AttClassesRecordPO classes, LocalDateTime classDate,
                                                             Long schedulingId
                                                            ) {
        List<AttClockInRecordsPO> attClockInRecordsPOS = this.baseMapper.selectList(new LambdaQueryWrapper<AttClockInRecordsPO>()
                                                                                            .eq(AttClockInRecordsPO::getUserId, user.getId())
                                                                                            .eq(AttClockInRecordsPO::getClassesId, classes.getClassesId())
                                                                                            .eq(AttClockInRecordsPO::getClockInDate, DateUtils.formatDate(classDate))
                                                                                            .in(AttClockInRecordsPO::getClockType, ClockInEnum.clockType)
                                                                                            .orderByDesc(AttClockInRecordsPO::getClockType));
        if (ObjectUtils.isEmpty(attClockInRecordsPOS)) {
            String uuid = IdUtils.fastUUIDString();
            // 新增上班打卡记录
            this.baseMapper.insert(this.getAttClockInRecordsPO(user, classes, uuid, ClockInEnum.START.name(), classDate));
            // 新增下班打卡记录
            this.baseMapper.insert(this.getAttClockInRecordsPO(user, classes, uuid, ClockInEnum.END.name(), classDate));
            if (Objects.nonNull(schedulingId)) {
                schedulingMapper.updateSchedulingByClock(schedulingId);
            }
            attClockInRecordsPOS = this.baseMapper.selectList(new LambdaQueryWrapper<AttClockInRecordsPO>()
                                                                      .eq(AttClockInRecordsPO::getUserId, user.getId())
                                                                      .eq(AttClockInRecordsPO::getClassesId, classes.getClassesId())
                                                                      .eq(AttClockInRecordsPO::getClockInDate, DateUtils.formatDate(classDate))
                                                                      .in(AttClockInRecordsPO::getClockType, ClockInEnum.clockType)
                                                                      .orderByDesc(AttClockInRecordsPO::getClockType));
        }
        return attClockInRecordsPOS;
    }

    private AttClockInRecordsPO getAttClockInRecordsPO(UserDTO user, AttClassesRecordPO classes, String uuid, String clockType,
                                                       LocalDateTime classDate
                                                      ) {
        AttClockInRecordsPO attClockInRecords = new AttClockInRecordsPO();
        Principal principal = SecurityContextHolder.getPrincipal();
        attClockInRecords.setUserId(principal.getId());
        attClockInRecords.setUserName(principal.getName());
        attClockInRecords.setClassesId(classes.getClassesId());
        attClockInRecords.setClassesVersion(classes.getVersion());
        attClockInRecords.setClockType(clockType);
        attClockInRecords.setUuid(uuid);
        attClockInRecords.setClockInDate(classDate);
        attClockInRecords.setOrgId(Optional.ofNullable(user.getOrganization()).orElse(new UserDTO.Organization()).getId());
        if (ClockInEnum.START.name().equals(clockType)) {
            attClockInRecords.setClockInTime(classes.getStartWorkTime());
            attClockInRecords.setRequireClockInTime(DateUtils.parse(DateUtils.formatDate(classDate) + " " + classes.getStartWorkTime()));
            attClockInRecords.setClockBeforeTime(DateUtils.parse(
                    DateUtils.formatDate(classDate) + " " + classes.getStartClockBeforeTime()));
            attClockInRecords.setClockAfterTime(DateUtils.parse(DateUtils.formatDate(classDate) + " " + classes.getStartClockAfterTime()));
        } else {
            // 下班是第二天需要偏移一天
            LocalDateTime endTime =
                    classes.getEndTimeIsMorrow() ? DateUtils.offsetDay(classDate, 1) : DateUtils.newLocalDateTime(classDate);
            attClockInRecords.setRequireClockInTime(DateUtils.parse(DateUtils.formatDate(endTime) + " " + classes.getEndWorkTime()));
            attClockInRecords.setClockBeforeTime(DateUtils.parse(DateUtils.formatDate(endTime) + " " + classes.getEndClockBeforeTime()));
            attClockInRecords.setClockAfterTime(DateUtils.parse(DateUtils.formatDate(endTime) + " " + classes.getEndClockAfterTime()));
            attClockInRecords.setClockInTime(classes.getEndWorkTime());
        }
        // 如果最早打卡时间大于打卡时间则最早打卡时间为前一天
        if (DateUtils.compare(attClockInRecords.getClockBeforeTime(), attClockInRecords.getRequireClockInTime()) > 0) {
            attClockInRecords.setClockBeforeTime(DateUtils.offsetDay(attClockInRecords.getClockBeforeTime(), -1));
        }
        // 如果最晚打卡时间小于打卡时间则最晚打卡时间为第二天
        if (DateUtils.compare(attClockInRecords.getClockAfterTime(), attClockInRecords.getRequireClockInTime()) < 0) {
            attClockInRecords.setClockAfterTime(DateUtils.offsetDay(attClockInRecords.getClockAfterTime(), 1));
        }
        attClockInRecords.setState(
                DateUtils.compare(attClockInRecords.getClockAfterTime(), LocalDateTime.now()) < 0 ? ClockInEnum.LACK_CHECKED.name()
                                                                                                  : ClockInEnum.NOT_CHECKED.name());
        return attClockInRecords;
    }
}
