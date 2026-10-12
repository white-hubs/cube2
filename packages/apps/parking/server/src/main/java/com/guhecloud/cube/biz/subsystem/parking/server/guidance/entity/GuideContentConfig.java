package com.guhecloud.cube.biz.subsystem.parking.server.guidance.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 引导内容配置表
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("guide_content_config")
public class GuideContentConfig extends BasicEnhancedTenantEntity<Long> {

    /**
     * 主键
     */
    private Long id;

    /**
     * 关联引导牌配置id
     */
    private Long guideBoardConfigId;

    /**
     * 内容名称
     */
    private String contentName;

    /**
     * 优先级
     */
    private Integer priority;

    /**
     * 绑定字段
     */
    private String boundField;

    /**
     * 字体大小
     */
    private Integer fontSize;

    /**
     * 字体颜色
     */
    private String fontColor;

    /**
     * 屏幕编号
     */
    private String screenNumber;

    /**
     * 屏幕区域编号
     */
    private String screenAreaNumber;
}

