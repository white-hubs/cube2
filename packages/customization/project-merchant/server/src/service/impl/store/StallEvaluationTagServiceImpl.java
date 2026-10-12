package com.guhecloud.cube.deliver.project.merchant.service.impl.store;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.service.store.StallEvaluationTagService;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallEvaluationTag;
import com.guhecloud.cube.deliver.project.merchant.mapper.store.StallEvaluationTagMapper;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 档位评价标签 服务实现类
 * </p>
 *
 * @author bq
 * @since 2022-06-17
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StallEvaluationTagServiceImpl extends ServiceImpl<StallEvaluationTagMapper, StallEvaluationTag> implements StallEvaluationTagService {

    @Override
    public void insertOrUpdate(StallEvaluationTag tag) {
        StallEvaluationTag one = this.lambdaQuery().eq(StallEvaluationTag::getProjectId, tag.getProjectId()).eq(StallEvaluationTag::getEvaluation, tag.getEvaluation())
                .eq(StallEvaluationTag::getTag, tag.getTag()).eq(StallEvaluationTag::getCategory, tag.getCategory()).one();
        if (one != null && !one.getId().equals(tag.getId())) {
            throw new ServiceException("评价标签已经存在，请您重新输入");
        }
        this.saveOrUpdate(tag);
    }

}
