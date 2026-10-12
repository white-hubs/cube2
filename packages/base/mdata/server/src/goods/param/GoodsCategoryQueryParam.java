package com.guhecloud.cube.biz.mdata.server.goods.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;

/**
 * 商品分类查询信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GoodsCategoryQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 市场租户编号
     */
    private Integer tenant;

}
