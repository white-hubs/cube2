package com.guhecloud.cube.biz.ouaa.server.param;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

/**
 * 组织机构查询参数
 *
 * @author Woody
 * @date 2021/7/16
 */
@Data
public class OrganizationQueryParam {
    /**
     * 组织机构名称
     */
    @Size(max = 100, message = "组织机构名称长度不能大于100")
    private String name;

    /**
     * 是否启用
     */
    private Boolean enabled;
    /**
     * 组织id集合
     */
    private Set<Long> ids;
}
