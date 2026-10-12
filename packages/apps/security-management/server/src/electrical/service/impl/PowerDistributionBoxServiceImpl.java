package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.dao.PowerDistributionBoxMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.entity.PowerDistributionBox;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.param.PowerConsumptionParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.param.RecordTimeParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.service.PowerDistributionBoxService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.DistributionsExcelVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.DistributionsVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.ElectricityVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo.RecordTimeVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PowerDistributionBoxServiceImpl extends ServiceImpl<PowerDistributionBoxMapper, PowerDistributionBox> implements PowerDistributionBoxService {

    private final PowerDistributionBoxMapper powerDistributionBoxMapper;

    @Value("${projct.flag}")
    private String flag;

    @Override
    public Pagination<RecordTimeVo> getElectricityTimeRecord(RecordTimeParam param) {
        Map<String, LocalDateTime> period = param.getDate();
        Page<RecordTimeVo> electricityTimeRecord = powerDistributionBoxMapper.getElectricityTimeRecord(PagingUtils.convert(param), period.get("beginTime"), period.get("endTime"));
        PagingUtils.convert(electricityTimeRecord);
        return PagingUtils.convert(electricityTimeRecord);
    }

    @Override
    public Pagination<RecordTimeVo> getDistributionTimeRecord(RecordTimeParam param) {
        Map<String, LocalDateTime> period = param.getDate();
        Page<RecordTimeVo> electricityTimeRecord = powerDistributionBoxMapper.getDistributionTimeRecord(PagingUtils.convert(param), period.get("beginTime"), period.get("endTime"));
        PagingUtils.convert(electricityTimeRecord);
        return PagingUtils.convert(electricityTimeRecord);
    }

    @Override
    public PowerDistributionBox getLastOne(Long deviceId) {

        return powerDistributionBoxMapper.getLastOne(deviceId);
    }

    @Override
    public List<ElectricityVo> getElectricityTimeRecordDetails(RecordTimeParam recordTimeParam) {
        Map<String, LocalDateTime> period = recordTimeParam.getDate();
        List<ElectricityVo> electricityTimeRecordDetails = powerDistributionBoxMapper.getElectricityTimeRecordDetails(period.get("beginTime"), period.get("endTime"));
        return electricityTimeRecordDetails;
    }

    @Override
    public List<DistributionsVo> getDistributionsTimeRecordDetails(RecordTimeParam recordTimeParam) {
        Map<String, LocalDateTime> period = recordTimeParam.getDate();
        List<DistributionsVo> distributionsTimeRecordDetails = powerDistributionBoxMapper.getDistributionsTimeRecordDetails(period.get("beginTime"), period.get("endTime"), recordTimeParam.getDeviceId());
        return distributionsTimeRecordDetails;
    }

    @Override
    public List<DistributionsExcelVo> getDistributionsTimeRecordExcel(RecordTimeParam recordTimeParam) {
        Map<String, LocalDateTime> period = recordTimeParam.getDate();
        List<DistributionsExcelVo> distributionsTimeRecordDetails = powerDistributionBoxMapper.getDistributionsTimeRecordExcel(period.get("beginTime"), period.get("endTime"), recordTimeParam.getDeviceId());
        return distributionsTimeRecordDetails;
    }

    @Override
    public BigDecimal getPowerConsumption(PowerConsumptionParam param) {
        String beginTime = "";
        String endTime = "";
        if (StringUtils.isNotEmpty(param.getPeriod())) {
            String[] split = param.getPeriod().split(",");
            beginTime = split[0];
            endTime = split[1];
        } else {
            var date = LocalDateTime.now();
            if (param.getType().equals("TODAY")) {
                var dateTime = DateUtils.beginOfDay(date);
                beginTime = DateUtils.format(dateTime, "yyyy-MM-dd HH:mm:ss");
                var dateTime1 = DateUtils.endOfDay(date);
                endTime = DateUtils.format(dateTime1, "yyyy-MM-dd HH:mm:ss");
            } else if (param.getType().equals("THIS_WEEK")) {
                var dateTime = DateUtils.beginOfWeek(date);
                beginTime = DateUtils.format(dateTime, "yyyy-MM-dd HH:mm:ss");
                var dateTime1 = DateUtils.endOfWeek(date);
                endTime = DateUtils.format(dateTime1, "yyyy-MM-dd HH:mm:ss");
            } else if (param.getType().equals("THIS_MONTH")) {
                var dateTime = DateUtils.beginOfMonth(date);
                beginTime = DateUtils.format(dateTime, "yyyy-MM-dd HH:mm:ss");
                var dateTime1 = DateUtils.endOfMonth(date);
                endTime = DateUtils.format(dateTime1, "yyyy-MM-dd HH:mm:ss");
            } else if (param.getType().equals("RECENT_THREE_MONTH")) {
                var offset = DateUtils.offset(date, ChronoUnit.MONTHS, -3);
                beginTime = DateUtils.format(offset, "yyyy-MM-dd HH:mm:ss");
                endTime = DateUtils.format(date, "yyyy-MM-dd HH:mm:ss");
            } else if (param.getType().equals("RECENT_HALF_YEAR")) {
                var offset = DateUtils.offset(date, ChronoUnit.MONTHS, -6);
                beginTime = DateUtils.format(offset, "yyyy-MM-dd HH:mm:ss");
                endTime = DateUtils.format(date, "yyyy-MM-dd HH:mm:ss");
            } else if (param.getType().equals("THIS_YEAR")) {
                var dateTime = DateUtils.beginOfYear(date);
                beginTime = DateUtils.format(dateTime, "yyyy-MM-dd HH:mm:ss");
                var dateTime1 = DateUtils.endOfYear(date);
                endTime = DateUtils.format(dateTime1, "yyyy-MM-dd HH:mm:ss");
            }
        }

        BigDecimal bigDecimal = powerConsumption(beginTime, endTime);

        return bigDecimal.setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal powerConsumption(String beginTimeStr, String endTimeStr) {
        var beginTime = DateUtils.parse(beginTimeStr);
        var endTime = DateUtils.parse(endTimeStr);
        if (flag.equals("cnp")) {
            return powerDistributionBoxMapper.getCnpPowerConsumption(beginTime, endTime);
        } else {
            return powerDistributionBoxMapper.getPowerConsumption(beginTime, endTime);
        }
    }
}
