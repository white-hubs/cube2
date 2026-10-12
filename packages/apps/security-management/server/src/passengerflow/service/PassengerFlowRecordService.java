package com.guhecloud.cube.srv.gen.securitymanagement.server.passengerflow.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.passengerflow.entity.PassengerFlowRecord;
import com.guhecloud.cube.srv.gen.securitymanagement.server.passengerflow.param.PassengerFlowStatisticsParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.passengerflow.vo.RankingPassengerFlowVo;

import java.util.List;
import java.util.Map;

public interface PassengerFlowRecordService extends IService<PassengerFlowRecord> {
    //客流统计
    Map<String, Object> getPassengerFlowStatistics(PassengerFlowStatisticsParam param);

    List<RankingPassengerFlowVo> getRankingPassengerFlowRegions(String flag, PassengerFlowStatisticsParam param);

    Map<String, Object> getAnalysisChartPassengerMuseum(PassengerFlowStatisticsParam param);

    void insertOrUpdatePassengerFlowStat(List<PassengerFlowRecord> dataList);
}
