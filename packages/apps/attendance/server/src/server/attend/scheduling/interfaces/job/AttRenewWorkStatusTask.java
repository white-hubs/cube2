package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.flow.client.FlowResult;
import com.guhecloud.cube.biz.ouaa.client.TerminalClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.param.RenewWorkStatusBatchParam;
import com.guhecloud.cube.biz.ouaa.client.param.UserQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttAskLeaveFromMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttAwayBusinessFromMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.mapper.AttLeavePostFromMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttAskLeaveFromPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttAwayBusinessFromPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.approval.infrastructure.po.AttLeavePostFromPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.valueobject.AttGroupRangesEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupStaffMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupStaffPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.querygateway.AttSchedulingQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingRuleMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingRulePO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 检测更新人员工作状态
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttRenewWorkStatusTask {

    private final AttGroupMapper attGroupMapper;
    private final AttGroupStaffMapper attGroupStaffMapper;
    private final UserClient userClient;
    private final AttSchedulingQueryService attSchedulingQueryService;
    private final AttSchedulingRuleMapper attSchedulingRuleMapper;
    private final AttClassesMapper attClassesMapper;
    private final TerminalClient terminalClient;

    private final AttAskLeaveFromMapper attAskLeaveFromMapper;
    private final AttLeavePostFromMapper attLeavePostFromMapper;
    private final AttAwayBusinessFromMapper attAwayBusinessFromMapper;

    @JobAnnotation(name = "attRenewWorkStatusTask", cron = "0 */10 * * * ?", desc = "考勤-检测/更新租户人员工作状态")
    public void execute(String param) {
        var nowTime = LocalDateTime.now();
        if (StringUtils.isNotBlank(param)) {
            try {
                nowTime = DateUtils.parse(param);
            } catch (Exception e) {
                throw new ServiceException("时间格式应该为yyyy-MM-dd HH:mm:ss");
            }
        }
        // 获取全部租户
        SessionContextHolder.clearSessionContext();
        var tenantGroupPOList = attGroupMapper.selectList(Wrappers.<AttGroupPO>lambdaQuery()
                                                                  .groupBy(AttGroupPO::getTenant)
                                                         );
        // 遍历租户
        for (AttGroupPO tenantTemp : tenantGroupPOList) {
            var tenant = tenantTemp.getTenant();
            SessionContextHolder.requestTenant(tenant);
            // 获取考勤人员
            List<AttGroupStaffPO> staffList = new ArrayList<>();
            var groupStaffPOList = attGroupStaffMapper.selectList(Wrappers.emptyWrapper());
            if (groupStaffPOList != null) {
                staffList = groupStaffPOList;
            } else {
                var allStaffGroupList = attGroupMapper.selectList(Wrappers.<AttGroupPO>lambdaQuery()
                                                                          .eq(AttGroupPO::getRanges, AttGroupRangesEnum.ALL.name())
                                                                 );
                if (allStaffGroupList != null && !allStaffGroupList.isEmpty()) {
                    var allStaffGroup = allStaffGroupList.getFirst();
                    var userQueryParam = new UserQueryParam();
                    userQueryParam.setEnabled(true);
                    userQueryParam.setPageNumber(1);
                    userQueryParam.setPageSize(999);
                    var pagination = userClient.listUserByTenant(tenant, userQueryParam);
                    if (pagination != null && pagination.getRecords() != null) {
                        staffList = pagination.getRecords().stream().map(v ->
                                                                                 AttGroupStaffPO.builder().staffId(v.getId())
                                                                                         .groupId(allStaffGroup.getId())
                                                                                         .schedulingRuleId(allStaffGroup.getSchedulingRuleId())
                                                                                         .clockRuleId(allStaffGroup.getClockRuleId())
                                                                                         .build()
                                                                        ).collect(Collectors.toList());
                    }
                }
            }
            if (staffList.isEmpty()) {
                return;
            }

            Map<Long, AttGroupPO> groupPOMap = new HashMap<>();
            var groupPOList = attGroupMapper.selectList(Wrappers.emptyWrapper());
            if (groupPOList != null) {
                groupPOMap = groupPOList.stream().collect(Collectors.toMap(AttGroupPO::getId, v -> v));
            }
            Map<Long, AttSchedulingRulePO> rulePOMap = new HashMap<>();
            var schedulingRulePOList = attSchedulingRuleMapper.selectList(Wrappers.emptyWrapper());
            if (schedulingRulePOList != null) {
                rulePOMap = schedulingRulePOList.stream().collect(Collectors.toMap(AttSchedulingRulePO::getId, v -> v));
            }
            Map<Long, AttClassesPO> classesPOMap = new HashMap<>();
            var classesPOList = attClassesMapper.selectList(Wrappers.emptyWrapper());
            if (classesPOList != null) {
                classesPOMap = classesPOList.stream().collect(Collectors.toMap(AttClassesPO::getId, v -> v));
            }

            List<Long> leaveStaffList = new ArrayList<>();
            List<Long> awayStaffList = new ArrayList<>();
            List<Long> postStaffList = new ArrayList<>();
            List<Long> workStaffList = new ArrayList<>();
            List<Long> restStaffList = new ArrayList<>();
            // 当前时间的请假单
            var leaveList = attAskLeaveFromMapper.selectList(Wrappers.<AttAskLeaveFromPO>lambdaQuery()
                                                                     .eq(AttAskLeaveFromPO::getStatus, FlowResult.FINISHED.name())
                                                                     .ge(AttAskLeaveFromPO::getLeaveEndTime, nowTime)
                                                                     .le(AttAskLeaveFromPO::getLeaveStartTime, nowTime)
                                                            );

            // 当前时间的离岗单
            var postList = attLeavePostFromMapper.selectList(Wrappers.<AttLeavePostFromPO>lambdaQuery()
                                                                     .eq(AttLeavePostFromPO::getStatus, FlowResult.FINISHED.name())
                                                                     .ge(AttLeavePostFromPO::getPostEndTime, nowTime)
                                                                     .le(AttLeavePostFromPO::getPostStartTime, nowTime)
                                                            );

            // 获取当前时间的出差单
            var awayList = attAwayBusinessFromMapper.selectList(Wrappers.<AttAwayBusinessFromPO>lambdaQuery()
                                                                        .eq(AttAwayBusinessFromPO::getStatus, FlowResult.FINISHED.name())
                                                                        .ge(AttAwayBusinessFromPO::getAwayEndTime, nowTime)
                                                                        .le(AttAwayBusinessFromPO::getAwayStartTime, nowTime)
                                                               );

            // 遍历人员
            for (AttGroupStaffPO groupStaffPO : staffList) {
                var staffId = groupStaffPO.getStaffId();

                /* 请假 */
                var leaveBool = false;
                ListIterator<AttAskLeaveFromPO> leaveIterator = null;
                if (leaveList != null && !leaveList.isEmpty()) {
                    leaveIterator = leaveList.listIterator();
                    while (leaveIterator.hasNext()) {
                        var leavePO = leaveIterator.next();
                        if (staffId.equals(leavePO.getApplicantId())) {
                            leaveStaffList.add(staffId);
                            leaveBool = true;
                            leaveIterator.remove();
                            break;
                        }
                    }
                }
                if (leaveBool) {
                    continue;
                }

                /* 出差 */
                var awayBool = false;
                ListIterator<AttAwayBusinessFromPO> awayIterator = null;
                if (awayList != null && !awayList.isEmpty()) {
                    awayIterator = awayList.listIterator();
                    while (awayIterator.hasNext()) {
                        var awayPO = awayIterator.next();
                        if (staffId.equals(awayPO.getApplicantId())) {
                            awayStaffList.add(staffId);
                            awayBool = true;
                            awayIterator.remove();
                            break;
                        }
                    }
                }

                if (awayBool) {
                    continue;
                }

                /* 离岗 */
                boolean postBool = false;
                ListIterator<AttLeavePostFromPO> postIterator = null;
                if (postList != null && !postList.isEmpty()) {
                    postIterator = postList.listIterator();
                    while (postIterator.hasNext()) {
                        var postPO = postIterator.next();
                        if (staffId.equals(postPO.getApplicantId())) {
                            postStaffList.add(staffId);
                            postBool = true;
                            postIterator.remove();
                            break;
                        }
                    }
                }

                if (postBool) {
                    continue;
                }

                // 查看是否是上班
                var schedulingPO = attSchedulingQueryService.getStaffTimeSchedulingPO(staffId, nowTime, groupPOMap.get(groupStaffPO.getGroupId()), rulePOMap.get(groupStaffPO.getSchedulingRuleId()), classesPOMap);
                if (schedulingPO != null) {
                    workStaffList.add(staffId);
                    continue;
                }

                // 否则更新为休息
                restStaffList.add(staffId);
            }

            var batchParam = new RenewWorkStatusBatchParam();
            batchParam.setTenant(tenant);
            batchParam.setLeaveStaffList(leaveStaffList);
            batchParam.setAwayStaffList(awayStaffList);
            batchParam.setPostStaffList(postStaffList);
            batchParam.setWorkStaffList(workStaffList);
            batchParam.setRestStaffList(restStaffList);
            batchParam.setResetAllStatus(true);
            terminalClient.batchUpdateTenantUserWorkStatus(batchParam);
        }
    }
}
