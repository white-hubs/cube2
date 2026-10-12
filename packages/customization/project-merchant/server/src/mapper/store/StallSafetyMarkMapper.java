package com.guhecloud.cube.deliver.project.merchant.mapper.store;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.deliver.project.merchant.dto.store.StallScoreDTO;
import com.guhecloud.cube.deliver.project.merchant.dto.store.StallScoreQueryDTO;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallSafetyMark;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 档位安全生产值 Mapper 接口
 * </p>
 *
 * @author zwb
 * @since 2021-12-24
 */
@Mapper
public interface StallSafetyMarkMapper extends BaseMapper<StallSafetyMark> {

    /**
     * 直接修改分数
     *
     * @param id   ID
     * @param mark 加减分值
     * @return 执行SQL后受影响行数
     */
    int updateScore(@Param("id") String id, @Param("mark") int mark);

    /**
     * 巡检扣分
     *
     * @param id   ID
     * @param mark 扣分值（正数扣分，负数加分）
     * @param size 巡检扣分条数
     * @return 执行SQL后受影响行数
     */

    int updateScoreAndInspTotal(@Param("id") String id, @Param("mark") int mark, @Param("size") int size);

    /**
     * 查询档位的基本信息，以及档位分值和评价
     * @param queryDTO
     * @return
     */
    List<StallScoreDTO> getStallScoreAndEvaluation(@Param("queryDTO") StallScoreQueryDTO queryDTO);

}
