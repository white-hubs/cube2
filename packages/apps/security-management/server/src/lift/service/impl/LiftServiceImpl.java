package com.guhecloud.cube.srv.gen.securitymanagement.server.lift.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.dao.LiftRecordMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.param.LiftRecordParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.param.LiftRecordStatisticsParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.service.LiftService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.vo.LiftRecordVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.lift.vo.OperationsAnalysisVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LiftServiceImpl implements LiftService {

    private final LiftRecordMapper liftRecordMapper;

    @Override
    public Pagination<LiftRecordVo> getLiftRecord(LiftRecordParam param) {
        Page<LiftRecordVo> liftRecord = liftRecordMapper.getLiftRecord(PagingUtils.convert(param), param);
        return PagingUtils.convert(liftRecord);
    }

    @Override
    public Map<String, Object> getOperationsAnalysis(LiftRecordStatisticsParam param) {
        Map<String, Object> remap = new HashMap<>();
        String timeFlag = "";
        String[] split = param.getPeriod().split(",");
        var st = DateUtils.parse(split[0]);
        var et = DateUtils.parse(split[1]);
        List<String> xtitle = new ArrayList<>();

        if (param.getType().equals("TODAY")) {
            timeFlag = "'%H'";
            int between = (int) DateUtils.between(st, et, ChronoUnit.HOURS);
            for (int i = 0; i <= between; i++) {
                var offset = DateUtils.offset(st, ChronoUnit.HOURS, i);
                int hour = offset.getHour();
                if (hour < 10) {
                    xtitle.add("0" + hour);
                } else {
                    xtitle.add(String.valueOf(hour));
                }
            }
        } else if (param.getType().equals("THIS_WEEK")) {
            timeFlag = "'%Y-%m-%d'";
            int between = (int) DateUtils.between(st, et, ChronoUnit.DAYS);
            for (int i = 0; i < between; i++) {
                var offset = DateUtils.offset(st, ChronoUnit.DAYS, i);
                xtitle.add(DateUtils.format(offset, "yyyy-MM-dd"));
            }
        } else if (param.getType().equals("THIS_MONTH")) {
            timeFlag = "'%Y-%m-%d'";
            int between = (int) DateUtils.between(st, et, ChronoUnit.DAYS);
            for (int i = 0; i < between; i++) {
                var offset = DateUtils.offset(st, ChronoUnit.DAYS, i);
                xtitle.add(DateUtils.format(offset, "yyyy-MM-dd"));
            }
        } else if (param.getType().equals("THIS_YEAR")) {
            timeFlag = "'%m'";
            var between = DateUtils.between(st, et, ChronoUnit.MONTHS);

            for (int i = 0; i < between; i++) {
                var offset = DateUtils.offset(st, ChronoUnit.MONTHS, i);
                xtitle.add(DateUtils.format(offset, "MM"));
            }
        }
        List<OperationsAnalysisVo> operationsAnalysis = liftRecordMapper.getOperationsAnalysis(timeFlag, st, et);
        remap.put("operationsAnalysis", operationsAnalysis);
        remap.put("xtitle", xtitle);
        return remap;
    }
}
