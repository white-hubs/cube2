package com.guhecloud.cube.biz.ouaa.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

/**
 * 角色查询参数
 *
 * @author Woody
 * @date 2021/7/17
 */
@Data
public class RoleQueryParam extends Paging {
    /**
     * 产品主键
     */
    @Min(value = 1, message = "产品主键不能小于1")
    private Long productId;

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字长度不能大于100")
    private String keyword;

    /**
     * 角色名称
     */
    @Size(max = 100, message = "角色名称长度不能大于100")
    private String name;

    /**
     * 是否内置
     */
    private Boolean builtin;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
