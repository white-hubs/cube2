package com.guhecloud.cube.srv.gen.energymanagement.server.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyDailySummary;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailySummaryResultVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.TrendStatVO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 每日汇总统计mapper
 */
@Mapper
public interface EnergyDailySummaryMapper extends BaseMapper<EnergyDailySummary> {

    @Select("select id,stat_date,date_str,dd_value,dc_value,zh_value,f_value as fd_value,p_value as pd_value,g_value as gd_value,j_value as jd_value,create_time" +
            " from energy_daily_summary " +
            " ${ew.customSqlSegment} "
    )
    Page<EnergyDailySummaryResultVO> getSummaryPage(@Param("page") Page page,
                                                    @Param(Constants.WRAPPER) Wrapper<EnergyDailySummary> wrapper
                                                   );

    List<TrendStatVO> getDailySummaryForTrend(@Param("period") Period period);
}
