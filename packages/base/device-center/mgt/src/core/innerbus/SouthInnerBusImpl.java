package com.guhecloud.cube.iot.mgt.core.innerbus;

import com.guhecloud.cube.iot.mgt.core.data.msg.device.DeviceInfoSyncMsg;
import com.guhecloud.cube.iot.mgt.core.data.msg.device.DeviceRemove;
import com.guhecloud.cube.iot.mgt.core.data.msg.device.MockDevEventReport;
import com.guhecloud.cube.iot.mgt.core.data.msg.device.MockPropReport;
import com.guhecloud.cube.iot.mgt.core.data.msg.device.PreDeviceMethodInvokeMsg;
import com.guhecloud.cube.iot.mgt.core.data.msg.device.PreDevicePropSetMsg;
import com.guhecloud.cube.iot.mgt.core.data.msg.device.PreDeviceResetMsg;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class SouthInnerBusImpl implements SouthInnerBus {

    /**
     * 设备信息变更消息的主题<br/>
     * 用于设备信息变更的内部总线通信<br/>
     */
    private static final String TO_RUNTIME_DEV_INFO_SYNC = "TO_IOT_RUNTIME_DEVICE_INFO_SYNC";

    /**
     * 模拟属性上报消息的主题<br/>
     * 用于模拟设备属性上报的内部总线通信<br/>
     */
    private static final String TO_RUNTIME_DEV_PROP_SET = "TO_IOT_RUNTIME_PRE_DEVICE_PROP_SET";

    /**
     * 设备重置消息的主题<br/>
     * 用于设备重置的内部总线通信<br/>
     */
    private static final String TO_RUNTIME_DEV_RESET = "TO_IOT_RUNTIME_PRE_DEVICE_RESET";

    /**
     * 模拟设备属性上报消息的主题<br/>
     * 用于模拟设备属性上报的内部总线通信<br/>
     */
    private static final String TO_RUNTIME_DEV_PROP_REPORT = "TO_IOT_RUNTIME_DEV_PROP_MOCK_REPORT";

    /**
     * 模拟设备事件上报消息的主题<br/>
     * 用于模拟设备事件上报的内部总线通信<br/>
     */
    private static final String TO_RUNTIME_DEV_EVENT_REPORT = "TO_IOT_RUNTIME_DEV_EVENT_MOCK_REPORT";

    /**
     * 设备方法调用消息的主题<br/>
     * 用于设备方法调用的内部总线通信<br/>
     */
    private static final String TO_RUNTIME_METHOD_INVOKE = "TO_IOT_RUNTIME_METHOD_INVOKE";

    /**
     * 设备删除消息的主题<br/>
     * 用于通知iot-runtime设备已删除<br/>
     */
    private static final String TO_RUNTIME_DEVICE_REMOVE = "TO_IOT_RUNTIME_DEVICE_REMOVE";

    @Override
    public void process(PreDevicePropSetMsg propSetMsg) {
        log.info("Received PreDevicePropSetMsg: {}", propSetMsg);
        RocketMQContextHolder.send(TO_RUNTIME_DEV_PROP_SET, propSetMsg);
    }

    @Override
    public void process(PreDeviceResetMsg resetMsg) {
        log.info("Received PreDeviceResetMsg: {}", resetMsg);

        RocketMQContextHolder.send(TO_RUNTIME_DEV_RESET, resetMsg);
    }

    @Override
    public void process(DeviceInfoSyncMsg deviceInfoSyncMsg) {
        log.info("Received DeviceInfoSyncMsg: {}", deviceInfoSyncMsg);

        RocketMQContextHolder.send(TO_RUNTIME_DEV_INFO_SYNC, deviceInfoSyncMsg);
    }

    @Override
    public void process(MockPropReport mockPropReport) {
        log.info("Received MockPropReport: {}", mockPropReport);
        RocketMQContextHolder.send(TO_RUNTIME_DEV_PROP_REPORT, mockPropReport);
    }

    @Override
    public void process(MockDevEventReport mockDevEventReport) {
        log.info("Received MockDevEventReport: {}", mockDevEventReport);
        RocketMQContextHolder.send(TO_RUNTIME_DEV_EVENT_REPORT, mockDevEventReport);
    }

    @Override
    public void process(PreDeviceMethodInvokeMsg methodInvokeMsg) {
        log.info("Received PreDeviceMethodInvokeMsg: {}", methodInvokeMsg);
        RocketMQContextHolder.send(TO_RUNTIME_METHOD_INVOKE, methodInvokeMsg);
    }

    /**
     * 批量设备删除通知南向(iot-runtime)
     */
    @Override
    public void deviceRemove(List<DeviceRemove> deviceRemoves) {
        log.info("批量设备删除通知, 数量: {}", deviceRemoves.size());
        DeviceOperate deviceOperate = new DeviceOperate();
        deviceOperate.setOperate("del");
        deviceOperate.setDeviceRemoves(deviceRemoves);
        RocketMQContextHolder.send(TO_RUNTIME_DEVICE_REMOVE, deviceOperate);
    }

    @Data
    static class DeviceOperate {
        /**
         * 设备操作类型<br/>
         * del 删除
         * add 新增
         * modify 修改
         */
        private String operate;

        private List<DeviceRemove> deviceRemoves;
    }
}
