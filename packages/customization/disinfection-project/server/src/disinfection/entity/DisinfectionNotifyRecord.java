package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionNotifySceneCodeEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@TableName("disinfection_notify_record")
@Data
public class DisinfectionNotifyRecord extends BasicEnhancedTenantEntity<Long> {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    private Long templateId;
    private DisinfectionNotifySceneCodeEnum sceneCode;
    private Long businessId;
    private String notifyUsers;
}
