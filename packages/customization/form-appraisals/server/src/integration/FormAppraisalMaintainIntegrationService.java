package com.guhecloud.cube.srv.gen.appraisals.server.integration;

import com.guhecloud.cube.srv.gen.maintain.client.MaintainStatisticsClient;
import com.guhecloud.cube.srv.gen.maintain.param.MaintainTaskTotalParam;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormAppraisalMaintainIntegrationService {
    private final MaintainStatisticsClient maintainStatisticsClient;

    /**
     * 获取超时任务数
     *
     * @param period      时间段
     * @param sourceRefId 来源引用 IDs
     *
     * @return 超时任务数
     */
    public BigDecimal getTimeoutTaskCount(Period period, String sourceRefId) {
        MaintainTaskTotalParam param = new MaintainTaskTotalParam();
        if (period != null && period.getBegin() != null && period.getEnd() != null) {
            param.setPeriod(DateUtils.format(period.getBegin()) + "," + DateUtils.format(period.getEnd()));
        }
        param.setOverTimeBool(true);
        if (StringUtils.isNotBlank(sourceRefId) && StringUtils.isNumeric(sourceRefId)) {
            // 将逗号分隔的字符串转换为 Long 类型列表
            List<Long> planIdList = StringUtils.split(sourceRefId, ",").stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            param.setPlanIdList(planIdList);
        }
        Long total = maintainStatisticsClient.maintainTaskTotalStat(param);
        return total == null ? BigDecimal.ZERO : BigDecimal.valueOf(total);
    }
}
