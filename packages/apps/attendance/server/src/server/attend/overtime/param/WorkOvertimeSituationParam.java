package com.guhecloud.cube.srv.gen.attendance.management.server.attend.overtime.param;

import com.alibaba.fastjson2.JSONObject;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author appet
 * @date 2022/11/21
 * @description 加班明细表
 */
@Data
public class WorkOvertimeSituationParam extends WorkOvertimeDetailsParam {
    private Long id;
    /**
     * 申请人ID
     */
    private Long applicantId;
    /**
     * 申请时间
     */
    private LocalDateTime applyTime;
    /**
     * 申请处理完成时间
     */
    private LocalDateTime applyDoneTime;
    /**
     * 考勤组ID
     */
    private Long attendanceGroupId;
    /**
     * 加班规则ID
     */
    private Long workRuleId;
    /**
     * 批量加班详情
     */
    private List<WorkOvertimeDetailsParam> overtimeDetails;

    /**
     * 表单数据校验
     */
    public static void flowVerification(String fromString) {
        WorkOvertimeSituationParam fomrData = JSONObject.parseObject(fromString, WorkOvertimeSituationParam.class);
        Assert.parameterIsNull(fomrData, "未绑定流程配置");
        if (ObjectUtils.isEmpty(fomrData.getOvertimeDetails())) {
            Assert.parameterIsNull(fomrData.getWorkType(), "流程配置未配置加班类型");
            Assert.parameterIsNull(fomrData.getWorkPeriod(), "流程配置未配置加班时段");
        } else {
            fomrData.getOvertimeDetails().forEach(item -> {
                Assert.parameterIsNull(item, "流程配置未配置加班信息");
                Assert.parameterIsNull(item.getWorkType(), "流程配置未配置加班类型");
                Assert.parameterIsNull(item.getWorkPeriod(), "流程配置未配置加班时段");
            });
        }
    }

    /**
     * 数据组装
     */
    public static WorkOvertimeSituationParam getWorkOvertimeSituationParam(String fromString) {
        WorkOvertimeSituationParam fomrData = JSONObject.parseObject(fromString, WorkOvertimeSituationParam.class);
        if (ObjectUtils.isEmpty(fomrData.getOvertimeDetails())) {
            List<WorkOvertimeDetailsParam> detailsParams = new ArrayList<>();
            detailsParams.add(WorkOvertimeDetailsParam.builder()
                                      .workType(fomrData.getWorkType())
                                      .startTime(fomrData.getStartTime())
                                      .endTime(fomrData.getEndTime())
                                      .workDuration(fomrData.getWorkDuration())
                                      .compensate(fomrData.getCompensate())
                                      .reason(fomrData.getReason())
                                      .workPeriod(fomrData.getWorkPeriod())
                                      .workDate(fomrData.getWorkDate())
                                      .build());
            fomrData.setOvertimeDetails(detailsParams);
        }
        return fomrData;
    }

}