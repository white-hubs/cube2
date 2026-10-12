package com.guhecloud.cube.customization.xialing.server.drone.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/** 报警派飞结果通知状态。 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("drone_alarm_dispatch_notify_state")
public class DroneAlarmDispatchNotifyState extends BasicEnhancedTenantEntity<Long> {
    /** 主键ID。 */
    private Long id;
    /** 报警记录ID。 */
    private Long alarmRecordId;
    /** 报警通知人ID集合JSON。 */
    private String notifyUserIds;
    /** 是否已收到报警初始化指派完成消息。 */
    private Boolean assignedReceived;
    /** 关联派飞日志ID。 */
    private Long dispatchRecordId;
    /** 通知状态：0待发送，1发送中，2发送成功，3发送失败。 */
    private Integer notifyStatus;
    /** 通知发送时间。 */
    private LocalDateTime notifySendTime;
    /** 通知失败原因。 */
    private String notifyFailReason;
}
