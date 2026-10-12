package com.guhecloud.cube.customized.guhecloudwebsite.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.guhecloud.platform.database.entity.BasicTenantEntity;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 网站发布配置实体
 * 存储静态站点发布到对象存储的配置信息
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SysSitePublishConfig extends BasicEnhancedTenantEntity<Long> {

    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /** 对象存储服务商，如 qiniu */
    private String provider;

    /** 对象存储配置JSON，包含 accessKey/secretKey/bucket/zone/domain */
    private String providerConfig;

}
