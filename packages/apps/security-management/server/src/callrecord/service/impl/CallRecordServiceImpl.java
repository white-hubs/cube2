package com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.dao.BedFileMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.dao.CallRecordMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.dao.ProcessingRecordsMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.entity.BedFile;
import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.entity.CallRecord;
import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.entity.ProcessingRecords;
import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.param.CallRecordParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.service.CallRecordService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.vo.BedFileVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.vo.CallRecordVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.vo.CallTrendsVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.callrecord.vo.ProcessingRecordsVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.util.SecurityManagementCommonUtil;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
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
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CallRecordServiceImpl extends ServiceImpl<CallRecordMapper, CallRecord> implements CallRecordService {

    private final CallRecordMapper callRecordMapper;

    private final ProcessingRecordsMapper processingRecordsMapper;

    private final BedFileMapper bedFileMapper;

    @Override
    public Map<String, Object> getCallStatistics() {
        Map<String, Object> remap = new HashMap<>();
        var date = LocalDateTime.now();
        var beginOfDay = DateUtils.beginOfDay(date);
        var yeEnd = DateUtils.offset(date, ChronoUnit.DAYS, -1);
        var yeBegin = DateUtils.offset(beginOfDay, ChronoUnit.DAYS, -1);
        var weekBegin = DateUtils.beginOfWeek(date);
        var weekEnd = DateUtils.offset(date, ChronoUnit.WEEKS, -1);
        var weekYeBegin = DateUtils.offset(weekBegin, ChronoUnit.WEEKS, -1);
        //今日
        Integer callStatistics = callRecordMapper.getCallStatistics(beginOfDay, date);
        //昨日
        Integer callStatisticsYe = callRecordMapper.getCallStatistics(yeBegin, yeEnd);
        //本周
        Integer callStatisticsWeek = callRecordMapper.getCallStatistics(weekBegin, date);
        //上周
        Integer callStatisticsYeWeek = callRecordMapper.getCallStatistics(weekYeBegin, weekEnd);
        //环比
        BigDecimal rate = SecurityManagementCommonUtil.getRate(String.valueOf(callStatisticsYe), String.valueOf(callStatistics));
        //周环比
        BigDecimal rate1 = SecurityManagementCommonUtil.getRate(String.valueOf(callStatisticsYeWeek), String.valueOf(callStatisticsWeek));
        remap.put("today", callStatistics);
        remap.put("yesToday", callStatisticsYe);
        remap.put("week", callStatisticsWeek);
        remap.put("yesWeek", callStatisticsYeWeek);
        remap.put("todayRate", rate);
        remap.put("weekRate", rate1);
        return remap;
    }

    @Override
    public Map<String, Object> getCallTrends(CallRecordParam param) {
        Map<String, Object> remap = new HashMap<>();
        Map<String, Object> dateFormat = SecurityManagementCommonUtil.getDateFormat(param.getType(), param.getPeriod().getBegin(), param
                .getPeriod().getEnd());
        String timeFlag = (String) dateFormat.get("timeFlag");
        List<String> xtitle = (List<String>) dateFormat.get("xtitle");
        List<CallTrendsVo> callTrends = callRecordMapper.getCallTrends(timeFlag, param.getPeriod().getBegin(), param.getPeriod().getEnd());
        remap.put("xtitle", xtitle);
        remap.put("callTrends", callTrends);
        return remap;
    }

    @Override
    public Pagination<CallRecordVo> getCallRecordsPage(CallRecordParam param) {
        LocalDateTime begin = null;
        LocalDateTime end = null;
        if (Objects.nonNull(param.getPeriod())) {
            begin = param.getPeriod().getBegin();
            end = param.getPeriod().getEnd();
        }
        Page<CallRecord> page = this.page(PagingUtils.convert(param), Wrappers.<CallRecord>lambdaQuery()
                .between(Objects.nonNull(param.getPeriod()), CallRecord::getCallTime, begin, end)
                .eq(Objects.nonNull(param.getCallType()), CallRecord::getCallType, param.getCallType().name())
                .like(StringUtils.isNotEmpty(param.getKeyword()), CallRecord::getDeviceName, param.getKeyword()));
        return PagingUtils.convert(page, CallRecordVo.class);
    }

    @Override
    public Pagination<ProcessingRecordsVo> getProcessingRecordsPage(CallRecordParam param) {
        Assert.isParameterError(StringUtils.isEmpty(param.getCallId()), "呼叫记录id为空");
        Page<ProcessingRecords> processingRecordsPage = processingRecordsMapper.selectPage(PagingUtils.convert(param), Wrappers
                .<ProcessingRecords>lambdaQuery()
                .eq(ProcessingRecords::getCallId, param.getCallId()));

        return PagingUtils.convert(processingRecordsPage, ProcessingRecordsVo.class);
    }

    @Override
    public Pagination<BedFileVo> getBedFilePage(CallRecordParam param) {
        Page<BedFile> bedFilePage = bedFileMapper.selectPage(PagingUtils.convert(param), Wrappers.<BedFile>lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getBedNum()), BedFile::getBed, param.getBedNum())
                .like(StringUtils.isNotEmpty(param.getCurrentPatient()), BedFile::getCurrentPatient, param.getCurrentPatient()));

        return PagingUtils.convert(bedFilePage, BedFileVo.class);
    }
}
