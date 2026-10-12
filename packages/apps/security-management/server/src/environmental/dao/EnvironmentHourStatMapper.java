package com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.entity.EnvironmentHourStat;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.param.EnvironmentHourParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.result.HourStatAvgResult;
import com.guhecloud.cube.srv.gen.securitymanagement.server.environmental.result.TrendResult;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 环境监测小时统计
 */
@Mapper
public interface EnvironmentHourStatMapper extends BaseMapper<EnvironmentHourStat> {

    /**
     * 获取环境趋势
     *
     * @param format 格式时间化格式
     * @param period 时间区间
     *
     * @return 趋势
     */
    List<TrendResult> selectTrend(@Param("format") String format, @Param("period") Period period);

    /**
     * @param param
     *
     * @return
     */
    HourStatAvgResult hourStatAvg(@Param("param") EnvironmentHourParam param);
}
