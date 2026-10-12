package com.guhecloud.cube.biz.traffic.detection.server.detection.domain.aggregates;

import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.valueobj.TrafficDetectionAreaDevice;
import com.guhecloud.cube.biz.traffic.detection.server.detection.domain.valueobj.TrafficDetectionAreaVisual;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrafficDetectionArea {

    /**
     * ID
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 区域模型
     */
    private String modelArea;

    /**
     * 备注
     */
    private String remarks;

    /**
     * 视角配置
     */
    private TrafficDetectionAreaVisual viewConfig;

    /**
     * 设备列表
     */
    private List<TrafficDetectionAreaDevice> devices;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 创建流量区域
     */
    public static TrafficDetectionArea create(String name, String modelArea, String remarks
                                             ) {
        return TrafficDetectionArea.builder()
                .name(name)
                .modelArea(modelArea)
                .remarks(remarks)
                .updateTime(LocalDateTime.now())
                .build();
    }

    public void changeDevices(List<TrafficDetectionAreaDevice> devices) {
        if (CollectionUtils.isEmpty(devices) || CollectionUtils.isEmpty(this.devices)) {
            this.devices = devices;
            return;
        }
        for (var newDevice : devices) {
            var trafficDetectionAreaDevice = this.devices.stream()
                    .filter(device -> device.getDeviceId().equals(newDevice.getDeviceId())).findFirst();
            trafficDetectionAreaDevice.ifPresent(
                    device -> newDevice.setDeviceConfig(device.getDeviceConfig()));
        }
        this.devices = devices;
    }
}
