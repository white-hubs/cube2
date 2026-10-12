package com.guhecloud.cube.biz.ouaa.server.param;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

/**
 * 租户产品保存对象
 *
 * @author lanchun
 * @date 2021/10/25 17:41
 */
@Data
public class TenantProductModifyParam {

    /**
     * 产品
     */
    @NotNull(message = "产品不能为空")
    @Size(min = 1, message = "产品数量不能小于1")
    private List<ProductParam> products;

    /**
     * 产品信息
     */
    @Data
    public static class ProductParam {

        /**
         * 产品主键
         */
        @NotNull(message = "产品主键不能为空")
        @Min(value = 1, message = "产品主键不能小于1")
        private Long id;

        /**
         * 应用主键列表
         */
        @NotNull(message = "应用列表为空")
        @Size(min = 1, message = "应用主键数量不能小于1")
        private List<Long> applicationIds;
    }
}
