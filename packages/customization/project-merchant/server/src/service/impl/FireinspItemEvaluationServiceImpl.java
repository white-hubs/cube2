package com.guhecloud.cube.deliver.project.merchant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.deliver.project.merchant.commons.utils.BizCodeUtils;
import com.guhecloud.cube.deliver.project.merchant.service.FireinspItemEvaluationService;
import com.guhecloud.cube.deliver.project.merchant.dto.FireinspItemEvaluationDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.FireinspItemEvaluation;
import com.guhecloud.cube.deliver.project.merchant.mapper.FireinspItemEvaluationMapper;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * <p>
 * 巡检项评价配置 服务实现类
 * </p>
 *
 * @author zwb
 * @since 2021-12-22
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FireinspItemEvaluationServiceImpl extends ServiceImpl<FireinspItemEvaluationMapper, FireinspItemEvaluation> implements FireinspItemEvaluationService {

    @Override
    public void save(FireinspItemEvaluationDTO dto) {
        // 页面提示已经是扣减分数，不需要自己设置负数，
        Integer itemResultMark = dto.getItemResultMark();
        if (itemResultMark != null && itemResultMark < 0) {
            dto.setItemResultMark(itemResultMark * -1);
        }
        //同巡检项的结果项不能相同
        long count = this.lambdaQuery()
                .eq(FireinspItemEvaluation::getStandardId, dto.getStandardId())
                .eq(FireinspItemEvaluation::getItemId, dto.getItemId())
                .eq(FireinspItemEvaluation::getItemResultCode, dto.getItemResultCode())
                .count();

        if (count > 0) {
            throw new ServiceException("该巡检项中的结果选项已存在");
        }

        FireinspItemEvaluation evaluation = dto.convert();
        evaluation.setCode(BizCodeUtils.generate("XJXPJ"));
        evaluation.setCreateBy(SecurityContextHolder.getPrincipal().getName());
        evaluation.setCreateTime(LocalDateTime.now());
        this.save(evaluation);
    }

    @Override
    public void update(FireinspItemEvaluationDTO dto) {
        // 页面提示已经是扣减分数，不需要自己设置负数，
        Integer itemResultMark = dto.getItemResultMark();
        if (itemResultMark != null && itemResultMark < 0) {
            dto.setItemResultMark(itemResultMark * -1);
        }
        //同巡检项的结果项不能相同
        long count = this.lambdaQuery()
                .ne(FireinspItemEvaluation::getId, dto.getId())
                .eq(FireinspItemEvaluation::getStandardId, dto.getStandardId())
                .eq(FireinspItemEvaluation::getItemId, dto.getItemId())
                .eq(FireinspItemEvaluation::getItemResultCode, dto.getItemResultCode())
                .count();

        if (count > 0) {
            throw new ServiceException("该巡检项中的结果选项已存在");
        }
        FireinspItemEvaluation evaluation = dto.convert();
        this.updateById(evaluation);

    }

    @Override
    public void remove(String id) {
        this.removeById(id);
    }
}
