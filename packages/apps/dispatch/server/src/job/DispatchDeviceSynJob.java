package com.guhecloud.cube.biz.dispatch.server.job;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchConfigService;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchDeviceService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 设备名称同步任务
 *
 * @author lanchun
 * @date 2023/3/27 11:29
 */
@Slf4j
@Component("DispatchDeviceSynJob")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DispatchDeviceSynJob {
    private final DispatchDeviceService dispatchDeviceService;
    private final DispatchConfigService dispatchConfigService;
    private static Boolean runJobFlag = true;

    /**
     * Scheduled 将在线程池中获取线程，为了避免在其他线程中设置了tenant;
     * 必须在在调用相关的接口前，重置tenant或清空tenant
     * 每隔30秒钟 刷新设备状态
     */
    @JobAnnotation(name = "updateDispatchDeviceStatus", cron = "0 * * * * ?", desc = "指挥调度-定时同步三方设备")
    public void updateDeviceStatus(String param) {
        if (!runJobFlag) {
            return;//对job进行开关
        }
        var start = System.currentTimeMillis();
        log.info("更新调度设备开始：{}", Thread.currentThread().getName());
        SessionContextHolder.requestTenant(null);
        var dispatchConfigs = dispatchConfigService.list();
        dispatchConfigs.forEach(dispatchConfig -> {
            SessionContextHolder.requestTenant(dispatchConfig.getTenant());
            var jsonObject = JsonUtils.deserialize(dispatchConfig.getExtendConfig(), new TypeReference<Map<String, Object>>() {});
            var rootNode = jsonObject.get("rootNode").toString();
            this.dispatchDeviceService.obtainThirdPlatformDevice(rootNode);
        });
        log.info("更新调度设备结束，耗时：{} 毫秒", System.currentTimeMillis() - start);
    }
}
