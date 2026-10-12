package com.guhecloud.cube.biz.dispatch.server.job;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.dispatch.server.entity.DispatchGroup;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchGroupService;
import com.guhecloud.cube.biz.dispatch.server.service.DispatchService;
import com.guhecloud.cube.biz.dispatch.server.service.impl.PTDispatchServiceImpl;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Component("DispatchDeviceGroupClearJob")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DispatchDeviceGroupClearJob {
    private final DispatchService dispatchService;
    private final DispatchGroupService dispatchDeviceGroupService;
    private static Boolean runJobFlag = true;

    /**
     * Scheduled 将在线程池中获取线程，为了避免在其他线程中设置了tenant;
     * 必须在在调用相关的接口前，重置tenant或清空tenant
     * 每日凌晨2点 重置有效期 到期的组被清除
     * 备注：普天调度台将在每日凌晨2点进行临时组的清理工作；这里给普天保持一致
     * 这个定时任务目前是普天调度台专用的
     */
    @JobAnnotation(name = "dispatchDeviceGroupClear", cron = "0 0 2 * * ?", desc = "指挥调度-调度设备组定时清理")
    public void dispatchDeviceGroupClear(String param) {
        if (dispatchService instanceof PTDispatchServiceImpl) {
            if (!runJobFlag) {
                return;//对job进行开关
            }
            var start = System.currentTimeMillis();
            log.info("调度设备组清理 开始：{}", Thread.currentThread().getName());
            SessionContextHolder.requestTenant(null);
            //查询临时设备组
            var groupList = dispatchDeviceGroupService.list(Wrappers.<DispatchGroup>lambdaQuery()
                                                                    .eq(DispatchGroup::getType, 1));//查询临时组
            Optional.ofNullable(groupList).orElse(Collections.emptyList())
                    .forEach(g -> {
                        if (g.getValidTime() != null && g.getValidTime() > 0) {
                            SessionContextHolder.requestTenant(g.getTenant());
                            if (g.getValidTime() - 1 <= 0) {
                                //删除该临时组   //第三方平台将会删除自己的临时组 这里无需删除
                                dispatchDeviceGroupService.delete(g.getId());
                            } else {
                                //更新有效期
                                g.setValidTime(g.getValidTime() - 1);
                                dispatchDeviceGroupService.updateById(g);
                            }
                        }
                    });
            log.info("调度设备组清理 结束，耗时：{} 毫秒", System.currentTimeMillis() - start);
        }
    }
}
