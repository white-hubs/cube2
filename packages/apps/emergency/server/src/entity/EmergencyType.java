package com.guhecloud.cube.biz.emergency.server.entity;

import com.guhecloud.platform.database.annotation.Unique;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 预案类型数据模型
 *
 * @author lanchun
 * @date 2022/9/2 10:18
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class EmergencyType extends BasicEnhancedEntity<Long> {

    private Long id;

    /**
     * 编号
     */
    private String code;

    /**
     * 名称
     */
    private String name;

    /**
     * 应用编号
     */
    @Unique(composites = "deleted", message = "应用下已存在预案类型配置")
    private String applicationCode;

    /**
     * 应用名称
     */
    private String applicationName;

}
