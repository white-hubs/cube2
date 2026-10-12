package com.guhecloud.cube.biz.ouaa.server.param;

import lombok.Data;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 应用路径保存参数
 *
 * @author lanchun
 * @date 2022/7/1 13:57
 */
@Data
public class ApplicationPathModifyParam {

    /**
     * 应用主键
     */
    @NotNull(message = "应用主键不能为空")
    private Long id;

    /**
     * 路径
     */
    @Size(max = 150, message = "路径不能大于150")
    private String path;
}
