package com.guhecloud.cube.customization.xialing.server.drone.job;

import com.guhecloud.cube.customization.xialing.server.drone.service.DroneService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * 霞凝无人机同步定时任务
 * <p>
 * 定期执行全量同步，将所有租户的无人机数据从霞凝平台同步到本地数据库。
 * 同步频率为每30分钟执行一次（cron: 40 0/30 * * * ?）。
 * </p>
 *
 * @author system
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DroneSyncJob {
    private final DroneService droneService;
    private final SubsystemRedisUtils subsystemRedisUtils;

    /**
     * 全量同步所有租户的无人机数据
     * <p>
     * 执行流程：
     * <ol>
     *   <li>清除会话和安全上下文，避免使用用户身份</li>
     *   <li>获取所有配置了无人机子系统的租户</li>
     *   <li>遍历每个租户，设置租户上下文和系统用户身份</li>
     *   <li>调用服务层的全量同步方法</li>
     *   <li>捕获异常并记录日志，确保一个租户失败不影响其他租户</li>
     *   <li>清理会话上下文</li>
     * </ol>
     * </p>
     */
    @JobAnnotation(name = "droneSync", cron = "40 0/30 * * * ?", desc = "霞凝无人机全量同步")
    public void fullSync(String param) {
        // 清除会话和安全上下文，避免使用用户身份执行定时任务
        SessionContextHolder.clearSessionContext();
        SecurityContextHolder.clearSecurityContext();
        // 获取所有配置了无人机子系统的租户
        var endpointConfigs = subsystemRedisUtils.getSubEndpointRefreshConfigs(SubsystemTypeConstants.DRONE_SERVER);
        endpointConfigs.forEach(config -> {
            try {
                // 设置租户上下文
                SessionContextHolder.requestTenant(config.getTenant());
                // 设置系统用户身份，用于审计日志
                SecurityContextHolder.setPrincipal(Principal.builder().id(1L).account("system").name("system").type(Principal.Type.CLIENT)
                                                           .build());
                // 执行全量同步
                droneService.fullSyncCurrentTenant();
            } catch (Exception e) {
                log.error("霞凝无人机全量同步失败 tenant={}", config.getTenant(), e);
            } finally {
                // 清理会话上下文，避免影响下一个租户
                SessionContextHolder.clearSessionContext();
            }
        });
    }
}
