package com.guhecloud.cube.biz.ouaa.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * 产品线查询参数
 *
 * @author Woody
 * @date 2021/7/16
 */
@Data
public class ProductLineQueryParam extends Paging {
    /**
     * 关键字名称
     */
    @Size(max = 100, message = "关键字名称长度不能大于100")
    private String keyword;

    /**
     * 是否启用
     */
    private Boolean enabled;
}
