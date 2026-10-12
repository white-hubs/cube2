package com.guhecloud.cube.srv.gen.product.market.server.pricing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPriceWarnCategory;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：采价预警规则-适用类型 Repository接口
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
@Repository
public interface GoodsPriceWarnCategoryMapper extends BaseMapper<GoodsPriceWarnCategory> {

    @Select("select * from goods_price_warn_category c " +
            "inner join goods_price_warn_rule_group g on c.group_id = g.id " +
            "where c.goods_category_id = #{goodsCategoryId} and g.`status` = 1"
    )
    List<GoodsPriceWarnCategory> getEnableRule(@Param("goodsCategoryId") Long goodsCategoryId);
}