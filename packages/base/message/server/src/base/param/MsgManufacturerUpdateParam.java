package com.guhecloud.cube.base.message2.server.base.param;

import lombok.Data;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

/**
 * 消息厂家新增参数
 *
 * @author lanchun
 * @date 2023/3/7 13:52
 */
@Data
public class MsgManufacturerUpdateParam {

    /**
     * 厂家名称
     */
    @Size(max = 30, message = "厂家名称不能大于30个字符")
    @NotEmpty(message = "厂家名称不能为空")
    private String name;
}
