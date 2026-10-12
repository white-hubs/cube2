package com.guhecloud.cube.srv.gen.product.market.server.pricing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPriceWarnRecordLog;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnRecordLogParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPriceWarnRecordLogQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPriceWarnRecordLogVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：采价预警-价格修改日志服务层
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
public interface GoodsPriceWarnRecordLogService extends IService<GoodsPriceWarnRecordLog> {

    Long add(GoodsPriceWarnRecordLogParam param);

    void update(Long id, GoodsPriceWarnRecordLogParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    GoodsPriceWarnRecordLogVO getById(Long id);

    Pagination<GoodsPriceWarnRecordLogVO> getPage(GoodsPriceWarnRecordLogQueryParam param);

}