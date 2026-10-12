package com.guhecloud.cube.base.device.center.metadata.std.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author wql
 * @describe 产品品类分页查询
 * @date 2024/3/12 18:06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryPageParam extends Paging {
    /**
     * 品类code
     */
    private String codes;
}
