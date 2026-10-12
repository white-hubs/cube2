package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionDataTypeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消杀业务分类表实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("disinfection_data_type")
public class DisinfectionDataType extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 业务类型
     */
    @TableField("data_type")
    private DisinfectionDataTypeEnum dataType;

    /**
     * 业务名称
     */
    @TableField("data_name")
    private String dataName;

    /**
     * 类型图标
     */
    private Long icon;

    /**
     * 备注信息
     */
    private String remark;
}