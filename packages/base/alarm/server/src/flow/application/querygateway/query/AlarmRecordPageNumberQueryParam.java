package com.guhecloud.cube.base.alarm.server.flow.application.querygateway.query;

import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmHandlerResultEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmHandlerTimeoutStatusEnum;
import com.guhecloud.cube.base.alarm.server.flow.domain.enumeration.AlarmStatusEnum;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

/**
 * 报警记录查询参数
 *
 * @author lanchun
 * @date 2022/6/7 10:39
 */
@Data
public class AlarmRecordPageNumberQueryParam {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 报警级别主键
     */
    @Min(value = 1, message = "报警级别主键不能小于1")
    private Long alarmLevelId;

    /**
     * 报警类型主键
     */
    @Min(value = 1, message = "报警类型主键不能小于1")
    private Long alarmTypeId;
    /**
     * 报警类型编码
     */
    private String alarmTypeCode;
    /**
     * 报警事件
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String alarmEvent;

    /**
     * 报警事件名称
     */
    private String alarmEventName;

    /**
     * 报警对象类型主键
     */
    private String alarmTargetTypeId;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 报警区域
     */
    private List<String> areaIds;

    /**
     * 处理结果
     */
    private AlarmHandlerResultEnum alarmHandlerResult;

    /**
     * 报警状态
     */
    private AlarmStatusEnum alarmStatus;

    /**
     * 报警超时状态
     */
    private AlarmHandlerTimeoutStatusEnum alarmHandlerTimeoutStatus;

    /**
     * 报警时间范围
     */
    private Period period;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 设备主键集合
     */
    private List<Long> deviceIds;

    /**
     * 区域
     */
    private String area;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 报警记录主键
     */
    @NotNull(message = "报警记录主键不能小于1")
    @Min(value = 1, message = "报警记录主键不能小于1")
    private Long id;

    /**
     * 页大小不能为空
     */
    @NotNull(message = "页大小不能为空")
    @Min(value = 1, message = "页大小不能小于1")
    private Integer pageSize;
}
