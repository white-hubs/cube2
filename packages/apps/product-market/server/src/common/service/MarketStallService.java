package com.guhecloud.cube.srv.gen.product.market.server.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.product.market.server.common.entity.MarketStall;
import com.guhecloud.cube.srv.gen.product.market.server.common.param.MarketStallParam;
import com.guhecloud.cube.srv.gen.product.market.server.common.param.MarketStallQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.common.vo.MarketStallGroupByBossVO;
import com.guhecloud.cube.srv.gen.product.market.server.common.vo.MarketStallVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：档位管理服务层
 *
 * @author Appet
 * @date 2023-05-10 15:08:36
 */
public interface MarketStallService extends IService<MarketStall> {

    Long add(MarketStallParam param);

    void update(Long id, MarketStallParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    MarketStallVO getById(Long id);

    Pagination<MarketStallVO> getPage(MarketStallQueryParam param);

    Pagination<MarketStallGroupByBossVO> getStallGroupByBossPage(MarketStallQueryParam param);

}