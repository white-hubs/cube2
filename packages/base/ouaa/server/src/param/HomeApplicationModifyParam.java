package com.guhecloud.cube.biz.ouaa.server.param;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 首页应用新增参数
 *
 * @author lanchun
 * @date 2022/5/13 11:35
 */
@Data
public class HomeApplicationModifyParam {

    /**
     * 应用主键
     */
    @NotNull(message = "应用主键不能为空")
    @Min(value = 1, message = "应用主键不能小于1")
    private Long applicationId;

    /**
     * 应用编号
     */
    @NotEmpty(message = "应用编号不能为空")
    @Size(max = 30, message = "应用编号长度不能大于30")
    private String applicationCode;

    /**
     * 应用序号
     */
    @NotNull(message = "应用序号不能为空")
    private Integer sort;

}
