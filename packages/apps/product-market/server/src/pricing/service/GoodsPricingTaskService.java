package com.guhecloud.cube.srv.gen.product.market.server.pricing.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingTask;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingTaskParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingTaskQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.CommonStatVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.GoodsPricingTaskVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.MyTaskStatVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：采价任务服务层
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
public interface GoodsPricingTaskService extends IService<GoodsPricingTask> {

    Long add(GoodsPricingTaskParam param);

    void update(Long id, GoodsPricingTaskParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    GoodsPricingTaskVO getById(Long id);

    GoodsPricingTaskVO getByIdForApp(Long id);

    Pagination<GoodsPricingTaskVO> getPage(GoodsPricingTaskQueryParam param);

    Pagination<GoodsPricingTaskVO> getPageForApp(GoodsPricingTaskQueryParam param);

    void overPricingTask(Long id, String remark);

    Long overPricingTaskCheck(Long id);

    void transferPricingTask(Long id, String fromUserId, String toUserId);

    void receivePricingTask(Long id);

    List<CommonStatVO> getGoodsPricingTaskStat(GoodsPricingTaskQueryParam param);

    MyTaskStatVO getMyPricingTaskStat(GoodsPricingTaskQueryParam param);
}