package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.vo.MonthlyMarketCountStatistics;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.po.MarketMonthlyReportPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * @Author: lijie
 * @CreateTime: 2023-09-07  16:46
 * @Description:
 * @Version: 1.0
 */
@Mapper
public interface MarketMonthlyReportMapper extends BaseMapper<MarketMonthlyReportPO> {

    List<MonthlyMarketCountStatistics> selectMonthlyReportCount(@Param("monthlyReportIds") Set<Long> monthlyReportIds);
}
