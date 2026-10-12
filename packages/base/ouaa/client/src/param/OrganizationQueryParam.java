package com.guhecloud.cube.biz.ouaa.client.param;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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