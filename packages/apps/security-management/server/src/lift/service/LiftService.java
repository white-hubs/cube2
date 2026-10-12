package com.guhecloud.cube.srv.gen.securitymanagement.server.lift.service;

import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.param.LiftRecordParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.param.LiftRecordStatisticsParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.vo.LiftRecordVo;
import com.guhecloud.platform.base.model.Pagination;

import java.util.Map;

public interface LiftService {
    Pagination<LiftRecordVo> getLiftRecord(LiftRecordParam param);

    Map<String, Object> getOperationsAnalysis(LiftRecordStatisticsParam param);
}
