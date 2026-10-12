package com.guhecloud.cube.srv.gen.product.market.server.pricing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingRecord;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceStatParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingRecordThirdParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceStatVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceTrendStatVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPricingRecordThirdVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：采价记录 Repository接口
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
@Repository
public interface GoodsPricingRecordMapper extends BaseMapper<GoodsPricingRecord> {

    List<GoodsPriceStatVO> getGoodsPriceStat(@Param("param") GoodsPriceStatParam param);

    Page<GoodsPriceStatVO> getGoodsPriceStat(Page page, @Param("param") GoodsPriceStatParam param);

    List<GoodsPriceTrendStatVO> getGoodsPriceTrendStat(@Param("param") GoodsPriceStatParam param);

    <T> Page<GoodsPricingRecordThirdVO> getGoodsPricingRecordThirdPage(@Param("page") Page<T> page,
                                                                       @Param("param") GoodsPricingRecordThirdParam param
                                                                      );

    List<GoodsPricingRecordThirdVO> getGoodsPricingRecordThirdList(@Param("param") GoodsPricingRecordThirdParam param);

}