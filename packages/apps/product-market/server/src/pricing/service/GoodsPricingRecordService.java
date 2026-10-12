package com.guhecloud.cube.srv.gen.product.market.server.pricing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingRecord;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceStatParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingRecordParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingRecordQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingRecordThirdParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceStatVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceTrendStatVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPricingRecordThirdVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPricingRecordVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;
import java.util.Map;

/**
 * 描述：采价记录服务层
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
public interface GoodsPricingRecordService extends IService<GoodsPricingRecord> {

    Long add(GoodsPricingRecordParam param);

    void update(Long id, GoodsPricingRecordParam param);

    void updatePrice(Long id, GoodsPricingRecordParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    GoodsPricingRecordVO getById(Long id);

    Pagination<GoodsPricingRecordVO> getPage(GoodsPricingRecordQueryParam param);

    List<GoodsPricingRecordVO> getList(GoodsPricingRecordQueryParam param);

    /**
     * 统计价格情况
     *
     * @param param
     *
     * @return
     */
    Pagination<GoodsPriceStatVO> getGoodsPriceStat(GoodsPriceStatParam param);

    List<GoodsPriceStatVO> getGoodsPriceStatForAll(GoodsPriceStatParam param);

    List<List<GoodsPriceTrendStatVO>> getGoodsPriceTrendStat(GoodsPriceStatParam param);

    List<Map<String, Object>> getGoodsPriceRToC(GoodsPriceStatParam param);

    Pagination<GoodsPricingRecordThirdVO> getPricingRecordThirdPage(GoodsPricingRecordThirdParam param);

    List<GoodsPricingRecordThirdVO> getPricingRecordThirdList(GoodsPricingRecordThirdParam param);
}