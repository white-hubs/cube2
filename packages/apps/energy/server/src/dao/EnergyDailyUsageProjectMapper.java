package com.guhecloud.cube.srv.gen.energymanagement.server.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.energymanagement.server.entity.EnergyDailyUsageProject;
import com.guhecloud.cube.srv.gen.energymanagement.server.queryparam.EnergyTrendUsageParam;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyDailyUsageProjectResultVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyProjectAreaStatVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.EnergyProjectStatVO;
import com.guhecloud.cube.srv.gen.energymanagement.server.vo.TrendStatVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface EnergyDailyUsageProjectMapper extends BaseMapper<EnergyDailyUsageProject> {

    @InterceptorIgnore(illegalSql = "true", tenantLine = "true")
    void insertOrUpdateEnergyDailyUsageProject(List<EnergyDailyUsageProject> dataList);

    List<TrendStatVO> getProjectUsageStatForTrend(@Param("param") EnergyTrendUsageParam param);

    List<TrendStatVO> getProjectUsageStatByTime(@Param("param") EnergyTrendUsageParam param);

    List<TrendStatVO> getProjectUsageStatByCategory(@Param("param") EnergyTrendUsageParam param);

    List<EnergyProjectStatVO> getProjectUsageStatByTypeAndProject(@Param("param") EnergyTrendUsageParam param);

    List<TrendStatVO> getProjectUsageStatByArea(@Param("param") EnergyTrendUsageParam param);

    List<TrendStatVO> getProjectUsageStatByItem(@Param("param") EnergyTrendUsageParam param);

    @Select("select id,stat_date,DATE_FORMAT(stat_date,'%Y-%m-%d') as time,instrument_type,instrument_type_name,project_category,project_id,project_code,project_name,d_value as dd_value,dc_value,zh_value,create_time " +
            "from energy_daily_usage_project " +
            "${ew.customSqlSegment}"
    )
    Page<EnergyDailyUsageProjectResultVO> getProjectPage(@Param("page") Page<EnergyDailyUsageProject> page,
                                                         @Param(Constants.WRAPPER) Wrapper<EnergyDailyUsageProject> wrapper
                                                        );

    @Select("select stat_date,DATE_FORMAT(stat_date,'%Y-%m-%d') as time, instrument_type,instrument_type_name,project_category,project_id,project_code,project_name,sum(d_value) as ddValue,sum(dc_value) as dcValue,sum(zh_value) as zhValue,create_time " +
            "from energy_daily_usage_project  " +
            "${ew.customSqlSegment} "
    )
    EnergyProjectAreaStatVO getDailyUsageProjectStatAllOne(@Param(Constants.WRAPPER) Wrapper<EnergyDailyUsageProject> wrapper);
}
