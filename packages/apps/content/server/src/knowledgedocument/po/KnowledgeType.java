package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantTreeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 知识文档类型
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KnowledgeType extends BasicEnhancedTenantTreeEntity<Long> {
    private Long id;
    private String name;
    private String description;
}
