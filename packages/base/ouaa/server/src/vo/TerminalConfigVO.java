package com.guhecloud.cube.biz.ouaa.server.vo;

import lombok.Data;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/8/13 15:45
 * @Description:
 */

@Data
public class TerminalConfigVO {

    /**
     * 主键
     */
    private Long id;

    /**
     * 访问端名称
     */
    private String terminalName;

    /**
     * 访问端编码
     */
    private String code;

    /**
     * 默认角色
     */
    private Long defaultRole;
    /**
     * 默认角色名称
     */
    private String defaultRoleName;

    /**
     * 别名
     */
    private String alias;
}
