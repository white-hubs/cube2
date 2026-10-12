package com.guhecloud.cube.biz.vmonitor.server.param;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 设备型号导入参数
 *
 * @author lanchun
 * @date 2022/11/8 10:22
 */
@Data
public class CameraDeviceModelExportParam {

    /**
     * 设备型号集合
     */
    @Valid
    @NotNull(message = "设备型号集合不能为空")
    @Size(min = 1, message = "设备型号集合不能小于1")
    private List<CameraDeviceModelModifyParam> cameraDeviceModelModifyParams;
}
