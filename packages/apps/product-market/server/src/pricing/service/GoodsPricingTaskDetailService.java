package com.guhecloud.cube.srv.gen.product.market.server.pricing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingTask;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingTaskDetail;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingTaskDetailParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingTaskDetailQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPricingTaskDetailVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：采价任务详情服务层
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
public interface GoodsPricingTaskDetailService extends IService<GoodsPricingTaskDetail> {

    Long add(GoodsPricingTaskDetailParam param);

    //添加采价任务详情
    void addAndNotice(GoodsPricingTask task, GoodsPricingTaskDetail taskDetail);

    void update(Long id, GoodsPricingTaskDetailParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    GoodsPricingTaskDetailVO getById(Long id);

    Pagination<GoodsPricingTaskDetailVO> getPage(GoodsPricingTaskDetailQueryParam param);

}