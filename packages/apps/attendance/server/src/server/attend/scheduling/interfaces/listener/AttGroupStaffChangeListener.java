package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.listener;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.application.commandgateway.AttGroupStaffCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.event.AttGroupStaffChangeEvent;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.application.commandgateway.AttHolidayBalanceCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.commandgateway.AttSchedulingCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.commandgateway.AttSchedulingStaffCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.mapper.AttSchedulingMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.infrastructure.po.AttSchedulingPO;
import com.guhecloud.platform.base.context.ExecutorContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttGroupStaffChangeListener {

    private final AttSchedulingCommandService attSchedulingCommandService;
    private final AttSchedulingStaffCommandService attSchedulingStaffCommandService;
    private final AttHolidayBalanceCommandService attHolidayBalanceCommandService;
    private final AttGroupStaffCommandService attGroupStaffCommandService;
    private final AttSchedulingMapper attSchedulingMapper;

    /**
     * 监听考勤组人员变更事件
     * 1.添加排班人员表数据
     * 2.添加人员假期余额信息
     */
    @EventListener
    public void groupStaffChangeListener(final AttGroupStaffChangeEvent event) {
        Principal principal = SecurityContextHolder.getPrincipal();
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        String by = principal == null ? "1" : String.valueOf(principal.getId());
        // 异步执行
        ExecutorContextHolder.run(() -> {
            SecurityContextHolder.setPrincipal(principal);
            SessionContextHolder.requestTenant(tenant);
            if (event.getGroupPO() == null) {
                return;
            }
            try {
                /* 删除手动排班 */
                if (event.getRemoveStaffIdList() != null && !event.getRemoveStaffIdList().isEmpty()) {
                    attSchedulingMapper.remove(DateUtils.toTimestamp(LocalDateTime.now()), Wrappers.<AttSchedulingPO>lambdaQuery()
                                                       .eq(AttSchedulingPO::getStaffGroupId, event.getGroupPO().getId())
                                                       .in(AttSchedulingPO::getStaffId, event.getRemoveStaffIdList())
                                                       .gt(AttSchedulingPO::getSchedulingDate, LocalDateTime.now())
                                              );
                }
                /* 更新考勤组人员 */
                attGroupStaffCommandService.renewGroupStaff(event.getGroupPO(), event.getAddStaffParamList(), event.getRemoveStaffIdList());
                /* 更新考勤排班人员信息 */
                attSchedulingStaffCommandService.schedulingStaffAdd(event.getGroupPO(), event.getYearMonth(), event.getAddStaffParamList());
                /* 固定考勤组排班 */
                attSchedulingCommandService.fixedNowGroupScheduling(null, event.getGroupPO(), LocalDateTime.now(), by);
                /* 更新考勤组人员假期余额 */
                attHolidayBalanceCommandService.staffAddBalance(event.getGroupPO(), event.getAllStaffParamList(), LocalDateTime.now());

            } catch (Exception e) {
                log.error("考勤组变更后续操作失败：", e);
            }

        });

    }
}
