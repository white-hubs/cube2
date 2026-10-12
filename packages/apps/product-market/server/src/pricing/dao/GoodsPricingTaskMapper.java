package com.guhecloud.cube.srv.gen.product.market.server.pricing.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.entity.GoodsPricingTask;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.param.GoodsPricingTaskQueryParam;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.CommonStatVO;
import com.guhecloud.cube.srv.gen.product.market.server.pricing.vo.MyTaskStatVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 描述：采价任务 Repository接口
 *
 * @author Appet
 * @date 2023-05-16 15:08:36
 */
@Repository
public interface GoodsPricingTaskMapper extends BaseMapper<GoodsPricingTask> {

    Page<GoodsPricingTask> getGoodsPricingTaskPage(Page page, @Param("param") GoodsPricingTaskQueryParam param);

    List<CommonStatVO> getGoodsPricingTaskStat(@Param("param") GoodsPricingTaskQueryParam param);

    MyTaskStatVO getMyPricingTaskStat(@Param("param") GoodsPricingTaskQueryParam param);
}