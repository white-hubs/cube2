package com.guhecloud.cube.base.alarm.server.sat.entity;

import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SatAlarmBusinessUrl extends BasicEnhancedEntity<Long> {

    private Long id;

    /**
     * 业务类型
     */
    @Unique(composites = { "terminal", "deleted" }, message = "该业务类型在该访问端下已存在路径配置")
    private String businessType;

    /**
     * 业务类型名称
     */
    private String businessTypeName;

    /**
     * 访问端
     */
    private String terminal;

    /**
     * 跳转地址
     */
    private String url;

    /**
     * 应用编号
     */
    private String applicationCode;
}
