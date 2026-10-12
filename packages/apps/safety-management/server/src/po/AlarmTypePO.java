package com.guhecloud.cube.srv.gen.safety.management.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
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
@TableName("alarm_type")
public class AlarmTypePO extends BasicEnhancedEntity<Long> {

    private Long id;

    /**
     * 报警分类编号
     */
    private String alarmTypeCode;

    /**
     * 报警分类名称
     */
    private String alarmTypeName;

    /**
     * 应用编号
     */
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;

    /**
     * 备注说明
     */
    private String remark;
}
