package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.WaterDataMonth;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.ReportDetailParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DataKeyValueVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.DataTotalVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.ReportDetailMonthVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WaterDataMonthMapper extends BaseMapper<WaterDataMonth> {
    Page<ReportDetailMonthVo> reportDetailMonth(Page page, @Param("param") ReportDetailParam param);

    List<ReportDetailMonthVo> reportDetailMonthExcel(@Param("param") ReportDetailParam param);

    DataTotalVo reportDetailMonthTotal(@Param("param") ReportDetailParam param);

    Integer numberOfActiveMerchants(@Param("startTime") String startTime, @Param("endTime") String endTime);

    Integer numberOfMerchants(@Param("startTime") String startTime, @Param("endTime") String endTime);

    Integer numberOfActiveDevice(@Param("startTime") String startTime, @Param("endTime") String endTime);

    Integer numberOfDevice(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<DataKeyValueVo> getNumberOfActiveMerchants(@Param("startTime") String startTime, @Param("endTime") String endTime);

    List<DataKeyValueVo> getNumberOfActiveDevice(@Param("startTime") String startTime, @Param("endTime") String endTime);

}
