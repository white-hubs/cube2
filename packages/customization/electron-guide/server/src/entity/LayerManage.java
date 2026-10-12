package com.guhecloud.cube.srv.electron.guide.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @Author: lijie
 * @CreateTime: 2024-01-05  17:41
 * @Description:
 * @Version: 1.0
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@TableName("layer_manage")
public class LayerManage extends BasicEnhancedTenantEntity<Long> {
    private Long id;
    /**
     * 图层名称
     */
    private String layerName;
    /**
     * 图层附件
     */
    private String layerFile;
    /**
     * 状态
     */
    private String state;
    /**
     * 区域主键
     */
    private Long areaId;
    /**
     * 区域序列号
     */
    private String areaSequence;
    /**
     * 西南角
     */
    private String southwest;
    /**
     * 东北角
     */
    private String northeast;
    /**
     * 中心点
     */
    private String center;
}
