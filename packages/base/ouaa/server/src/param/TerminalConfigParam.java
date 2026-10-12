package com.guhecloud.cube.biz.ouaa.server.param;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/8/13 15:44
 * @Description:
 */

@Data
public class TerminalConfigParam {

    /**
     * 访问端编码
     */
    private String code;

    /**
     * 默认角色
     */
    private Long defaultRole;

    /**
     * 别名
     */
    private String alias;
}
