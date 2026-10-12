package com.guhecloud.cube.srv.gen.safety.management.dto.request.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Set;

@Data
public class AlarmRecordQuery extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 市场编号
     */
    private Integer marketTenant;

    /**
     * 报警类型编码
     */
    private String alarmTypeCode;

    /**
     * 报警时间范围
     */
    private Period period;

    /**
     * 处理结果
     */
    private String alarmHandlerResult;

    /**
     * 报警状态
     */
    private String alarmStatus;

    /**
     * 排除的报警状态
     */
    private String filterAlarmStatus;

    /**
     * 报警超时状态
     */
    private String alarmHandlerTimeoutStatus;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 事件分类编号
     */
    private String alarmEventTypeCode;

    /**
     * 事件分类编号集合
     */
    private Set<String> alarmEventTypeCodes;

    /**
     * 不包含的事件分类编号集合
     */
    private Set<String> filterAlarmEventTypeCodes;

    /**
     * 报警级别主键
     */
    private Long alarmLevelId;

    /**
     * 再次确认结果
     */
    private String assistConfirmResult;
}
