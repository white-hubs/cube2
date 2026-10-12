package com.guhecloud.cube.srv.gen.securitymanagement.server.passengerflow.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.util.SecurityManagementCommonUtil;
import com.guhecloud.cube.srv.gen.securitymanagement.server.passengerflow.dao.PassengerFlowRecordMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.passengerflow.entity.PassengerFlowRecord;
import com.guhecloud.cube.srv.gen.securitymanagement.server.passengerflow.param.PassengerFlowStatisticsParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.passengerflow.service.PassengerFlowRecordService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.passengerflow.vo.AnalysisVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.passengerflow.vo.RankingPassengerFlowVo;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PassengerFlowRecordServiceImpl extends ServiceImpl<PassengerFlowRecordMapper, PassengerFlowRecord> implements PassengerFlowRecordService {

    private final PassengerFlowRecordMapper passengerFlowRecordMapper;

    @Override
    public Map<String, Object> getPassengerFlowStatistics(PassengerFlowStatisticsParam param) {
        Map<String, Object> reMap = new HashMap<>();
        var begin = param.getPeriod().getBegin();
        var end = param.getPeriod().getEnd();
        //环比时间
        var oldBegin = DateUtils.offset(begin, ChronoUnit.DAYS, -1);
        var oldEnd = DateUtils.offset(end, ChronoUnit.DAYS, -1);

        //出馆
        Integer passengerFlowRecordOutCount = passengerFlowRecordMapper.getPassengerFlowRecordCount("0", begin, end);
        //环比数据
        Integer passengerFlowRecordOutCountQOQ = passengerFlowRecordMapper.getPassengerFlowRecordCount("0", oldBegin, oldEnd);
        BigDecimal rateOut = SecurityManagementCommonUtil.getRate(String.valueOf(passengerFlowRecordOutCountQOQ), String.valueOf(passengerFlowRecordOutCount));

        //进馆
        Integer passengerFlowRecordInCount = passengerFlowRecordMapper.getPassengerFlowRecordCount("1", begin, end);
        Integer passengerFlowRecordInCountQOQ = passengerFlowRecordMapper.getPassengerFlowRecordCount("1", oldBegin, oldEnd);
        BigDecimal rateIn = SecurityManagementCommonUtil.getRate(String.valueOf(passengerFlowRecordInCountQOQ), String.valueOf(passengerFlowRecordInCount));
        Integer inMuseum = passengerFlowRecordInCount - passengerFlowRecordOutCount;
        reMap.put("outCount", passengerFlowRecordOutCount);
        reMap.put("rateOut", rateOut);
        reMap.put("inCount", passengerFlowRecordInCount);
        reMap.put("rateIn", rateIn);
        reMap.put("inMuseum", inMuseum < 0 ? 0 : inMuseum);
        return reMap;
    }

    @Override
    public List<RankingPassengerFlowVo> getRankingPassengerFlowRegions(String flag, PassengerFlowStatisticsParam param) {
        List<RankingPassengerFlowVo> rankingPassengerFlowRegions = passengerFlowRecordMapper.getRankingPassengerFlowRegions(flag, param);
        return rankingPassengerFlowRegions;
    }

    @Override
    public Map<String, Object> getAnalysisChartPassengerMuseum(PassengerFlowStatisticsParam param) {
        Map<String, Object> remap = new HashMap<>();
        Map<String, Object> dateFormat = SecurityManagementCommonUtil.getDateFormat(param.getType(), param.getPeriod().getBegin(), param
                .getPeriod().getEnd());
        var st = (LocalDateTime) dateFormat.get("st");
        var et = (LocalDateTime) dateFormat.get("et");
        String timeFlag = (String) dateFormat.get("timeFlag");
        List<String> xtitle = (List<String>) dateFormat.get("xtitle");
        List<AnalysisVo> analysisChartPassengerMuseum = passengerFlowRecordMapper.getAnalysisChartPassengerMuseum(st, et, timeFlag);
        remap.put("xtitle", xtitle);
        remap.put("analysis", analysisChartPassengerMuseum);
        return remap;
    }

    @Override
    public void insertOrUpdatePassengerFlowStat(List<PassengerFlowRecord> dataList) {
        passengerFlowRecordMapper.insertOrUpdatePassengerFlowStat(dataList);
    }
}
