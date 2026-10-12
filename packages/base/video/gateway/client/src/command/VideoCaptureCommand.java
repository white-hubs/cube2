package com.guhecloud.cube.base.videogw.client.command;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 视频录像参数
 *
 * @author lanchun
 * @date 2023/11/27 15:36
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VideoCaptureCommand extends VideoFileStoreCommand {

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
