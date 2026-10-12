package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.job;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionProject;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionProjectStatusEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.DisinfectionProjectService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import com.guhecloud.platform.redis.util.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DisinfectionProjectJob {
    private final DisinfectionProjectService disinfectionProjectService;
    private final static String DISINFECTION_PROJECT_REDIS_LOCK = "disinfection:project:";

    @JobAnnotation(name = "updateProjectStatus", cron = "0/30 * * * * ?", desc = "病媒生物-定时更新项目状态")
    public void updateProjectStatus(String param) {
        log.info("病媒生物-定时更新项目状态开始执行");
        //查询消杀项目关联的租户
        var tenants = disinfectionProjectService.getAllTenants();
        tenants.forEach(tenant -> {
            log.info("租户【{}】消杀项目开始更新", tenant);
            SessionContextHolder.requestTenant(tenant);
            String token = null;
            try {
                token = RedisUtils.tryLock(DISINFECTION_PROJECT_REDIS_LOCK + tenant.toString(), Expiration.seconds(10));
                if (StringUtils.isBlank(token)) {
                    return;
                }
                //查询未开始和进行中的项目
                var projects = disinfectionProjectService.lambdaQuery()
                        .in(DisinfectionProject::getStatus, DisinfectionProjectStatusEnum.WAITING, DisinfectionProjectStatusEnum.PROCESSING)
                        .list();

                //获取更新任务状态
                var waitUpdateProjects = projects.stream().filter(project -> {
                    var now = DateUtils.now();
                    var beginTime = project.getBeginTime();
                    var endTime = project.getEndTime();
                    var status = project.getStatus();
                    if (DateUtils.compare(now, beginTime) > 0 && DateUtils.compare(now, endTime) < 0) {
                        project.setStatus(DisinfectionProjectStatusEnum.PROCESSING);
                    } else if (DateUtils.compare(now, beginTime) <= 0) {
                        project.setStatus(DisinfectionProjectStatusEnum.WAITING);

                    } else {
                        project.setStatus(DisinfectionProjectStatusEnum.COMPLETED);
                    }
                    if (status.equals(project.getStatus())) {
                        return false;
                    }
                    log.info("项目【{}】，状态由【{}】，变更为【{}】", project.getProjectName(), status, project.getStatus());
                    return true;
                }).toList();
                disinfectionProjectService.updateBatchById(waitUpdateProjects);
            } catch (Exception e) {
                log.error("租户消杀项目信息更新失败", e);
            } finally {
                if (StringUtils.isNotBlank(token)) {
                    RedisUtils.unlock(DISINFECTION_PROJECT_REDIS_LOCK + tenant, token);
                }
            }

        });

        log.info("病媒生物-定时更新项目状态执行完毕");

    }
}
