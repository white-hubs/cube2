package com.guhecloud.cube.biz.work.center.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 工作tag关联的任务类型(暂时不按租户自定义,内置初始化)
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class WorkTagReferBusinessType extends BasicEnhancedEntity<Long> {

    private Long id;

    /**
     * 标签编号
     */
    private String workTagCode;

    /**
     * 业务类型
     */
    private String businessType;

}
