package com.guhecloud.cube.srv.gen.product.market.server.pricing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPriceWarnRecord;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnRecordParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnRecordQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnStatParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.CommonStatVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceWarnRecordVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：采价预警记录服务层
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
public interface GoodsPriceWarnRecordService extends IService<GoodsPriceWarnRecord> {

    Long add(GoodsPriceWarnRecordParam param);

    void update(Long id, GoodsPriceWarnRecordParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    GoodsPriceWarnRecordVO getById(Long id);

    Pagination<GoodsPriceWarnRecordVO> getPage(GoodsPriceWarnRecordQueryParam param);

    List<CommonStatVO> goodsPriceWarnStatusStatByTime(GoodsPriceWarnStatParam param);
}