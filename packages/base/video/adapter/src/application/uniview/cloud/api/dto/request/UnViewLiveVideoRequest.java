package com.guhecloud.cube.srv.gen.adapter.video.server.application.uniview.cloud.api.dto.request;

import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.guhecloud.cube.srv.gen.adapter.video.server.application.uniview.cloud.api.constants.UniViewApiConstant.UNI_VIEW_LIVE_VIDEO_GET;

/**
 * 获取播放地址请求
 *
 * @author lanchun
 * @date 2025/5/29 11:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UnViewLiveVideoRequest extends AbstractUniViewRequest {

    /**
     * 设备序列号
     */
    private String deviceSerial;

    /**
     * 通道号（IPC填0，NVR从1开始）
     */
    private Integer channelNo;

    /**
     * 过期时长，单位：秒；针对HLS/RTMP/FLV设置有效期，相对时间；30秒-7天；非必选，默认为1天
     */
    private Integer expireTime;

    /**
     * 流播放协议，2-HLS、3-RTMP、4-FLV
     */
    private Integer protocol;

    /**
     * 视频清晰度，0-高清（主码流），1-标清（辅码流），2-流畅（第三流）
     */
    private Integer quality;

    /**
     * 协议地址的本地录像/云存储录像回放开始时间, UTC时间戳
     */
    private Long startTime;

    /**
     * 协议地址的本地录像/云存储录像回放结束时间, UTC时间戳
     */
    private Long endTime;

    /**
     * 协议地址的类型，1-直播，2-本地录像回放，3-云存储录像回放（仅HLS），非必选，默认为1
     */
    private Integer type;

    @Override
    public String getRequestUrl() {
        return getRequestUrl(UNI_VIEW_LIVE_VIDEO_GET).build();
    }
}
