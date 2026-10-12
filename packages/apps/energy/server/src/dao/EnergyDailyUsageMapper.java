package com.guhecloud.cube.srv.gen.energymanagement.server.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyDailyUsage;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyDailyUsageParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyTrendUsageParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailyUsageResultVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailyUsageVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailyUsageWithCarbonVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyTypeStatVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.TrendStatVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EnergyDailyUsageMapper extends BaseMapper<EnergyDailyUsage> {

    @InterceptorIgnore(illegalSql = "true", tenantLine = "true")
    void insertOrUpdateEnergyDailyUsage(List<EnergyDailyUsage> dataList);

    /**
     * 根据时间条件 统计每种类型能源的用量
     *
     * @param param
     *
     * @return
     */
    EnergyDailyUsageVO getUsageStatByInstrumentType(@Param("param") EnergyDailyUsageParam param);

    EnergyDailyUsageWithCarbonVO getUsageStatWithCarbonByTime(@Param("param") EnergyDailyUsageParam param);

    List<EnergyDailyUsageWithCarbonVO> getUsageStatWithCarbonByType(@Param("param") EnergyTrendUsageParam param);

    List<TrendStatVO> getUsageToDcStatForTrend(@Param("param") EnergyTrendUsageParam param);

    List<TrendStatVO> getUsageToDcStatByTimeAndType(@Param("param") EnergyTrendUsageParam param);

    /**
     * 按分类统计
     *
     * @param wrapper 条件
     *
     * @return 每个分类的：用量 碳排 综合能耗 f p g j
     */
    @Select(" select instrument_type as instrumentType,instrument_type_name as instrumentTypeName, sum(d_value) as ddValue,sum(dc_value) as dcValue,sum(zh_value) as zhValue " +
            " ,sum(f_value) as fValue,sum(p_value) as pValue,sum(g_value) as gValue,sum(j_value) as jValue " +
            "        from energy_daily_usage " +
            " ${ew.customSqlSegment} " +
            " group by instrument_type "
    )
    List<EnergyTypeStatVO> getUsageStatGroupType(@Param(Constants.WRAPPER) Wrapper<EnergyDailyUsage> wrapper);

    /**
     * 所有类型总和统计
     *
     * @param wrapper 条件
     *
     * @return 总的：用量 碳排 综合能耗 f p g j
     */
    @Select(" select instrument_type,instrument_type_name, sum(d_value) as ddValue,sum(dc_value) as dcValue,sum(zh_value) as zhValue " +
            " ,sum(f_value) as fValue,sum(p_value) as pValue,sum(g_value) as gValue,sum(j_value) as jValue " +
            "        from energy_daily_usage " +
            " ${ew.customSqlSegment} "
    )
    EnergyTypeStatVO getUsageStatAllIn(@Param(Constants.WRAPPER) Wrapper<EnergyDailyUsage> wrapper);

    @Select(" select id,stat_date,DATE_FORMAT(stat_date,'%Y-%m-%d') as time,instrument_type,instrument_type_name,d_value as dd_value,dc_value,zh_value,f_value as fdValue,p_value as pd_value,g_value as gd_value,j_value as jd_value,create_time " +
            " from energy_daily_usage " +
            " ${ew.customSqlSegment} "
    )
    Page<EnergyDailyUsageResultVO> getPage(@Param("page") Page page, @Param(Constants.WRAPPER) Wrapper<EnergyDailyUsage> wrapper);
}
