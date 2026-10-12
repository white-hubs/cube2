package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.domain.repository.AttApproveLogRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.mapper.AttApproveLogMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.infrastructure.po.AttApproveLogPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.AttClockInDetailsParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.ClassesStatusTriggerEnum;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-01  09:57
 * @Description:
 * @Version: 1.0
 */
@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttApproveLogRepositoryImpl extends ServiceImpl<AttApproveLogMapper, AttApproveLogPO> implements AttApproveLogRepository {

    @Override
    public Boolean getApproveLogIsData(String date) {
        AttApproveLogPO log = this.baseMapper.selectOne(new LambdaQueryWrapper<AttApproveLogPO>()
                                                                .eq(AttApproveLogPO::getStatus, "FINISHED")
                                                                .ge(AttApproveLogPO::getEndTime, date + " 00:00:00")
                                                                .le(AttApproveLogPO::getStartTime, date + " 23:59:59")
                                                                .eq(AttApproveLogPO::getUserId, SecurityContextHolder.getPrincipal()
                                                                        .getId())
                                                                .last("limit 1"));
        return ObjectUtils.isNotEmpty(log);
    }

    @Override
    public List<AttApproveLogPO> getApproveLogAllByDate(String date) {
        return this.baseMapper.selectList(new LambdaQueryWrapper<AttApproveLogPO>()
                                                  .eq(AttApproveLogPO::getStatus, "FINISHED")
                                                  .ge(AttApproveLogPO::getEndTime, date + " 00:00:00")
                                                  .le(AttApproveLogPO::getStartTime, date + " 23:59:59")
                                                  .eq(AttApproveLogPO::getUserId, SecurityContextHolder.getPrincipal()
                                                          .getId()));
    }

    @Override
    public Boolean addApproveLog(List<AttApproveLogPO> attApproveLog) {
        AttApproveLogPO log = this.baseMapper.selectOne(new LambdaQueryWrapper<AttApproveLogPO>()
                                                                .eq(AttApproveLogPO::getProcessInstanceId, attApproveLog.get(0)
                                                                        .getProcessInstanceId())
                                                                .last("limit 1"));
        return ObjectUtils.isNotEmpty(log) || super.saveBatch(attApproveLog);
    }

    @Override
    public Boolean updateApproveLog(String status, String processInstanceId) {
        return this.update()
                .set("status", status)
                .set("update_time", LocalDateTime.now())
                .eq("process_instance_id", processInstanceId)
                .update();
    }

    @Override
    public Boolean updateApproveLog(String status, String remarks, String processInstanceId) {
        return this.update()
                .set("status", status)
                .set("remarks", remarks)
                .set("update_time", LocalDateTime.now())
                .eq("process_instance_id", processInstanceId)
                .update();
    }

    @Override
    public Boolean updateApproveLog(AttApproveLogPO log) {
        return this.updateApproveLog(log.getStatus(), log.getProcessInstanceId());
    }

    @Override
    public List<AttApproveLogPO> getApproveLog(String processInstanceId) {
        return super.baseMapper.selectList(new LambdaQueryWrapper<AttApproveLogPO>()
                                                   .eq(AttApproveLogPO::getProcessInstanceId, processInstanceId));
    }

    @Override
    public Boolean getApproveLogByUser(LocalDateTime clockInTime, Long userId) {
        AttApproveLogPO attApproveLog = this.baseMapper.selectOne(new LambdaQueryWrapper<AttApproveLogPO>()
                                                                          .le(AttApproveLogPO::getStartTime, clockInTime)
                                                                          .ge(AttApproveLogPO::getEndTime, clockInTime)
                                                                          .eq(AttApproveLogPO::getUserId, userId)
                                                                          .eq(AttApproveLogPO::getStatus, FlowResult.FINISHED.name())
                                                                          .in(AttApproveLogPO::getBusinessType, ClassesStatusTriggerEnum.LEAVE.name(), ClassesStatusTriggerEnum.LEAVE_THE_POST.name(), ClassesStatusTriggerEnum.AWAY_BUSINESS.name())
                                                                          .last("limit 1"));
        return ObjectUtils.isNotEmpty(attApproveLog);
    }

    @Override
    public List<AttApproveLogPO> getApproveLog(Long userId, LocalDateTime clockInTime) {
        List<AttApproveLogPO> endApproveLogPOList = this.baseMapper.selectList(new LambdaQueryWrapper<AttApproveLogPO>()
                                                                                       .le(AttApproveLogPO::getStartTime, clockInTime)
                                                                                       .ge(AttApproveLogPO::getEndTime, clockInTime)
                                                                                       .eq(AttApproveLogPO::getUserId, userId)
                                                                                       .eq(AttApproveLogPO::getStatus, FlowResult.FINISHED.name())
                                                                                       .in(AttApproveLogPO::getBusinessType, ClassesStatusTriggerEnum.LEAVE.name(), ClassesStatusTriggerEnum.LEAVE_THE_POST.name(), ClassesStatusTriggerEnum.AWAY_BUSINESS.name()));
        return endApproveLogPOList;
    }

    @Override
    public List<AttApproveLogPO> getApproveLogByUserAndTime(AttClockInDetailsParam param) {
        List<AttApproveLogPO> approveLog = this.baseMapper.selectList(new LambdaQueryWrapper<AttApproveLogPO>()
                                                                              .eq(AttApproveLogPO::getStatus, "FINISHED")
                                                                              .eq(AttApproveLogPO::getUserId, param.getUserId())
                                                                              .le(AttApproveLogPO::getStartTime, param.getPeriod()
                                                                                      .getBegin())
                                                                              .ge(AttApproveLogPO::getEndTime, param.getPeriod().getEnd())
                                                                              .eq(AttApproveLogPO::getSceneCode, param.getType()));
        return approveLog;
    }

    /**
     * 是否存在申请,存在返回时间段，不存在返回null
     *
     * @param userId     人员id
     * @param startTime  开始时间(只要有交叉就算存在)
     * @param endTime    结束时间(只要有交叉就算存在)
     * @param sceneCode  场景code(请假时为假期类型id)
     * @param statusList 状态集合(in条件)
     *
     * @return 存在返回时间段，不存在返回null
     */
    @Override
    public Period existApproveLog(Long userId, LocalDateTime startTime, LocalDateTime endTime, String businessType, String sceneCode,
                                  List<String> statusList
                                 ) {
        Period period = null;
        AttApproveLogPO approveLog = this.getOne(Wrappers.<AttApproveLogPO>lambdaQuery()
                                                         .ge(startTime != null, AttApproveLogPO::getEndTime, startTime)
                                                         .le(endTime != null, AttApproveLogPO::getStartTime, endTime)
                                                         .eq(userId != null, AttApproveLogPO::getUserId, userId)
                                                         .eq(StringUtils.isNotBlank(businessType), AttApproveLogPO::getBusinessType, businessType)
                                                         .eq(StringUtils.isNotBlank(sceneCode), AttApproveLogPO::getSceneCode, sceneCode)
                                                         .in(CollectionUtils.isNotEmpty(statusList), AttApproveLogPO::getStatus, statusList)
                                                         .last(" limit 1 ")
                                                );
        if (approveLog != null) {
            period = new Period(approveLog.getStartTime(), approveLog.getEndTime());
        }
        return period;
    }

}
