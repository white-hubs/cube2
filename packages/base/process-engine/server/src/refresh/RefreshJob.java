package com.guhecloud.cube.base.flow.refresh;

import com.guhecloud.cube.base.flow.refresh.commandgateway.RefreshService;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/7/12 9:33
 * @Description: 刷新表单job
 */

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RefreshJob {

    private final RefreshService refreshService;
    private final TenantClient tenantClient;

    /**
     * 定时任务：自动刷新表单数据
     *
     * @param param 流程相关参数
     *
     * @return 返回一个ReturnT类型的对象，表示任务执行结果
     */
    public void refreshFlowDataJob(String param) {
        SessionContextHolder.clearSessionContext();
        var tenantDTOS = tenantClient.listTenants("");
        for (var tenantDTO : tenantDTOS) {
            try {
                SessionContextHolder.requestTenant(tenantDTO.getCode());
                refreshService.refreshProcdef();
            } catch (Exception e) {
                log.error("刷新表单数据失败", e);
                SessionContextHolder.clearSessionContext();
            }
        }
        SessionContextHolder.clearSessionContext();
        log.info("刷新表单数据结束");
    }
}
