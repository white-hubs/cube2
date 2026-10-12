package com.guhecloud.cube.biz.vmonitor.server.vo;

import lombok.Data;

/**
 * 监控设备类型
 *
 * @author lanchun
 * @date 2022/11/8 13:42
 */
@Data
public class CameraDeviceGenreVO {

    /**
     * 设备类型主键
     */
    private Long deviceGenreId;

    /**
     * 设备类型名称
     */
    private String deviceGenreName;
}
