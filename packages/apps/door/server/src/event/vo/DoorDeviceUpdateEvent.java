package com.guhecloud.cube.biz.door.server.event.vo;

import com.guhecloud.cube.biz.door.server.entity.DoorDevice;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.List;

/**
 * 设备修改事件(目前这个收到这个事件只修改了名称，如果要修改其他的字段 自己在接收事件中添加)
 */
@Getter
public class DoorDeviceUpdateEvent extends ApplicationEvent {

    private final List<DoorDevice> doorDeviceList;

    public DoorDeviceUpdateEvent(Object source, List<DoorDevice> doorDeviceList) {
        super(source);
        this.doorDeviceList = doorDeviceList;
    }
}
