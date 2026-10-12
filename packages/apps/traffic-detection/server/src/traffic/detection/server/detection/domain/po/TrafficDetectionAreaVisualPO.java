package com.guhecloud.cube.biz.traffic.detection.server.detection.domain.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 人流区域视角信息
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@TableName("traffic_detection_area_visual")
public class TrafficDetectionAreaVisualPO extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 中心位置
     */
    private String centerAddress;

    /**
     * 视角参数
     */
    private String visualConfig;

    /**
     * 隐藏楼栋
     */
    private String hiddenBuilding;

}
