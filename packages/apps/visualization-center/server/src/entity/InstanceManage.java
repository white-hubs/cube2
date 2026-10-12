package com.guhecloud.cube.base.visualization.server.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

/**
 * 发布实例;
 *
 * @author :lijie
 * @date : 2023-8-21
 */
@Data
@TableName("instance_manage")
public class InstanceManage extends BasicEnhancedTenantEntity<Long> {
    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 项目id
     */
    private Long projectId;
    /**
     * 实例名称
     */
    private String instanceName;
    /**
     * 链接地址
     */
    private String path;
    /**
     * 状态
     */
    private String status;
}
