package com.guhecloud.cube.biz.ouaa.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author bq
 * @date 2022/11/08
 * @description 职位管理
 */
@Data
@TableName("appointment")
@EqualsAndHashCode(callSuper = false)
public class Appointment extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 描述
     */
    private String remark;

    /**
     * 启停用状态
     */
    private Boolean enable;

}