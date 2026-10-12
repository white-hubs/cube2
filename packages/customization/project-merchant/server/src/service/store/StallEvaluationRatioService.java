package com.guhecloud.cube.deliver.project.merchant.service.store;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallEvaluationRatio;

/**
 * <p>
 * 档位评价维度权重占比 服务类
 * </p>
 *
 * @author bq
 * @since 2022-06-16
 */
public interface StallEvaluationRatioService extends IService<StallEvaluationRatio> {

    void insertOrUpdate(StallEvaluationRatio ratio);

}
