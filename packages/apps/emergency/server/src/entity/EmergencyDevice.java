package com.guhecloud.cube.biz.emergency.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Appet
 * @Description 应急设备管理
 * @date 2024-01-26 10:08:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EmergencyDevice extends BasicEnhancedTenantEntity<Long> {

    //
    private Long id;
    //设备名称
    private String name;
    //设备资料名称（冗余：存放第三方的文件名称）
    private String fileNames;
    //设备资料路径（冗余：存放第三方的文件路径）
    private String filePaths;
    //设备资料(文件IDS)
    private String fileIds;
    //备注
    private String remark;
    //设备数量
    private Integer deviceNum;
    //使用数量
    private Integer deviceUsedNum;
    //损失数量
    private Integer deviceLossNum;

}