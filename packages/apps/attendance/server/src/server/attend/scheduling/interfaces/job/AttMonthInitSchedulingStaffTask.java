package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.job;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.repository.AttGroupRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.commandgateway.AttSchedulingStaffCommandService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttMonthInitSchedulingStaffTask {

    private final AttGroupRepository attGroupRepository;
    private final AttSchedulingStaffCommandService attSchedulingStaffCommandService;

    /**
     * 每月初始化考勤人员
     *
     * @param param 月份（yyyy-MM)
     *
     * @return 是否成功
     */
    @JobAnnotation(name = "attMonthInitSchedulingStaff", cron = "0 2 0 1 * ?", desc = "考勤-每月初始化考勤人员")
    public void execute(String param) {
        if (StringUtils.isBlank(param)) {
            param = DateUtils.format(LocalDateTime.now(), "yyyy-MM");
        }
        // 获取全部租户
        SessionContextHolder.clearSessionContext();
        var tenantList = attGroupRepository.getAttTenantAll();
        for (Integer tenant : tenantList) {
            SessionContextHolder.requestTenant(tenant);
            SecurityContextHolder.setPrincipal(Principal.builder().id(1L).name("system").account("system").type(Principal.Type.CLIENT)
                                                       .build());
            attSchedulingStaffCommandService.initMonthSchedulingStaff(param);
        }
    }
}
