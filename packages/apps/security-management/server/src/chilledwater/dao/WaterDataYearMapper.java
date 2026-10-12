package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.WaterDataYear;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.ReportDetailParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DataTotalVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.ReportDetailYearVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.WaterDataDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WaterDataYearMapper extends BaseMapper<WaterDataYear> {
    Page<ReportDetailYearVo> reportDetailYear(Page page, @Param("param") ReportDetailParam param);

    List<ReportDetailYearVo> reportDetailYearExcel(@Param("param") ReportDetailParam param);

    DataTotalVo reportDetailYearTotal(@Param("param") ReportDetailParam param);

    Page<WaterDataDetailVo> getReportDetailYear(Page page, @Param("startTime") String startTime, @Param("endTime") String endTime,
                                                @Param("deviceCode") String deviceCode
                                               );

    Page<WaterDataDetailVo> getReportDetailMonth(Page page, @Param("startTime") String startTime, @Param("endTime") String endTime,
                                                 @Param("deviceCode") String deviceCode
                                                );
}
