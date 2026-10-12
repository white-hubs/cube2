package com.guhecloud.cube.biz.emergency.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Appet
 * @Description 应急知识（技术）管理
 * @date 2024-01-26 10:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EmergencyKnowledge extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    //知识名称
    private String name;
    //知识备注
    private String remark;
    //文件地址(冗余：存放三方的文件地址)
    private String filePath;
    //
    private String fileIds;

}