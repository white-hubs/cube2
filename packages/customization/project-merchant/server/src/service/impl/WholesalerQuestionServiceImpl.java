package com.guhecloud.cube.deliver.project.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.entity.storeprofile.WholesalerQuestion;
import com.guhecloud.cube.deliver.project.merchant.mapper.WholesalerQuestionMapper;
import com.guhecloud.cube.deliver.project.merchant.service.WholesalerQuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangliang
 * @since 2022-01-11
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WholesalerQuestionServiceImpl extends ServiceImpl<WholesalerQuestionMapper, WholesalerQuestion> implements WholesalerQuestionService {

}
