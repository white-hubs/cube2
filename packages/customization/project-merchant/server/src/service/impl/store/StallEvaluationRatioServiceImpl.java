package com.guhecloud.cube.deliver.project.merchant.service.impl.store;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallEvaluationRatio;
import com.guhecloud.cube.deliver.project.merchant.mapper.store.StallEvaluationRatioMapper;
import com.guhecloud.cube.deliver.project.merchant.service.store.StallEvaluationRatioService;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 档位评价维度权重占比 服务实现类
 * </p>
 *
 * @author bq
 * @since 2022-06-16
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StallEvaluationRatioServiceImpl extends ServiceImpl<StallEvaluationRatioMapper, StallEvaluationRatio> implements StallEvaluationRatioService {

    @Override
    public void insertOrUpdate(StallEvaluationRatio ratio) {
        float f = ratio.getSRatio() + ratio.getTRatio() + ratio.getWRatio() + ratio.getZRatio();
        if (f != 1f) throw new ServiceException("权重之和必须等于1，请您重新设置");
        this.saveOrUpdate(ratio);
    }

}
