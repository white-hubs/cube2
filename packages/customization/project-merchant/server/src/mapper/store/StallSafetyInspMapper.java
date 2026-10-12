package com.guhecloud.cube.deliver.project.merchant.mapper.store;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.deliver.project.merchant.entity.store.StallSafetyInsp;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 档位安全生产巡检扣分记录 Mapper 接口
 * </p>
 *
 * @author zwb
 * @since 2021-12-23
 */
@Mapper
public interface StallSafetyInspMapper extends BaseMapper<StallSafetyInsp> {
  Integer getScoreRecord(@Param("period") Period period,@Param("stallId")String stallId);

}
