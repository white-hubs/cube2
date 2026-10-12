package com.guhecloud.cube.biz.vmonitor.server.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 设备型号导入参数
 *
 * @author lanchun
 * @date 2022/11/8 10:22
 */
@Data
public class CameraDeviceModelModifyParam {

    /**
     * 设备型号主键
     */
    @NotNull(message = "设备型号主键不能为空")
    private Long deviceModelId;

    /**
     * 设备型号名称
     */
    @NotEmpty(message = "设备型号名称不能为空")
    @Size(max = 100, message = "设备型号名称不能大于100")
    private String deviceModelName;

    /**
     * 设备类型主键
     */
    @NotNull(message = "设备类型主键不能为空")
    private Long deviceGenreId;

    /**
     * 设备类型名称
     */
    @NotEmpty(message = "设备类型名称不能为空")
    @Size(max = 100, message = "设备类型名称不能大于100")
    private String deviceGenreName;

    /**
     * 设备品牌主键
     */
    @NotNull(message = "设备品牌主键不能为空")
    private Long deviceBrandId;

    /**
     * 设备品牌名称
     */
    @NotEmpty(message = "设备品牌名称不能为空")
    @Size(max = 100, message = "设备品牌名称不能大于100")
    private String deviceBrandName;
}
