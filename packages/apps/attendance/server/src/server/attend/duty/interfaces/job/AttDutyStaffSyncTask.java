package com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.interfaces.job;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.common.AttConstants;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.duty.application.commandgateway.AttDutyStaffCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.repository.AttGroupRepository;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.ServiceException;
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

/**
 * 同步上月值班人员
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttDutyStaffSyncTask {

    private final AttGroupRepository attGroupRepository;
    private final AttDutyStaffCommandService attDutyStaffCommandService;

    /**
     * 同步上月值班人员
     */
    @JobAnnotation(name = "attDutyStaffSync", cron = "0 0 0 20 * ?", desc = "考勤-每月同步值班人员")
    public void execute(String param) {
        SessionContextHolder.clearSessionContext();
        var tenantList = attGroupRepository.getAttTenantAll();
        if (tenantList == null || tenantList.isEmpty()) {
            return;
        }
        String yearMonth;
        if (StringUtils.isBlank(param)) {
            yearMonth = DateUtils.format(DateUtils.offsetMonth(LocalDateTime.now(), AttConstants.oneInt), "yyyy-MM");
        } else {
            try {
                DateUtils.parseDate(param + "-01");
                yearMonth = param;
            } catch (Exception e) {
                throw new ServiceException("日期格式为yyyy-MM");
            }
        }
        var finalYearMonth = yearMonth;
        tenantList.forEach(tenant -> {
            SessionContextHolder.requestTenant(tenant);
            SecurityContextHolder.setPrincipal(Principal.builder().id(1L).name("xxl").account("1").type(Principal.Type.CLIENT).build());
            // 同步值班人员
            attDutyStaffCommandService.dutyStaffSync(finalYearMonth, false);

        });
    }
}
