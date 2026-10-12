package com.guhecloud.cube.biz.emergency.server.param;

import lombok.Data;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 预案类型保存参数
 *
 * @author lanchun
 * @date 2022/9/2 10:36
 */
@Data
public class EmergencyTypeModifyParam {

    /**
     * 编号
     */
    @Size(max = 100, message = "编号不能大于100")
    private String code;

    /**
     * 名称
     */
    @NotEmpty(message = "名称不能为空")
    @Size(max = 100, message = "名称不能大于100")
    private String name;

    /**
     * 应用编号
     */
    @NotEmpty(message = "应用编号不能为空")
    @Size(max = 100, message = "应用编号不能大于100")
    private String applicationCode;

    /**
     * 应用名称
     */
    @NotEmpty(message = "应用名称不能为空")
    @Size(max = 100, message = "应用名称不能大于100")
    private String applicationName;

    /**
     * 预案类型关联设备类型列表
     */
    @NotNull(message = "关联设备类型不能为空")
    @Size(min = 1, message = "关联设备类型不能小于1")
    @Valid
    private List<EmergencyDeviceGenreModifyParam> emergencyDeviceGenreModifyParams;
}
