package com.guhecloud.cube.deliver.project.merchant.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.deliver.project.merchant.dto.FireinspItemEvaluationDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.FireinspItemEvaluation;

/**
 * <p>
 * 巡检项评价配置 服务类
 * </p>
 *
 * @author zwb
 * @since 2021-12-22
 */
public interface FireinspItemEvaluationService extends IService<FireinspItemEvaluation> {

    void save(FireinspItemEvaluationDTO dto);

    void update(FireinspItemEvaluationDTO dto);

    void remove(String id);
}
