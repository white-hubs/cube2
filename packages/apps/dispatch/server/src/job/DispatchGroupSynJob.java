package com.guhecloud.cube.biz.dispatch.server.job;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchConfigService;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchGroupService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 调度组同步
 *
 * @author appet
 * @date 2023/9/20 11:29
 */
@Slf4j
@Component("DispatchGroupSynJob")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DispatchGroupSynJob {
    private final DispatchGroupService dispatchGroupService;
    private final DispatchConfigService dispatchConfigService;
    private static Boolean runJobFlag = true;

    /**
     * Scheduled 将在线程池中获取线程，为了避免在其他线程中设置了tenant;
     * 必须在在调用相关的接口前，重置tenant或清空tenant
     * 每隔30秒钟 刷新设备状态
     */
    @JobAnnotation(name = "syncDispatchGroup", cron = "30 * * * * ?", desc = "指挥调度-调度组定时同步")
    public void updateDeviceStatus(String param) {
        if (!runJobFlag) {
            return;//对job进行开关
        }
        var start = System.currentTimeMillis();
        log.info("同步调度组开始：{}", Thread.currentThread().getName());
        SessionContextHolder.requestTenant(null);
        var dispatchConfigs = dispatchConfigService.list();
        dispatchConfigs.forEach(dispatchConfig -> {
            SessionContextHolder.requestTenant(dispatchConfig.getTenant());
            var jsonObject = JsonUtils.deserialize(dispatchConfig.getExtendConfig(), new TypeReference<Map<String, Object>>() {});
            var rootNode = jsonObject.get("rootNode").toString();
            this.dispatchGroupService.obtainThirdPlatformDevice(rootNode);
        });
        log.info("同步调度组结束，耗时：{} 毫秒", System.currentTimeMillis() - start);

    }
}
