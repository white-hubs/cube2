package com.guhecloud.cube.biz.meattrade.server.base.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * 设备类型新增信息
 *
 * @author lzy
 * @date 2021/9/13
 */
@Data
public class DeviceTypeCreateParam {

    /**
     * 类型名称
     */
    @NotEmpty(message = "类型名称不能为空")
    @Size(max = 20, message = "类型名称不能超过20")
    private String name;

    /**
     * 父类主键
     */
    private Long parentId;

    /**
     * 类别
     */
    private String type;
}
