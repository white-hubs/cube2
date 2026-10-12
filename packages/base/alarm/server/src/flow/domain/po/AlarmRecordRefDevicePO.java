package com.guhecloud.cube.base.alarm.server.flow.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("alarm_record_ref_device")
public class AlarmRecordRefDevicePO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 报警记录id
     */
    private Long alarmRecordId;

    /**
     * 设备id
     */
    private Long deviceId;
}
