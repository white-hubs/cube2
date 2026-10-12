package com.guhecloud.cube.biz.ouaa.server.param;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 访问端保存参数
 *
 * @author lanchun
 * @date 2021/10/13 18:47
 */
@Data
public class TerminalModifyParam {

    /**
     * 应用主键
     */
    @Deprecated
    private Long productId;

    /**
     * 访问端编号
     */
    @NotEmpty(message = "访问端编号不能为空")
    @Size(max = 100, message = "访问端编号长度不能大于100")
    private String code;

    /**
     * 访问端名称
     */
    @NotEmpty(message = "访问端名称不能为空")
    @Size(max = 100, message = "访问端名称长度不能大于100")
    private String name;

    /**
     * 访问端备注
     */
    private String remark;

    /**
     * 是否启用
     */
    private Boolean enabled = true;
}
