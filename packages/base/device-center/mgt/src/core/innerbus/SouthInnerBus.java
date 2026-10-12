package com.guhecloud.cube.iot.mgt.core.innerbus;

import com.guhecloud.cube.iot.mgt.core.data.msg.device.DeviceInfoSyncMsg;
import com.guhecloud.cube.iot.mgt.core.data.msg.device.DeviceRemove;
import com.guhecloud.cube.iot.mgt.core.data.msg.device.MockDevEventReport;
import com.guhecloud.cube.iot.mgt.core.data.msg.device.MockPropReport;
import com.guhecloud.cube.iot.mgt.core.data.msg.device.PreDeviceMethodInvokeMsg;
import com.guhecloud.cube.iot.mgt.core.data.msg.device.PreDevicePropSetMsg;
import com.guhecloud.cube.iot.mgt.core.data.msg.device.PreDeviceResetMsg;

import java.util.List;

/**
 * 定义南向总线
 */
public interface SouthInnerBus {

    /**
     * 属性命令下发
     *
     * @param propSetMsg 属性设置消息
     */
    void process(PreDevicePropSetMsg propSetMsg);

    /**
     * 设备复位命令下发
     *
     * @param resetMsg 设备复位消息
     */
    void process(PreDeviceResetMsg resetMsg);

    /**
     * 设备信息同步命令下发
     *
     * @param deviceInfoSyncMsg 设备信息同步消息
     */
    void process(DeviceInfoSyncMsg deviceInfoSyncMsg);

    /**
     * 模拟设备属性上报<br/>
     *
     * @param mockPropReport 模拟设备属性上报消息
     */
    void process(MockPropReport mockPropReport);

    /**
     * 模拟设备事件上报
     *
     * @param mockDevEventReport 模拟设备事件上报消息
     */
    void process(MockDevEventReport mockDevEventReport);

    /**
     * 设备方法调用
     *
     * @param methodInvokeMsg 方法调用消息
     */
    void process(PreDeviceMethodInvokeMsg methodInvokeMsg);

    /**
     * 批量设备删除通知南向(iot-runtime)
     *
     * @param deviceRemoves 设备删除消息列表
     */
    void deviceRemove(List<DeviceRemove> deviceRemoves);
}
