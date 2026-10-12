package com.guhecloud.subsystem.requester.sdk.param.drone;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 无人机报警派飞日志分页查询参数。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class DroneAlarmDispatchRecordPageParam extends Paging {
    /** 报警记录ID。 */
    private Long alarmRecordId;
    /** 报警对象ID。 */
    private Long alarmTargetId;
    /** 报警级别ID。 */
    private Long alarmLevelId;
    /** 报警类型ID。 */
    private Long alarmTypeId;
    /** 派飞状态：1成功，2失败。 */
    private Integer dispatchStatus;
    /** 关键字，匹配报警编号、对象名称或任务名称。 */
    private String keyword;
}
