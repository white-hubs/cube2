package com.guhecloud.cube.srv.gen.loose.works.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 工程类型
 *
 * @TableName project_type
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@TableName("project_type")
public class ProjectTypePO extends BasicEnhancedTenantEntity<Long> implements Serializable {
    private static final long serialVersionUID = -7277715899559744361L;
    /**
     * id
     */
    private Long id;

    /**
     * 类型名称
     */
    private String name;

}