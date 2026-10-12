package com.guhecloud.cube.biz.door.server.event.handler;

import cn.hutool.core.thread.ThreadUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.door.server.entity.DoorDevice;
import com.guhecloud.cube.biz.door.server.event.vo.DoorDeviceUpdateEvent;
import com.guhecloud.cube.biz.door.server.service.DoorDeviceService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 门禁设备修改事件处理
 * 目前只修改了名称，如果要修改其他字段请自行添加
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DoorDeviceUpdateEventHandler {

    private final DoorDeviceService doorDeviceService;

    @EventListener
    public void doorDeviceUpdateEvent(DoorDeviceUpdateEvent event) {
        List<DoorDevice> doorDeviceList = event.getDoorDeviceList();
        if (CollectionUtils.isNotEmpty(doorDeviceList)) {
            Principal principal = SecurityContextHolder.getPrincipal();
            Integer tenant = SessionContextHolder.getRequester().getTenant();
            // 异步执行
            ThreadUtil.execAsync(() -> {
                SecurityContextHolder.setPrincipal(principal);
                SessionContextHolder.requestTenant(tenant);
                for (DoorDevice doorDevice : doorDeviceList) {
                    LambdaUpdateWrapper<DoorDevice> wrapper = Wrappers.<DoorDevice>lambdaUpdate()
                            .eq(DoorDevice::getId, doorDevice.getId());
                    boolean isUpdate = false;
                    if (doorDevice.getName() != null) {
                        wrapper.set(DoorDevice::getName, doorDevice.getName());
                        isUpdate = true;
                    }
                    if (doorDevice.getDoorArea() != null) {
                        wrapper.set(DoorDevice::getDoorArea, doorDevice.getDoorArea());
                        isUpdate = true;
                    }
                    if (doorDevice.getDoorCode() != null) {
                        wrapper.set(DoorDevice::getDoorCode, doorDevice.getDoorCode());
                        isUpdate = true;
                    }
                    if (doorDevice.getDoorModel() != null) {
                        wrapper.set(DoorDevice::getDoorModel, doorDevice.getDoorModel());
                        isUpdate = true;
                    }
                    if (isUpdate) {
                        doorDeviceService.update(wrapper);
                    }
                }
            });
        }
    }
}
