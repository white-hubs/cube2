package com.guhecloud.cube.base.message2.server.base.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * 变量映射
 *
 * @author lanchun
 * @date 2025/1/21 19:48
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class VarMapping extends BasicEnhancedEntity<Long> {

    private Long id;

    /**
     * 映射的json
     */
    private String mappingJson;
}
