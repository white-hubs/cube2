package com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.rocketmq.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author lanchun
 * @date 2022/11/1 13:55
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LinkageDeviceCtlMessage {

    public static final String TOPIC = "LINKAGE_DEVICE_CTL_NOTIFY";

    /**
     * 业务主键
     */
    private String businessKey;

    /**
     * 设备信息
     */
    private List<LinkageDeviceMessage> devices;
}
