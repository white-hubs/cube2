package com.guhecloud.cube.base.device.center.core.model;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTreeEntity;
import lombok.Data;

/**
 * 标签定义
 */
@Data
@TableName("center_biz_tag")
public class BizTag extends BasicEnhancedTreeEntity<Long> {
    private Long id;

    /**
     * 标签code
     */
    private String tagCode;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 业务含义描述
     */
    private String description;

    /**
     * 启用状态
     */
    private String enableStatus;

}