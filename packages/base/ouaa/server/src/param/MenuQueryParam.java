package com.guhecloud.cube.biz.ouaa.server.param;

import lombok.Data;

/**
 * 菜单查询参数
 *
 * @author lanchun
 * @date 2021/10/13 20:23
 */
@Data
public class MenuQueryParam {

    /**
     * 关键字
     */
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
     * 启用状态
     */
    private Boolean enabled;
}
