package com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.request.query.MarketMonthlyReportUrlQuery;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.dto.vo.MarketMonthlyReportUrlVO;
import com.guhecloud.cube.srv.gen.safety.management.monthlyreport.po.MarketMonthlyReportUrlPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MarketMonthlyReportUrlMapper extends BaseMapper<MarketMonthlyReportUrlPO> {

    Page<MarketMonthlyReportUrlVO> selectPage(Page page, @Param("query") MarketMonthlyReportUrlQuery query);
}
