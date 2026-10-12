package com.guhecloud.cube.biz.ouaa.server.param;

import com.guhecloud.platform.base.model.Paging;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

/**
 * 产品查询参数
 *
 * @author Woody
 * @date 2021/7/16
 */
@Data
public class ProductQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字长度不能大于100")
    private String keyword;

    /**
     * 是否启用
     */
    private Boolean enabled;

    /**
     * 产品id集合
     */
    private Set<Long> ids;
}
