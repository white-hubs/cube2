package com.guhecloud.cube.srv.gen.product.market.server.pricing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingTaskTransfer;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingTaskTransferParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingTaskTransferQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPricingTaskTransferVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：采价任务移交
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
public interface GoodsPricingTaskTransferService extends IService<GoodsPricingTaskTransfer> {

    Long add(GoodsPricingTaskTransferParam param);

    GoodsPricingTaskTransferVO getById(Long id);

    Pagination<GoodsPricingTaskTransferVO> getPage(GoodsPricingTaskTransferQueryParam param);

    List<GoodsPricingTaskTransferVO> getList(GoodsPricingTaskTransferQueryParam param);
}