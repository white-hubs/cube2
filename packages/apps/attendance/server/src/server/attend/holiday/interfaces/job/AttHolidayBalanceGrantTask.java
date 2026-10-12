package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.job;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.repository.AttGroupRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.application.commandgateway.AttHolidayBalanceCommandService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * 余额发放定时任务
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttHolidayBalanceGrantTask {

    private final AttHolidayBalanceCommandService attHolidayBalanceCommandService;
    private final AttGroupRepository attGroupRepository;

    /**
     * 假期余额发放
     *
     * @param param 入参
     *
     * @return 结果
     */
    @JobAnnotation(name = "attHolidayBalanceGrant", cron = "0 0 0 * * ?", desc = "考勤-假期余额发放")
    public void execute(String param) {
        var nowTime = LocalDateTime.now();
        // 获取所有租户
        SessionContextHolder.clearSessionContext();
        var tenantList = attGroupRepository.getAttTenantAll();
        if (tenantList == null || tenantList.isEmpty()) {
            return;
        }
        tenantList.forEach(tenant -> {
            SessionContextHolder.requestTenant(tenant);
            SecurityContextHolder.setPrincipal(Principal.builder().id(1L).name("system").account("1").type(Principal.Type.CLIENT).build());
            try {
                attHolidayBalanceCommandService.grantBalance(nowTime);
            } catch (Exception e) {
                log.error("假期余额发放失败{}：", tenant, e);
            }
        });
    }
}
