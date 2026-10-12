package com.guhecloud.cube.deliver.project.merchant.service.impl.store;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallEvaluationRecordTag;
import com.guhecloud.cube.deliver.project.merchant.service.store.StallEvaluationRecordTagService;
import com.guhecloud.cube.deliver.project.merchant.mapper.store.StallEvaluationRecordTagMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 评价记录关联标签表 服务实现类
 * </p>
 *
 * @author bq
 * @since 2022-06-24
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StallEvaluationRecordTagServiceImpl extends ServiceImpl<StallEvaluationRecordTagMapper, StallEvaluationRecordTag> implements StallEvaluationRecordTagService {

}
