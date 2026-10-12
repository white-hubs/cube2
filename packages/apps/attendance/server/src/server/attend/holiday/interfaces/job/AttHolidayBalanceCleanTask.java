package com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.interfaces.job;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.domain.repository.AttGroupRepository;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.application.commandgateway.AttHolidayBalanceCommandService;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.holiday.application.message.AttHolidayBalanceExpireNoticeService;
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

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttHolidayBalanceCleanTask {

    private final AttGroupRepository attGroupRepository;
    private final AttHolidayBalanceCommandService attHolidayBalanceCommandService;
    private final AttHolidayBalanceExpireNoticeService attHolidayBalanceExpireNoticeService;

    /**
     * 假期余额过期清除
     *
     * @param param 入参
     *
     * @return 是否成功
     */
    @JobAnnotation(name = "attCleanExpireBalance", cron = "0 0 0 * * ?", desc = "考勤-清除过期余额")
    public void execute(String param) {
        var nowTime = LocalDateTime.now();
        if (StringUtils.isNotBlank(param)) {
            try {
                nowTime = DateUtils.parse(param);
            } catch (Exception e) {
                throw new ServiceException("日期格式错误（yyyy-MM-dd）");
            }
        }
        SessionContextHolder.clearSessionContext();
        var tenantList = attGroupRepository.getAttTenantAll();
        if (tenantList == null || tenantList.isEmpty()) {
            return;
        }
        var finalNowTime = nowTime;
        tenantList.forEach(tenant -> {
            SessionContextHolder.requestTenant(tenant);
            SecurityContextHolder.setPrincipal(Principal.builder().id(1L).name("system").account("system").type(Principal.Type.CLIENT).build());
            try {
                attHolidayBalanceExpireNoticeService.sendExpireNotice(finalNowTime);
            } catch (Exception e) {
                log.error("假期余额到期提醒发送失败{}：", tenant, e);
            }
            try {
                attHolidayBalanceCommandService.cleanExpireBalance(finalNowTime);
            } catch (Exception e) {
                log.error("定时清理假期余额失败{}：", tenant, e);
            }
        });
    }
}
