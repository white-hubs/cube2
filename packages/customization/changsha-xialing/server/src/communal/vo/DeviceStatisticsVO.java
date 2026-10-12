package com.guhecloud.cube.customization.xialing.server.communal.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceStatisticsVO {
    private long totalCount;
    private long onlineCount;
    private long offlineCount;

    public static DeviceStatisticsVO empty() {
        return new DeviceStatisticsVO(0L, 0L, 0L);
    }

    public void add(long total, long online) {
        this.totalCount += total;
        this.onlineCount += online;
        this.offlineCount += total - online;
    }
}
