package com.guhecloud.cube.deliver.project.merchant.service.impl.store;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.service.store.StallEvaluationService;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallEvaluation;
import com.guhecloud.cube.deliver.project.merchant.mapper.store.StallEvaluationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 档位评价总表 服务实现类
 * </p>
 *
 * @author benhuabao
 * @since 2021-12-22
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StallEvaluationServiceImpl extends ServiceImpl<StallEvaluationMapper, StallEvaluation> implements StallEvaluationService {


}
