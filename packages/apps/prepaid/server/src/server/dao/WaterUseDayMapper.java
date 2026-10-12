package com.guhecloud.cube.srv.prepaid.server.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.guhecloud.cube.srv.prepaid.server.po.WaterUseDayPO;
import com.guhecloud.cube.srv.prepaid.server.result.UseDayStatResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 日用水mapper
 */
@Mapper
public interface WaterUseDayMapper extends BaseMapper<WaterUseDayPO> {
    /**
     * 获取水表日用能统计
     *
     * @return 集合
     */
    @Select(" select use_date,sum(pv) as pv from water_use_day ${ew.customSqlSegment} ")
    List<UseDayStatResult> getWaterUseDayStat(@Param(Constants.WRAPPER) Wrapper<?> wrapper);
}
