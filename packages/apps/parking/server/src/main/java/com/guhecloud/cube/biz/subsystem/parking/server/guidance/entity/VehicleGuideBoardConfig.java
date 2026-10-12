package com.guhecloud.cube.biz.subsystem.parking.server.guidance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 车辆引导牌配置表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("vehicle_guide_board_config")
public class VehicleGuideBoardConfig extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 现场图片文件id（多个，逗号分隔）
     */
    private String sceneImageIds;

    /**
     * 安装位置
     */
    private String installationLocation;

    /**
     * 关联车场id
     */
    private Long parkingLotId;

    /**
     * 备注
     */
    private String remark;
}

