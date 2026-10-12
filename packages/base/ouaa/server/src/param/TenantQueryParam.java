package com.guhecloud.cube.biz.ouaa.server.param;

import com.guhecloud.platform.base.model.Paging;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Collection;

/**
 * 租户查询参数
 *
 * @author Woody
 * @date 2021/7/14
 */
@Data
public class TenantQueryParam extends Paging {
    /**
     * 租住编号
     */
    @Min(value = 1, message = "租住编号不能小于1")
    private Integer code;

    /**
     * 租户名称
     */
    @Size(max = 100, message = "租户名称长度不能大于100")
    private String name;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 租户编号集合
     */
    private Collection<Integer> codes;
}
