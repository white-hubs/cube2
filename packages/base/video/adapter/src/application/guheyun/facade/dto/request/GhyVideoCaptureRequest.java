package com.guhecloud.cube.srv.gen.adapter.video.server.application.guheyun.facade.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/21 14:49
 * @Description:
 */

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GhyVideoCaptureRequest extends AbstractGhyRequest{

    /**
     * 设备id
     */
    private String deviceId;

    /**
     * 开始时间
     * Iso8601兼容时间戳,不填则立即开始
     */
    private String startTime;

    /**
     * 抓拍时长
     * 抓拍时长，单位秒。 <=0视同默认最小抓拍时长；大于最大支持值，视同最大支持的录制时长
     */
    private Integer duration;

    @Override
    public String getUrl() {
        RequestUrl url = getUrl("/api/stream/aggregator/video-capture")
                .append("?deviceId={deviceId}");
        if (duration != null){
            url.append("&duration={duration}");
        }
        if (startTime != null){
            url.append("&startTime={startTime}");
        }
        return url.toString();
    }
}
