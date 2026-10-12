package com.guhecloud.cube.biz.todo.server.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/**
 * 待办tag关联的任务类型(暂时不按租户自定义,内置初始化)
 *
 * @author lanchun
 * @date 2023/2/27 11:26
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class TodoTagReferBusinessType extends BasicEnhancedEntity<Long> {

    private Long id;

    /**
     * 标签编号
     */
    private String todoTagCode;

    /**
     * 业务类型
     */
    private String businessType;

}
