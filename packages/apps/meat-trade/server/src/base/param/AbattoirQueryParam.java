package com.guhecloud.cube.biz.meattrade.server.base.param;

import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.SortEnum;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

/**
 * 屠宰场查询信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AbattoirQueryParam extends Paging {
    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 屠宰场主键集合
     */
    private List<Long> abattoirIds;

    /**
     * 商品品类信息
     */
    private List<String> goodsCategoryIds;

    /**
     * 商品品类主键
     */
    private String goodsCategoryId;

    /**
     * 时间
     */
    private Period period;

    /**
     * 最低价格
     */
    private BigDecimal lowerPrice;

    /**
     * 白条标准重量
     */
    private BigDecimal standardWeight;

    /**
     * 升降序
     */
    private SortEnum sort;

    /**
     * 排序字段
     */
    private String item;

    /**
     * 租户编号
     */
    private Integer tenant;
}
