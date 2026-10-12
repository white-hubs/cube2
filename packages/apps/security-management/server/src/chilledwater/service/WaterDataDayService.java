package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity.WaterDataDay;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.ReportDetailParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param.UpdateDataParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo.ReportDetailDayVo;

import java.util.List;
import java.util.Map;

public interface WaterDataDayService extends IService<WaterDataDay> {
    Map<String, Object> reportDetailDay(ReportDetailParam reportDetailParam);

    Map<String, Object> reportDetailElectricity(ReportDetailParam reportDetailParam);

    List<ReportDetailDayVo> reportDetailDayExcel(ReportDetailParam reportDetailParam);

    List<ReportDetailDayVo> reportDetailDayExcelElectricCharge(ReportDetailParam reportDetailParam);

    void addData(UpdateDataParam updateDataParam);

    void updateData(UpdateDataParam updateDataParam);
}
