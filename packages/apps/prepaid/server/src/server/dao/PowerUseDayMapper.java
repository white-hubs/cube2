package com.guhecloud.cube.srv.prepaid.server.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.guhecloud.cube.srv.prepaid.server.po.PowerUseDayPO;
import com.guhecloud.cube.srv.prepaid.server.result.UseDayStatResult;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 日用电mapper
 */
@Mapper
public interface PowerUseDayMapper extends BaseMapper<PowerUseDayPO> {

    /**
     * 获取电表日用能统计
     *
     * @return 集合
     */
    @Select(" select use_date,sum(pv) as pv from power_use_day ${ew.customSqlSegment} ")
    List<UseDayStatResult> getPowerUseDayStat(@Param(Constants.WRAPPER) Wrapper<?> wrapper);

    /**
     * 获取电表月用能统计
     *
     * @return 集合
     */
    @Select(" select use_month as use_date,sum(pv) as pv from power_use_day ${ew.customSqlSegment} ")
    List<UseDayStatResult> getPowerUseMonthStat(@Param(Constants.WRAPPER) Wrapper<?> wrapper);
}
