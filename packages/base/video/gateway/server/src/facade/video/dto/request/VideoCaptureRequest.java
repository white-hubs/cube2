package com.guhecloud.cube.base.videogw.server.facade.video.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 视频录像参数
 *
 * @author lanchun
 * @date 2023/11/27 15:36
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VideoCaptureRequest {

    /**
     * 录像开始时间(iso-8601), 缺省为服务器接收到请求的时间.
     */
    private LocalDateTime startTime;

    /**
     * 录像结束时间. 如果存在录像时长, 可忽略.
     */
    private LocalDateTime endTime;

    /**
     * 录像时长(单位:s) 要求录像时长. 如果存在录像结束时间, 可忽略.
     */
    private Integer length;
}
