package com.guhecloud.cube.srv.gen.appraisals.server.integration;

import com.guhecloud.cube.srv.gen.maintain.client.InspectRecordClient;
import com.guhecloud.cube.srv.gen.maintain.client.InspectTaskClient;
import com.guhecloud.cube.srv.gen.maintain.dto.TotalNumberRateDTO;
import com.guhecloud.cube.srv.gen.maintain.param.InspectionCommonParam;
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
public class FormAppraisalInspectIntegrationService {
    private final InspectRecordClient inspectRecordClient;
    private final InspectTaskClient inspectTaskClient;

    /**
     * 获取考核周期内的任务总数
     *
     * @param period      考核周期
     * @param sourceRefId 源引用 ID
     *
     * @return 任务数量
     */
    public BigDecimal getTaskCount(Period period, String sourceRefId) {
        // 构建巡检通用查询参数
        InspectionCommonParam param = new InspectionCommonParam();
        param.setStatus("OVER");
        // 设置考核周期
        if (period != null && period.getBegin() != null && period.getEnd() != null) {
            param.setPeriod(DateUtils.format(period.getBegin()) + "," + DateUtils.format(period.getEnd()));
        }
        if (StringUtils.isNotBlank(sourceRefId) && StringUtils.isNumeric(sourceRefId)) {
            // 将逗号分隔的字符串转换为 Long 类型列表
            List<Long> planIdList = StringUtils.split(sourceRefId, ",").stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            param.setPlanIdList(planIdList);
        }
        // 调用巡检客户端接口获取完
        Long total = inspectTaskClient.postTaskTotal(param);
        // 如果为null返回 0；否则返回总数
        return total == null ? BigDecimal.ZERO : BigDecimal.valueOf(total);
    }

    /**
     * 获取考核周期内的超时任务数量
     *
     * @param period      考核周期
     * @param sourceRefId 源引用 IDs
     *
     * @return 超时任务数量
     */
    public BigDecimal getTimeoutTaskCount(Period period, String sourceRefId) {
        // 构建巡检通用查询参数
        InspectionCommonParam param = new InspectionCommonParam();
        // 设置考核周期
        if (period != null && period.getBegin() != null && period.getEnd() != null) {
            param.setPeriod(DateUtils.format(period.getBegin()) + "," + DateUtils.format(period.getEnd()));
        }
        if (StringUtils.isNotBlank(sourceRefId) && StringUtils.isNumeric(sourceRefId)) {
            // 将逗号分隔的字符串转换为 Long 类型列表
            List<Long> planIdList = StringUtils.split(sourceRefId, ",").stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            param.setPlanIdList(planIdList);
        }
        param.setOverTimeBool(true);
        // 调用巡检记录客户端接口获取完成率 DTO（包含总数和完成数）
        Long total = inspectTaskClient.postTaskTotal(param);

        // 返回超时任务数
        return total == null ? BigDecimal.ZERO : BigDecimal.valueOf(total);
    }

    /**
     * 获取考核周期内的异常项数量
     *
     * @param period      考核周期
     * @param sourceRefId 源引用 IDs
     *
     * @return 异常项数量
     */
    public BigDecimal getAbnormalItemCount(Period period, String sourceRefId) {
        // 构建巡检记录项查询参数
        InspectionCommonParam param = new InspectionCommonParam();
        // 设置考核周期
        if (period != null && period.getBegin() != null && period.getEnd() != null) {
            param.setPeriod(DateUtils.format(period.getBegin()) + "," + DateUtils.format(period.getEnd()));
        }
        if (StringUtils.isNotBlank(sourceRefId) && StringUtils.isNumeric(sourceRefId)) {
            // 将逗号分隔的字符串转换为 Long 类型列表
            List<Long> planIdList = StringUtils.split(sourceRefId, ",").stream()
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            param.setPlanIdList(planIdList);
        }
        // 调用巡检记录客户端接口获取巡检记录项统计数据列表
        TotalNumberRateDTO dto = inspectRecordClient.postRecordErrRate(param);
        // 如果列表为 null 或空，返回 0
        if (dto == null || dto.getNumber() == null) {
            return BigDecimal.ZERO;
        }
        // 返回异常项数
        return BigDecimal.valueOf(dto.getNumber());
    }
}
