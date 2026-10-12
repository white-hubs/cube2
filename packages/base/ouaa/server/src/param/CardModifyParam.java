package com.guhecloud.cube.biz.ouaa.server.param;

import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class CardModifyParam {

    /**
     * 访问端主键
     */
    @NotNull(message = "访问端主键不能为空")
    @Min(value = 1, message = "访问端主键不能小于1")
    private Long terminalId;

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
    @Size(max = 100, message = "应用编号不能大于100")
    private String applicationCode;

    /**
     * 卡片编号
     */
    @NotEmpty(message = "卡片编号不能为空")
    @Size(max = 100, message = "卡片编号不能大于100")
    private String code;

    /**
     * 卡片名称
     */
    @NotEmpty(message = "卡片名称不能为空")
    @Size(max = 100, message = "卡片名称不能大于100")
    private String name;

    /**
     * 排序数字
     */
    private Integer sort;

    /**
     * 备注
     */
    @Size(max = 150, message = "备注不能大于150")
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled = true;

    /**
     * 是否默认卡片
     */
    private Boolean defaultCard = false;

    /**
     * 附件主键
     */
    private Long fileId;
}
