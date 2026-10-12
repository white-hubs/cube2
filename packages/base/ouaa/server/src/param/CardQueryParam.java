package com.guhecloud.cube.biz.ouaa.server.param;

import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * 卡片查询参数
 *
 * @author lanchun
 * @date 2022/5/23 16:35
 */
@Data
public class CardQueryParam {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 访问端id
     */
    private Long terminalId;

    /**
     * 应用id
     */
    private Long applicationId;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 启用状态
     */
    private Boolean enabled;
}
