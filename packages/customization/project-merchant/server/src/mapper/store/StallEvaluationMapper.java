package com.guhecloud.cube.deliver.project.merchant.mapper.store;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.deliver.project.merchant.dto.store.StallEvaluationRecordDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallEvaluation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 档位评价总表 Mapper 接口
 * </p>
 *
 * @author benhuabao
 * @since 2021-12-22
 */
@Mapper
public interface StallEvaluationMapper extends BaseMapper<StallEvaluation> {

    /**
     * 修改档位评价总表
     * @param dto 修改的实例内容
     */
    @Update("update stall_evaluation set total = total + 1,  " +
            " z_score = z_score + #{dto.zScore},  " +
            " s_score = s_score + #{dto.sScore},  " +
            " t_score = t_score + #{dto.tScore},  " +
            " w_score = w_score + #{dto.wScore},  " +
            " honesty_score = honesty_score + #{dto.honestyScore},  " +
            " z_score_avg = ROUND(z_score/total), " +
            " s_score_avg = ROUND(s_score/total), " +
            " t_score_avg = ROUND(t_score/total), " +
            " w_score_avg = ROUND(w_score/total), " +
            " total_score =  total_score + #{dto.zScore} + #{dto.sScore} + #{dto.tScore} + #{dto.wScore}, " +
            " honesty_score = ROUND(honesty_score/total) " +
            " where id = #{dto.id}")
    void updateStoreEvaluation(@Param("dto") StallEvaluationRecordDTO dto);

    /**
     * 删除单个评价记录时修改档位评价总表
     * @param dto 修改的实例内容
     */
    @Update("update stall_evaluation set total = total - 1,  " +
            " z_score = z_score - #{dto.zScore},  " +
            " s_score = s_score - #{dto.sScore},  " +
            " t_score = t_score - #{dto.tScore},  " +
            " w_score = w_score - #{dto.wScore},  " +
            " honesty_score = honesty_score + #{dto.honestyScore},  " +
            " z_score_avg = ROUND(z_score/total), " +
            " s_score_avg = ROUND(s_score/total), " +
            " t_score_avg = ROUND(t_score/total), " +
            " w_score_avg = ROUND(w_score/total), " +
            " total_score =  total_score - #{dto.zScore} - #{dto.sScore} - #{dto.tScore} - #{dto.wScore} ," +
            " honesty_score = ROUND(honesty_score/total) " +
            " where id = #{dto.id}")
    void updateEvaluationByDelRecord(@Param("dto") StallEvaluationRecordDTO dto);

}
