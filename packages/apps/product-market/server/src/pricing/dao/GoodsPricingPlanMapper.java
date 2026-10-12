package com.guhecloud.cube.srv.gen.product.market.server.pricing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingPlan;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.CommonStatVO;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：商品采价计划 Repository接口
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
@Repository
public interface GoodsPricingPlanMapper extends BaseMapper<GoodsPricingPlan> {

    @Select("select p.status as code,count(1) as value from goods_pricing_plan p\n" +
            "group by p.status"
    )
    List<CommonStatVO> GoodsPricingPlanStatusStat();
}