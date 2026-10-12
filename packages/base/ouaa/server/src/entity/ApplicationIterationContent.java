package com.guhecloud.cube.biz.ouaa.server.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.guhecloud.cube.biz.ouaa.server.enumeration.IterationCategoryEnum;
import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/8/6 09:07
 * @Description:
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@TableName("application_iteration_content")
public class ApplicationIterationContent extends BasicEnhancedEntity<Long> {
    /**
     * 主键
     */
    private Long id;

    /**
     * 应用迭代记录ID
     */
    private Long iterationId;
    /**
     * 迭代类别
     */
    private IterationCategoryEnum category;
    /**
     * 迭代内容
     */
    private String content;
}
