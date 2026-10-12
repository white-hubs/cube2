package com.guhecloud.cube.srv.gen.third.connection.server.huawei.attendace.job;

import com.guhecloud.cube.biz.ouaa.client.TerminalClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserTenantDTO;
import com.guhecloud.cube.biz.ouaa.client.param.RenewWorkStatusBatchParam;
import com.guhecloud.cube.biz.ouaa.client.param.WorkStatusQueryParam;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.cube.srv.gen.third.connection.server.huawei.attendace.service.AttendanceService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * 考勤检查定时任务
 * 每隔5秒执行一次，采集HR数据并判断用户是否在岗
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AttendanceCheckJob {

    private final AttendanceService attendanceService;
    private final UserClient userClient;
    private final TerminalClient terminalClient;

    /**
     * 考勤检查任务
     * 每隔5秒执行一次
     */
    @JobAnnotation(name = "attendanceCheck", cron = "0 */5 * * * ?", desc = "考勤检查任务-每5分钟执行一次", param = "39116660")
    public void checkAttendance(String tenantParam) {
        try {
            log.info("开始执行考勤检查任务...");
            if (StringUtils.isBlank(tenantParam)) {
                log.warn("未指定租户，跳过考勤检查任务");
                return;
            }
            SessionContextHolder.requestTenant(Integer.parseInt(tenantParam));
            var checkTime = LocalDateTime.now();

            // 批量检查所有用户是否在岗
            var statusList = attendanceService.batchCheckUsersOnDuty(checkTime);

            if (ListUtils.isEmpty(statusList)) {
                log.warn("未查询到任何用户考勤状态");
                return;
            }

            // 收集所有手机号
            var cellPhones = new HashSet<String>();
            for (var status : statusList) {
                if (status.getCellPhone() != null) {
                    cellPhones.add(status.getCellPhone());
                }
            }

            // 根据手机号批量查询用户信息
            var userMap = new HashMap<String, UserDTO>();
            var userIdSet = new HashSet<Long>();
            if (ListUtils.isNotEmpty(cellPhones)) {
                var userQuery = UserQuery.builder()
                        .mobileList(cellPhones)
                        .build();
                var users = userClient.postList(userQuery);

                // 按手机号建立映射，并收集用户ID
                if (ListUtils.isNotEmpty(users)) {
                    for (var user : users) {
                        if (user.getMobile() != null) {
                            userMap.put(user.getMobile(), user);
                            if (user.getId() != null) {
                                userIdSet.add(user.getId());
                            }
                        }
                    }
                }
            }

            // 根据用户ID批量查询工作状态
            var workStatusMap = new HashMap<Long, UserTenantDTO>();
            if (ListUtils.isNotEmpty(userIdSet)) {
                try {
                    var tenant = SessionContextHolder.getRequester().getTenant();
                    if (tenant != null) {
                        var workStatusQuery = new WorkStatusQueryParam();
                        workStatusQuery.setTenant(tenant);
                        workStatusQuery.setUserIdList(userIdSet);
                        var workStatusList = terminalClient.getTenantUserWorkStatusPost(workStatusQuery);

                        // 按用户ID建立映射
                        if (ListUtils.isNotEmpty(workStatusList)) {
                            for (var workStatus : workStatusList) {
                                if (workStatus.getUserId() != null) {
                                    workStatusMap.put(workStatus.getUserId(), workStatus);
                                }
                            }
                        }
                    } else {
                        log.warn("无法获取租户信息，跳过工作状态查询");
                    }
                } catch (Exception e) {
                    log.error("查询用户工作状态失败", e);
                }
            }

            // 统计在岗和不在岗人数，并收集需要更新状态的用户
            var onDutyCount = 0;
            var offDutyCount = 0;
            var workStaffList = new ArrayList<Long>(); // 需要设置为在岗的用户ID列表
            var restStaffList = new ArrayList<Long>(); // 需要设置为休息的用户ID列表

            for (var status : statusList) {
                var user = userMap.get(status.getCellPhone());
                var workStatus = user != null && user.getId() != null ? workStatusMap.get(user.getId()) : null;

                if (user != null && user.getId() != null) {
                    var workStatusStr = workStatus != null ? workStatus.getWorkStatus() : "未知";
                    var hrOnDuty = Boolean.TRUE.equals(status.getIsOnDuty());
                    var systemWorking = "WORKING".equals(workStatusStr);

                    // 判断是否需要更新工作状态
                    if (hrOnDuty && !systemWorking) {
                        // HR显示在岗，但系统状态不是 WORKING，需要更新为 WORKING
                        workStaffList.add(user.getId());
                        log.info("员工 {} ({}) - 用户ID: {}, 姓名: {}, HR在岗状态: 在岗, 系统工作状态: {}, 需要更新为 WORKING",
                                 status.getEmployeeNo(),
                                 status.getCellPhone(),
                                 user.getId(),
                                 user.getName(),
                                 workStatusStr);
                    } else if (!hrOnDuty && systemWorking) {
                        // HR显示不在岗，但系统状态是 WORKING，需要更新为 REST
                        restStaffList.add(user.getId());
                        log.info("员工 {} ({}) - 用户ID: {}, 姓名: {}, HR在岗状态: 不在岗 - {}, 系统工作状态: WORKING, 需要更新为 REST",
                                 status.getEmployeeNo(),
                                 status.getCellPhone(),
                                 user.getId(),
                                 user.getName(),
                                 status.getStatusDesc());
                    } else {
                        log.debug("员工 {} ({}) - 用户ID: {}, 姓名: {}, HR在岗状态: {}, 系统工作状态: {}, 状态一致无需更新",
                                  status.getEmployeeNo(),
                                  status.getCellPhone(),
                                  user.getId(),
                                  user.getName(),
                                  hrOnDuty ? "在岗" : "不在岗 - " + status.getStatusDesc(),
                                  workStatusStr);
                    }
                } else {
                    log.debug("员工 {} ({}) 未找到对应用户信息，HR在岗状态: {}",
                              status.getEmployeeNo(),
                              status.getCellPhone(),
                              status.getIsOnDuty() ? "在岗" : "不在岗 - " + status.getStatusDesc());
                }

                if (Boolean.TRUE.equals(status.getIsOnDuty())) {
                    onDutyCount++;
                } else {
                    offDutyCount++;
                }
            }

            // 批量更新工作状态
            if (ListUtils.isNotEmpty(workStaffList) || ListUtils.isNotEmpty(restStaffList)) {
                try {
                    var tenant = SessionContextHolder.getRequester().getTenant();
                    if (tenant != null) {
                        var batchParam = new RenewWorkStatusBatchParam();
                        batchParam.setTenant(tenant);
                        if (ListUtils.isNotEmpty(workStaffList)) {
                            batchParam.setWorkStaffList(workStaffList);
                        }
                        if (ListUtils.isNotEmpty(restStaffList)) {
                            batchParam.setRestStaffList(restStaffList);
                        }
                        terminalClient.batchUpdateTenantUserWorkStatus(batchParam);
                        log.info("批量更新工作状态完成，设置为在岗: {} 人，设置为休息: {} 人",
                                 workStaffList.size(), restStaffList.size());
                    } else {
                        log.warn("无法获取租户信息，跳过工作状态更新");
                    }
                } catch (Exception e) {
                    log.error("批量更新工作状态失败", e);
                }
            }

            log.info("考勤检查任务执行完成，检查时间: {}，总人数: {}，在岗: {}，不在岗: {}，找到用户信息: {}，找到工作状态: {}，更新在岗: {}，更新休息: {}",
                     checkTime, statusList.size(), onDutyCount, offDutyCount, userMap.size(), workStatusMap.size(),
                     workStaffList.size(), restStaffList.size());

        } catch (Exception e) {
            log.error("考勤检查任务执行失败", e);
        } finally {
            SessionContextHolder.clearSessionContext();
        }
    }
}
