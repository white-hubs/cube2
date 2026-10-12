package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.WaterDataDay;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.ReportDetailParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DataTotalVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.ReportDetailDayVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.WaterDataDetailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WaterDataDayMapper extends BaseMapper<WaterDataDay> {

    Page<ReportDetailDayVo> reportDetailDay(Page page, @Param("param") ReportDetailParam param);

    Page<ReportDetailDayVo> reportDetailDayElectricCharge(Page page, @Param("param") ReportDetailParam param);

    List<ReportDetailDayVo> reportDetailDayExcel(@Param("param") ReportDetailParam param);

    List<ReportDetailDayVo> reportDetailDayExcelElectricCharge(@Param("param") ReportDetailParam param);

    DataTotalVo reportDetailDayTotal(@Param("param") ReportDetailParam param);

    Page<WaterDataDetailVo> getReportDetailDay(Page page, @Param("startTime") String startTime, @Param("endTime") String endTime,
                                               @Param("deviceCode") String deviceCode
                                              );

    List<WaterDataDetailVo> selectTotalDataByDateTime(@Param("dateTimes") List<String> dateTimes);
}
