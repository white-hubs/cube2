package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.vo.MonthlyMarketCountStatistics;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.po.MarketMonthlyWindUpPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

@Mapper
public interface MarketMonthlyWindUpMapper extends BaseMapper<MarketMonthlyWindUpPO> {

    List<MonthlyMarketCountStatistics> selectMonthlyWindUpCount(@Param("monthlyReportIds") Set<Long> monthlyReportIds);
}
