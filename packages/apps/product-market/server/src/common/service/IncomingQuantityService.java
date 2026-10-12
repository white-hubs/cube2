package com.guhecloud.cube.srv.gen.product.market.server.common.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.product.market.server.common.entity.IncomingQuantity;
import com.guhecloud.cube.srv.gen.product.market.server.common.param.IncomingQuantityParam;
import com.guhecloud.cube.srv.gen.product.market.server.common.param.IncomingQuantityQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.common.vo.IncomingQuantityVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：农产品来货量服务层
 *
 * @author Appet
 * @date 2023-05-10 15:08:36
 */
public interface IncomingQuantityService extends IService<IncomingQuantity> {

    Long add(IncomingQuantityParam param);

    void update(Long id, IncomingQuantityParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    IncomingQuantityVO getById(Long id);

    Pagination<IncomingQuantityVO> getPage(IncomingQuantityQueryParam param);

}