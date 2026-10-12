package com.guhecloud.cube.biz.ouaa.server.param;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

/**
 * 权限更新参数
 *
 * @author Woody
 * @date 2021/7/26
 */
@Data
public class PermissionModifyParam {
    /**
     * 角色主键
     */
    @NotNull(message = "角色主键不能为空")
    @Min(value = 1, message = "角色主键不能小于1")
    private Long id;

    /**
     * 应用列表
     */
    private List<Application> applications;

    /**
     * 访问端主键列表
     */
    private List<Long> terminals;

    /**
     * 应用权限信息
     */
    @Data
    public static class Application {
        @NotNull(message = "应用主键不能为空")
        @Min(value = 1, message = "应用主键不能小于1")
        private Long id;

        /**
         * 菜单主键列表
         */
        private List<Long> menus;
    }
}
