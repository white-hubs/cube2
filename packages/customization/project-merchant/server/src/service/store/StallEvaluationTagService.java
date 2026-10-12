package com.guhecloud.cube.deliver.project.merchant.service.store;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallEvaluationTag;

/**
 * <p>
 * 档位评价标签 服务类
 * </p>
 *
 * @author bq
 * @since 2022-06-17
 */
public interface StallEvaluationTagService extends IService<StallEvaluationTag> {

    void insertOrUpdate(StallEvaluationTag tag);

}
