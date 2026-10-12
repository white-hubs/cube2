package com.guhecloud.cube.srv.gen.conferencemanage.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/7 15:42
 * @Description: 字段配置
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("field_configuration")
public class FieldConfiguration extends BasicEnhancedTenantEntity<Long> {

    private static final long serialVersionUID = -6076058890622983408L;
    /**
     *
     */
    private Long id;

    /**
     * 字段编码
     */
    private String fieldCode;

    /**
     * 字段名称
     */
    private String fieldName;

    /**
     * 是否必输
     */
    private Boolean required;

    /**
     * 是否隐藏
     */
    private Boolean hidden;

}