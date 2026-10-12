package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration.ClockInAuthorityEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration.ClockInEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.enumeration.IdentityVerifyEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttClockInRecordsRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttStaffClockRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttClockInRecordsMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttClockInRecordsPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.ClockInLogDateParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.ClockInLogPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.ClockInLogResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesRecordPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.dto.AttClockSchedulingClassesDTO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-22  15:20
 * @Description:
 * @Version: 1.0
 */
@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttClockInRecordsRepositoryImpl extends ServiceImpl<AttClockInRecordsMapper, AttClockInRecordsPO> implements AttClockInRecordsRepository {
    private final AttStaffClockRepository attStaffClockRepository;
    private final RedissonClient redissonClient;

    @Override
    public Pagination<ClockInLogResult> getClockInLog(ClockInLogPageParam param) {
        Page<AttClockInRecordsPO> page = new Page<>(param.getPageNumber(), param.getPageSize());
        page = this.baseMapper.selectPage(page, new LambdaQueryWrapper<AttClockInRecordsPO>()
                .isNotNull(AttClockInRecordsPO::getActualClockInTime)
                .in(AttClockInRecordsPO::getClockType, ClockInEnum.clockType)
                .eq(ObjectUtils.isNotEmpty(param.getOrgId()), AttClockInRecordsPO::getOrgId, param.getOrgId())
                .eq(ObjectUtils.isNotEmpty(param.getWays()), AttClockInRecordsPO::getWays, param.getWays())
                .between(AttClockInRecordsPO::getActualClockInTime, param.getPeriod().getBegin(), param.getPeriod().getEnd())
                .like(ObjectUtils.isNotEmpty(param.getUserName()), AttClockInRecordsPO::getUserName, param.getUserName())
                .orderByDesc(AttClockInRecordsPO::getActualClockInTime));
        return PagingUtils.convert(page, ObjectUtils.copying(page.getRecords(), ClockInLogResult.class));
    }

    @Override
    public List<AttClockInRecordsPO> getClockInLogByDate(ClockInLogDateParam param) {
        return this.baseMapper.selectList(new LambdaQueryWrapper<AttClockInRecordsPO>()
                                                  .eq(AttClockInRecordsPO::getClockInDate, param.getClockInDate())
                                                  .eq(AttClockInRecordsPO::getUserId, param.getUserId())
                                                  .in(AttClockInRecordsPO::getClockType, ClockInEnum.clockType)
                                                  .in(param.getAbnormal(), AttClockInRecordsPO::getState, ClockInEnum.clockAbnormalState)
                                                  .orderByAsc(AttClockInRecordsPO::getRequireClockInTime));
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AttClockInRecordsPO> getClockInLog(Long userId, LocalDateTime startDate, LocalDateTime endDate) {
        return this.baseMapper.selectList(new LambdaQueryWrapper<AttClockInRecordsPO>()
                                                  .between(AttClockInRecordsPO::getActualClockInTime, startDate, endDate)
                                                  .eq(AttClockInRecordsPO::getUserId, userId)
                                                  .orderByAsc(AttClockInRecordsPO::getActualClockInTime));
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public List<AttClockInRecordsPO> getClockInInfo(UserDTO user, AttClassesRecordPO classes, String uuid,
                                                    AttClockSchedulingClassesDTO scheduling
                                                   ) {
        List<AttClockInRecordsPO> clock = null;
        if (ObjectUtils.isNotEmpty(uuid)) {
            clock = this.baseMapper.selectList(new LambdaQueryWrapper<AttClockInRecordsPO>()
                                                       .eq(AttClockInRecordsPO::getUuid, uuid)
                                                       .in(AttClockInRecordsPO::getClockType, ClockInEnum.clockType)
                                                       .orderByDesc(AttClockInRecordsPO::getClockType));
        } else {
            clock = addClock(user, classes, scheduling.getClassDate(), scheduling.getId());
        }
        Assert.parameterIsNull(clock, "打卡信息不存在");
        return clock;
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public AttClockInRecordsPO getClockInInfo(String uuid, LocalDateTime actualClockInTime) {
        List<AttClockInRecordsPO> clock = this.baseMapper.selectList(new LambdaQueryWrapper<AttClockInRecordsPO>()
                                                                             .eq(AttClockInRecordsPO::getUuid, uuid)
                                                                             .in(AttClockInRecordsPO::getClockType, ClockInEnum.clockType));
        Assert.parameterIsNull(clock, "打卡信息不存在");
        AttClockInRecordsPO clockInRecords = null;
        for (AttClockInRecordsPO ex : clock) {
            boolean bool = DateUtils.isIn(actualClockInTime, ex.getClockBeforeTime(), ex.getClockAfterTime());
            if (ClockInEnum.START.name().equals(ex.getClockType()) && bool) {
                if (!ClockInEnum.NORMAL.name().equals(ex.getState())) {
                    if (ObjectUtils.isNotEmpty(ex.getActualClockInTime())) {
                        if (DateUtils.compare(actualClockInTime, ex.getActualClockInTime()) < 0) {
                            clockInRecords = ex;
                            break;
                        }
                    } else {
                        clockInRecords = ex;
                        break;
                    }
                }
            } else if (ClockInEnum.END.name().equals(ex.getClockType())) {
                clockInRecords = ex;
            }
        }
        Assert.parameterIsNull(clockInRecords, "未找到打卡信息");
        return clockInRecords;
    }

    /**
     * @description: 上班打卡
     * @author: lijie
     * @date: 2023/9/21 17:58
     * @param:
     * @return: 打卡结果
     */
    @Override
    public AttClockInRecordsPO clockWork(AttClockInRecordsPO clockIn) {
        clockIn.setState(DateUtils.compare(clockIn.getActualClockInTime(), DateUtils.offsetMinute(clockIn.getRequireClockInTime(), 1)) < 0
                         ? ClockInEnum.NORMAL.name() : ClockInEnum.LATE_CHECKED.name());
        if (ClockInEnum.LATE_CHECKED.name().equals(clockIn.getState())) {
            String requireClockInTime = DateUtils.format(clockIn.getRequireClockInTime());
            requireClockInTime = requireClockInTime.substring(0, requireClockInTime.length() - 2) + "00";
            Long minute = DateUtils.between(DateUtils.parse(requireClockInTime), clockIn.getActualClockInTime(), ChronoUnit.MINUTES);
            clockIn.setDeletionDuration(minute);
        }
        this.baseMapper.updateById(clockIn);
        // 更新部门、班次快照
        this.update().set("org_id", clockIn.getOrgId())
                .set("classes_version", clockIn.getClassesVersion())
                .eq("uuid", clockIn.getUuid());
        return clockIn;
    }

    /**
     * @description: 下班打卡
     * @author: lijie
     * @date: 2023/9/21 17:58
     * @param:
     * @return: 打卡结果
     */
    @Override
    public AttClockInRecordsPO afterAlockWork(AttClockInRecordsPO clockIn) {
        String state =
                DateUtils.compare(clockIn.getRequireClockInTime(), clockIn.getActualClockInTime()) > 0 ? ClockInEnum.EARLY_CHECKED.name()
                                                                                                       : ClockInEnum.NORMAL.name();
        if (ClockInEnum.EARLY_CHECKED.name().equals(state)) {
            String requireClockInTime = DateUtils.format(clockIn.getRequireClockInTime());
            requireClockInTime = requireClockInTime.substring(0, requireClockInTime.length() - 2) + "59";
            long minute = DateUtils.between(clockIn.getActualClockInTime(), DateUtils.parse(requireClockInTime), ChronoUnit.MINUTES, true);
            clockIn.setDeletionDuration(minute);
        }
        if (ClockInEnum.NOT_CHECKED.name().equals(clockIn.getState())) {
            clockIn.setState(state);
            this.baseMapper.updateById(clockIn);
        } else {
            clockIn.setState(state);
            clockIn.setClockType(ClockInEnum.END.name());
            // 更新之前的打卡记录为无效
            this.update()
                    .set("clock_type", ClockInEnum.REVISE.name())
                    .eq("uuid", clockIn.getUuid())
                    .eq("clock_type", ClockInEnum.END.name()).update();
            // 新增打卡记录
            clockIn.setId(null);
            this.baseMapper.insert(clockIn);
        }
        return clockIn;
    }

    @Override
    public Boolean addClockLog(AttClockInRecordsPO attClockInRecordsPO) {
        attClockInRecordsPO.setId(null);
        if (ObjectUtils.isEmpty(attClockInRecordsPO.getClockBeforeTime())) {
            attClockInRecordsPO.setClockBeforeTime(LocalDateTime.now());
        }
        if (ObjectUtils.isEmpty(attClockInRecordsPO.getClockAfterTime())) {
            attClockInRecordsPO.setClockAfterTime(LocalDateTime.now());
        }
        return this.baseMapper.insert(attClockInRecordsPO) > 0;
    }

    @Override
    public List<AttClockInRecordsPO> addClock(UserDTO user, AttClassesRecordPO classes, LocalDateTime classDate, Long schedulingId) {
        List<AttClockInRecordsPO> attClockInRecordsPOS = this.baseMapper.selectList(new LambdaQueryWrapper<AttClockInRecordsPO>()
                                                                                            .eq(AttClockInRecordsPO::getUserId, user.getId())
                                                                                            .eq(AttClockInRecordsPO::getClassesId, classes.getClassesId())
                                                                                            .eq(AttClockInRecordsPO::getClockInDate, DateUtils.formatDate(classDate))
                                                                                            .in(AttClockInRecordsPO::getClockType, ClockInEnum.clockType)
                                                                                            .orderByDesc(AttClockInRecordsPO::getClockType));
        if (CollectionUtils.isNotEmpty(attClockInRecordsPOS)) {
            return attClockInRecordsPOS;
        }
        RLock fairLock = redissonClient.getFairLock("lock:insertAttClockInRecords:user:" + user.getId().toString());
        boolean res = false;
        try {
            //尝试60秒内获取锁
            res = fairLock.tryLock(60L, 60L, TimeUnit.SECONDS);
            if (res) {
                //处理业务
                attClockInRecordsPOS = attStaffClockRepository.insertAttClockInRecords(user, classes, classDate, schedulingId);
            } else {
                throw new Exception("get lock fail");
            }
        } catch (Exception e) {
            log.error("获取锁失败：" + e);
        } finally {
            //解锁
            if (res) {
                fairLock.unlock();
            }
        }
        return attClockInRecordsPOS;
    }

    @Override
    public AttClockInRecordsPO updateClockIn(Long id) {
        AttClockInRecordsPO clockInRecords = new AttClockInRecordsPO();
        AttClockInRecordsPO records = this.baseMapper.selectById(id);
        Assert.parameterIsNull(records, "打卡记录不存在");
        String name = ClockInAuthorityEnum.getNext(records.getClockInAuthority());
        records.setClockInAuthority(name);
        // 撤销
        if (ObjectUtils.isEmpty(name) || ClockInAuthorityEnum.REVOKE_CLOCK.name().equals(name)) {
            records.setActualClockInTime(null);
            records.setState(ClockInEnum.LACK_CHECKED.name());
            this.updateById(records);
            return records;
        }

        // 作废
        if (ClockInAuthorityEnum.DISCARD_CLOCK.name().equals(name)) {
            records.setPastDataType(records.getClockType());
            if (ClockInEnum.REVISE.name().equals(records.getClockType())) {
                this.updateById(records);
                return null;
            }
            this.update()
                    .set("past_data_type", ClockInEnum.REVISE.name())
                    .eq("uuid", records.getUuid())
                    .eq("clock_type", ClockInEnum.REVISE.name())
                    .update();
            clockInRecords = records.convertAttClockInRecordsPO();
            clockInRecords.setState(
                    DateUtils.compare(records.getClockAfterTime(), LocalDateTime.now()) < 0 ? ClockInEnum.LACK_CHECKED.name()
                                                                                            : ClockInEnum.NOT_CHECKED.name());
            this.baseMapper.insert(clockInRecords);
            records.setClockType(ClockInEnum.REVISE.name());
            this.baseMapper.updateById(records);
            return clockInRecords;
        }
        // 取消作废
        if (ClockInAuthorityEnum.MY_CLOCK.name().equals(name)) {
            if (ObjectUtils.isEmpty(records.getPastDataType())) {
                records.setPastDataType(null);
                this.updateById(records);
                return null;
            }
            if (ClockInEnum.REVISE.name().equals(records.getPastDataType())) {
                records.setPastDataType(ClockInEnum.REVISE.name());
                this.updateById(records);
                return null;
            }
            AttClockInRecordsPO inRecordsPO = this.baseMapper.selectOne(new LambdaQueryWrapper<AttClockInRecordsPO>()
                                                                                .eq(AttClockInRecordsPO::getUuid, records.getUuid())
                                                                                .eq(AttClockInRecordsPO::getClockType, records.getPastDataType())
                                                                                .last("limit 1"));
            if (ObjectUtils.isNotEmpty(inRecordsPO) && ObjectUtils.isEmpty(inRecordsPO.getActualClockInTime())) {
                inRecordsPO.setDeleted(true);
                this.updateById(inRecordsPO);
                // 修改
                records.setClockType(records.getPastDataType());
                records.setPastDataType(null);
                this.updateById(records);
                return records;
            } else {
                records.setPastDataType(ClockInEnum.REVISE.name());
                this.updateById(records);
                return null;
            }
        }
        return null;
    }

    @Override
    public AttClockInRecordsPO updateClockIn(String classesId) {
        AttClockInRecordsPO clock = this.baseMapper.selectById(classesId);
        clock.setState(ClockInEnum.NORMAL.name());
        clock.setActualClockInTime(clock.getRequireClockInTime());
        clock.setDeletionDuration(0L);
        clock.setWorkOmit(true);
        this.baseMapper.updateById(clock);
        return clock;
    }

    @Override
    public Boolean updateClockInRecords(Long userId) {
        // 缺卡数据
        List<AttClockInRecordsPO> clock = this.baseMapper.selectList(new LambdaQueryWrapper<AttClockInRecordsPO>()
                                                                             .lt(AttClockInRecordsPO::getClockAfterTime, LocalDateTime.now())
                                                                             .eq(AttClockInRecordsPO::getState, ClockInEnum.NOT_CHECKED.name())
                                                                             .eq(ObjectUtils.isNotEmpty(userId), AttClockInRecordsPO::getUserId, userId)
                                                                             .last("limit 1"));
        if (ObjectUtils.isNotEmpty(clock)) {
            // 先检查是否有需要修复的缺卡数据
            try {
                clock.stream().forEach(ex -> {
                    List<AttClockInRecordsPO> clockLog = this.baseMapper.selectList(new LambdaQueryWrapper<AttClockInRecordsPO>()
                                                                                            .eq(AttClockInRecordsPO::getUserId, ex.getUserId())
                                                                                            .eq(AttClockInRecordsPO::getClockType, ClockInEnum.LOG.name())
                                                                                            .ge(AttClockInRecordsPO::getActualClockInTime, ex.getClockBeforeTime())
                                                                                            .le(AttClockInRecordsPO::getActualClockInTime, ex.getClockAfterTime()));
                    if (ObjectUtils.isNotEmpty(clockLog)) {
                        AttClockInRecordsPO clockInLog =
                                ClockInEnum.START.name().equals(ex.getClockType()) ? clockLog.get(0) : clockLog.get(clockLog.size() - 1);
                        ex.getAttClockInRecordsPO(clockInLog);
                        Boolean bool = this.baseMapper.updateById(ex) > 0;
                        if (bool) {
                            clockInLog.setDeleted(true);
                            // 修正后的打卡记录需要逻辑删除
                            this.baseMapper.updateById(clockInLog);
                        }
                    }
                });
            } catch (Exception e) {
                log.error("att:日志修复缺卡数据失败", e);
            }
            return this.update().set("state", ClockInEnum.LACK_CHECKED.name())
                    .eq(ObjectUtils.isNotEmpty(userId), "user_id", userId)
                    .eq("state", ClockInEnum.NOT_CHECKED.name())
                    .lt("clock_after_time", LocalDateTime.now()).update();
        }
        return true;
    }

    @Override
    public Boolean updateClockInRecords(Long id, String clockType) {
        return this.update().set("clock_type", clockType)
                .eq("id", id).update();
    }

    @Override
    public Boolean updateClockInByClasses(AttClassesRecordPO classes) {
        // 上班时间
        LocalDateTime startWorkTime = DateUtils.parse(DateUtils.formatDate(LocalDateTime.now()) + " " + classes.getStartWorkTime());
        // 上班打卡开始时间
        LocalDateTime startClockBeforeTime = DateUtils.parse(
                DateUtils.formatDate(LocalDateTime.now()) + " " + classes.getStartClockBeforeTime());
        // 上班打卡结束时间
        LocalDateTime startClockAfterTime = DateUtils.parse(
                DateUtils.formatDate(LocalDateTime.now()) + " " + classes.getStartClockAfterTime());

        List<AttClockInRecordsPO> clock = this.baseMapper.selectList(new LambdaQueryWrapper<AttClockInRecordsPO>()
                                                                             .eq(AttClockInRecordsPO::getClassesId, classes.getClassesId())
                                                                             .eq(AttClockInRecordsPO::getClockInDate, LocalDateTime.now())
                                                                             .ge(AttClockInRecordsPO::getActualClockInTime, LocalDateTime.now())
                                                                             .eq(AttClockInRecordsPO::getClockType, ClockInEnum.START.name()));
        for (AttClockInRecordsPO ex : clock) {
            if (DateUtils.isIn(ex.getActualClockInTime(), startClockBeforeTime, startClockAfterTime)) {
                ex.setRequireClockInTime(startWorkTime);
                ex.setClassesVersion(classes.getVersion());
                ex.setClockAfterTime(startClockAfterTime);
                ex.setState(DateUtils.compare(startWorkTime, ex.getActualClockInTime()) < 0 ? ClockInEnum.LATE_CHECKED.name()
                                                                                            : ClockInEnum.NORMAL.name());
                AttClockInRecordsPO clockInRecords = this.baseMapper.selectOne(new LambdaQueryWrapper<AttClockInRecordsPO>()
                                                                                       .eq(AttClockInRecordsPO::getUuid, ex.getUuid())
                                                                                       .eq(AttClockInRecordsPO::getClockType, ClockInEnum.END.name())
                                                                                       .last("limit 1"));
                this.baseMapper.updateById(ex);
                // 下班是第二天需要偏移一天
                LocalDateTime endTime = classes.getEndTimeIsMorrow() ? DateUtils.offsetDay(LocalDateTime.now(), 1) : LocalDateTime.now();
                clockInRecords.setRequireClockInTime(DateUtils.parse(DateUtils.formatDate(endTime) + " " + classes.getEndWorkTime()));
                clockInRecords.setClockAfterTime(DateUtils.parse(DateUtils.formatDate(endTime) + " " + classes.getEndClockAfterTime()));
                clockInRecords.setClockInTime(classes.getEndWorkTime());
                if (DateUtils.compare(clockInRecords.getClockAfterTime(), clockInRecords.getActualClockInTime()) < 0) {
                    clockInRecords.setClockAfterTime(DateUtils.offsetDay(clockInRecords.getClockAfterTime(), 1));
                }
                this.baseMapper.updateById(clockInRecords);
            } else {
                this.update().set("clock_type", ClockInEnum.LOG.name()).eq("uuid", ex.getUuid()).update();
            }
        }
        return true;
    }

    @Override
    public void revalidation(AttClockInRecordsPO clock) {
        if (IdentityVerifyEnum.SUCCESS.getRemarks().equals(clock.getFaceRecognitionResult())) {
            this.updateById(clock);
        } else {
            this.update().set("clock_type", ClockInEnum.REVISE.name())
                    .set("face_recognition_result", clock.getFaceRecognitionResult())
                    .eq("id", clock.getId())
                    .update();
            this.baseMapper.insert(this.getAttClockInRecordsPO(clock));

        }
    }

    /**
     * @description: 根据打卡记录计算加班时长
     * @author: lijie
     * @date: 2024/1/2 9:56
     * @param: period 加班时间
     * @return: 加班时长
     **/
    @Override
    public Period getAttClockInByDate(Period period) {
        /**
         * 1、找到最后一次上班记录
         * 2、如果时间在该班次直接取上班时间-下班时间
         * 3、如果超过下班打卡时间，则查找最早的打卡记录
         **/
        Long userId = SecurityContextHolder.getPrincipal().getId();
        AttClockInRecordsPO clockStart = this.baseMapper.getClockInInByUserIdAndTime(userId, period.getBegin());
        if (ObjectUtils.isNotEmpty(clockStart)) {
            AttClockInRecordsPO clockEnd = this.baseMapper.selectOne(new LambdaQueryWrapper<AttClockInRecordsPO>()
                                                                             .eq(AttClockInRecordsPO::getUuid, clockStart.getUuid())
                                                                             .eq(AttClockInRecordsPO::getClockType, ClockInEnum.END.name())
                                                                             .last("limit 1"));
            // 加班开始时间小于下班最晚打卡时间
            if (DateUtils.compare(period.getBegin(), clockEnd.getClockAfterTime()) < 1) {
                // 下班未打卡无法提交申请
                Assert.parameterIsNull(clockEnd.getActualClockInTime(), "未找到打卡记录，无法提交加班申请");
                // 下班打卡时间早于开始加班时间，无法提交申请
                Assert.parameterIsNull(
                        DateUtils.compare(period.getBegin(), clockEnd.getActualClockInTime()) > 0, "未找到打卡记录，无法提交加班申请");
                // 上下班直接的申请直接使用申请时间
                // 上班缺卡则直接取提交的开始加班时间
                if (DateUtils.compare(period.getEnd(), clockEnd.getActualClockInTime()) < 1) {
                    return period;
                } else {
                    return new Period(period.getBegin(), clockEnd.getActualClockInTime());
                }
            }
        }
        // 开始打卡日志
        clockStart = this.baseMapper.selectOne(new LambdaQueryWrapper<AttClockInRecordsPO>()
                                                       .le(AttClockInRecordsPO::getActualClockInTime, period.getBegin())
                                                       .eq(AttClockInRecordsPO::getUserId, userId)
                                                       .orderByDesc(AttClockInRecordsPO::getActualClockInTime)
                                                       .last("limit 1"));
        if (ObjectUtils.isEmpty(clockStart)) {
            clockStart = this.baseMapper.selectOne(new LambdaQueryWrapper<AttClockInRecordsPO>()
                                                           .lt(AttClockInRecordsPO::getActualClockInTime, period.getBegin())
                                                           .eq(AttClockInRecordsPO::getUserId, userId)
                                                           .orderByAsc(AttClockInRecordsPO::getActualClockInTime)
                                                           .last("limit 1"));
        }
        // 没有打卡日志，无法提交申请
        Assert.parameterIsNull(clockStart, "未找到打卡记录，无法提交加班申请");
        // 下班打卡时间不能作为加班开始时间
        Assert.isNotFound(!ClockInEnum.LOG.name().equals(clockStart.getClockType()), "未找到打卡记录，无法提交加班申请");
        AttClockInRecordsPO clockEnd = this.baseMapper.selectOne(new LambdaQueryWrapper<AttClockInRecordsPO>()
                                                                         .lt(AttClockInRecordsPO::getActualClockInTime, period.getBegin())
                                                                         .eq(AttClockInRecordsPO::getUserId, userId)
                                                                         .orderByAsc(AttClockInRecordsPO::getActualClockInTime)
                                                                         .last("limit 1"));
        if (ObjectUtils.isEmpty(clockEnd)) {
            clockEnd = this.baseMapper.selectOne(new LambdaQueryWrapper<AttClockInRecordsPO>()
                                                         .le(AttClockInRecordsPO::getActualClockInTime, period.getBegin())
                                                         .eq(AttClockInRecordsPO::getUserId, userId)
                                                         .orderByDesc(AttClockInRecordsPO::getActualClockInTime)
                                                         .last("limit 1"));
        }
        // 没有打卡日志，无法提交申请
        Assert.parameterIsNull(clockEnd, "未找到打卡记录，无法提交加班申请");
        // 下班打卡时间不能作为加班开始时间
        Assert.isNotFound(!ClockInEnum.LOG.name().equals(clockEnd.getClockType()), "未找到打卡记录，无法提交加班申请");
        Assert.isNotFound(clockStart.getId().equals(clockEnd.getId()), "未找到打卡记录，无法提交加班申请");
        LocalDateTime begin =
                DateUtils.compare(period.getBegin(), clockStart.getActualClockInTime()) < 1 ? clockStart.getActualClockInTime()
                                                                                            : period.getBegin();
        LocalDateTime end =
                DateUtils.compare(period.getEnd(), clockEnd.getActualClockInTime()) < 1 ? period.getEnd() : clockEnd.getActualClockInTime();
        Assert.isNotFound(DateUtils.compare(end, begin) <= 0, "未找到打卡记录，无法提交加班申请");
        return new Period(DateUtils.compare(period.getBegin(), clockStart.getActualClockInTime()) < 1 ? clockStart.getActualClockInTime()
                                                                                                      : period.getBegin(),
                          DateUtils.compare(period.getEnd(), clockEnd.getActualClockInTime()) < 1 ? period.getEnd()
                                                                                                  : clockEnd.getActualClockInTime());
    }

    /**
     * 获得一个新的打卡记录
     */
    public AttClockInRecordsPO getAttClockInRecordsPO(AttClockInRecordsPO clock) {
        AttClockInRecordsPO clockInRecords = ObjectUtils.copying(clock, AttClockInRecordsPO.class);
        clockInRecords.setId(null);
        clockInRecords.setState(ClockInEnum.NOT_CHECKED.name());
        clockInRecords.setOutClock(false);
        clockInRecords.setWays(null);
        clockInRecords.setActualClockInTime(null);
        clockInRecords.setDeletionDuration(0L);
        clockInRecords.setName(null);
        clockInRecords.setPhotoFileId(null);
        clockInRecords.setPhotoFileUrl(null);
        clockInRecords.setSyncTime(null);
        clockInRecords.setFaceRecognitionResult(null);
        clockInRecords.setPastDataType(null);
        clockInRecords.setOutClock(false);
        clockInRecords.setWorkOmit(false);
        return clockInRecords;
    }

    /**
     * 打卡记录修正考勤结果
     */
    private void getAttClockInRecordsPO(AttClockInRecordsPO clock, AttClockInRecordsPO clockLog) {
        clock.setActualClockInTime(clockLog.getActualClockInTime());
        clock.setWays(clockLog.getWays());
        clock.setClockInAuthority(ClockInEnum.MY_CLOCK.name());
        clock.setSyncTime(null);
    }
}
