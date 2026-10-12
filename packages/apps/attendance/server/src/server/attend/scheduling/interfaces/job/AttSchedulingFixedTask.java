package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.mapper.AttGroupMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.foundation.infrastructure.po.AttGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.application.commandgateway.AttSchedulingCommandService;
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

/**
 *
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttSchedulingFixedTask {

    private final AttSchedulingCommandService attSchedulingCommandService;
    private final AttGroupMapper attGroupMapper;

    /**
     * 将已经发生的排班固定
     *
     * @param param 时间(2023-09-11 18:28:39)
     *
     * @return 执行状态
     */
    @JobAnnotation(name = "attFixedNowSchedulingTask", cron = "0 3 0 * * ?", desc = "考勤-考勤固定已发生的排班")
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
        for (AttGroupPO groupPO : tenantGroupPOList) {
            SessionContextHolder.requestTenant(groupPO.getTenant());
            attSchedulingCommandService.fixedNowScheduling(nowTime, "1");
        }
    }
}
