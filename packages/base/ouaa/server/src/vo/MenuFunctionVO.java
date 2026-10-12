package com.guhecloud.cube.biz.ouaa.server.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 菜单数据模型
 *
 * @author Woody
 * @date 2021/6/30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuFunctionVO {

    /**
     * 菜单id
     */
    private Long menuId;

    /**
     * 功能ID
     */
    private Long functionId;

    /**
     * 功能名称
     */
    private String functionName;

    /**
     * 功能编码
     */
    private String functionCode;

    /**
     * 功能是否启用
     */
    private Boolean enabled;

    public MenuFunctionVO(Long menuId, Long functionId) {
        this.menuId = menuId;
        this.functionId = functionId;
    }
}
