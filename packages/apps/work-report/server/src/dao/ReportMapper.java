package com.guhecloud.cube.srv.gen.reportmanagement.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.reportmanagement.server.entity.Report;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.ReportQueryParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.StatisticsParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.vo.ReportStatistics;
import com.guhecloud.cube.srv.gen.reportmanagement.server.vo.ReportStatisticsVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ReportMapper extends BaseMapper<Report> {
    ReportStatisticsVo selectReportStatisticsInfo(@Param("param") StatisticsParam param, @Param("startTime") String startTime,
                                                  @Param("endTime") String endTime
                                                 );

    Page<ReportStatistics> selectReportStatisticsPage(@Param("page") Page page, @Param("param") ReportQueryParam param);

    Page<ReportStatistics> selectReportStatistics(@Param("page") Page page, @Param("param") ReportQueryParam param);

    List<ReportStatistics> selectReportStatistics(@Param("param") ReportQueryParam param);

    List<ReportStatistics> selectReportStatisticsGroupByDepartment(@Param("param") ReportQueryParam param);
}
