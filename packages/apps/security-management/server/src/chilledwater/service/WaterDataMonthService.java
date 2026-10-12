package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.WaterDataMonth;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.ReportDetailParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.ReportDetailMonthVo;

import java.util.List;
import java.util.Map;

public interface WaterDataMonthService extends IService<WaterDataMonth> {
    Map<String, Object> reportDetailMonth(ReportDetailParam reportDetailParam);

    List<ReportDetailMonthVo> reportDetailMonthExcel(ReportDetailParam reportDetailParam);
}
