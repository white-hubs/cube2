package com.guhecloud.cube.biz.ouaa.metadata.enumeration;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/10/22 18:15
 * @Description: 别名配置枚举
 */
public enum AliasConfigEnum {
    /**
     * 访问端别名配置
     */
    TERMINAL_ALIAS,

    /**
     * 应用别名配置
     */
    APPLICATION_ALIAS,

    /**
     * 菜单别名配置
     */
    MENU_ALIAS,
    ;

    public enum OuaaCode {

        OUAA("认证中心");

        private final String label;

        OuaaCode(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }
}
