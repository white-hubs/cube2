package com.guhecloud.cube.base.dataintegrate.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.dataintegrate.server.entity.MarketConfig;
import com.guhecloud.cube.base.dataintegrate.server.model.ContextParam;
import com.guhecloud.cube.base.dataintegrate.server.param.MarketConfigParam;
import com.guhecloud.cube.base.dataintegrate.server.param.MarketConfigQueryParam;
import com.guhecloud.cube.base.dataintegrate.server.vo.MarketConfigVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：采集市场配置服务层
 *
 * @author Appet
 * @date 2023-09-09 15:08:36
 */
public interface MarketConfigService extends IService<MarketConfig> {

    Long add(MarketConfigParam param);

    void update(Long id, MarketConfigParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    MarketConfigVO getById(Long id);

    Pagination<MarketConfigVO> getPage(MarketConfigQueryParam param);

    ContextParam getContextParam(Long id);
}