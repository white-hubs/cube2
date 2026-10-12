package com.guhecloud.cube.srv.gen.safety.management.notify.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("alarm_notice")
public class AlarmNoticePO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 报警级别编号
     */
    private String alarmLevelCode;

    /**
     * 业务类型
     */
    private String businessType;

    /**
     * 条件值
     */
    private Integer value;

    /**
     * 场景编号
     */
    private String sceneCode;

    /**
     * 启用状态
     */
    private Boolean enabled;
}
