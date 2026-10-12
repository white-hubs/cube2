package com.guhecloud.cube.biz.vmonitor.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 多路视频摄像机数据模型
 *
 * @author lanchun
 * @date 2021/12/6 19:47
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MultipleVideoCollectCamera extends BasicEnhancedTenantEntity<Long> {

    private Long id;

    /**
     * 多路视频收藏主键
     */
    private Long multipleVideoCollectId;

    /**
     * 用户主键
     */
    private Long userId;

    /**
     * 摄像机设备主键
     */
    private Long deviceId;

    /**
     * 序号
     */
    private Integer sort;

    /**
     * 收藏来源
     */
    private String source;
}