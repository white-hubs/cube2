package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.WaterDataYear;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.ReportDetailParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.ReportDetailYearVo;

import java.util.List;
import java.util.Map;

public interface WaterDataYearService extends IService<WaterDataYear> {
    Map<String, Object> reportDetailYear(ReportDetailParam reportDetailParam);

    List<ReportDetailYearVo> reportDetailYearExcel(ReportDetailParam reportDetailParam);
}
