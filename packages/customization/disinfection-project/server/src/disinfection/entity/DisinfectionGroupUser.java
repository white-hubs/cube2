package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 消杀小组成员表实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("disinfection_group_user")
public class DisinfectionGroupUser extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 小组ID
     */
    @TableField("group_id")
    private Long groupId;

    /**
     * 组员ID
     */
    @TableField("user_id")
    private Long userId;

}