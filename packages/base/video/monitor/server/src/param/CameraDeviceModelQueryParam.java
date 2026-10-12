package com.guhecloud.cube.biz.vmonitor.server.param;

import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * 摄像机设备型号查询参数
 *
 * @author lanchun
 * @date 2022/11/8 10:08
 */
@Data
public class CameraDeviceModelQueryParam {

    @Size(max = 100, message = "关键字长度不能大于100")
    private String keyword;

    /**
     * 设备类型主键
     */
    private Long deviceGenreId;
}
