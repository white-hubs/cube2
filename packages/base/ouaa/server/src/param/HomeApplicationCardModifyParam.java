package com.guhecloud.cube.biz.ouaa.server.param;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * 首页应用卡片新增参数
 *
 * @author lanchun
 * @date 2022/5/13 11:35
 */
@Data
public class HomeApplicationCardModifyParam {

    /**
     * 应用主键
     */
    @Min(value = 1, message = "应用主键不能小于1")
    private Long applicationId;

    /**
     * 应用编号
     */
    @Size(max = 30, message = "应用编号长度不能大于30")
    private String applicationCode;

    /**
     * 卡片主键
     */
    @NotNull(message = "卡片主键不能为空")
    @Min(value = 1, message = "卡片主键不能小于1")
    private Long cardId;

    /**
     * 卡片编号
     */
    @NotEmpty(message = "卡片编号不能为空")
    @Size(max = 30, message = "卡片编号长度不能大于30")
    private String cardCode;

    /**
     * 卡片大小
     */
    @Size(max = 30, message = "卡片大小长度不能大于30")
    private String cardSize;

    /**
     * 序号
     */
    private Integer sort;
}
