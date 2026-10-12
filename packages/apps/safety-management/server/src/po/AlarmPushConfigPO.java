package com.guhecloud.cube.srv.gen.safety.management.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("alarm_push_config")
public class AlarmPushConfigPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 报警类型编号
     */
    private String alarmTypeCode;

    /**
     * 报警级别编号
     */
    private String alarmLevelCode;

    /**
     * 超时的数据
     */
    private Boolean timeout;
}
