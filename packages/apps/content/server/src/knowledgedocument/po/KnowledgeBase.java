package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.po;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 知识文档
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class KnowledgeBase extends BasicEnhancedTenantEntity<Long> {
    /**
     * 主键
     */
    private Long id;

    /**
     * 文档名称
     */
    private String name;

    /**
     * 文档类型
     */
    private Long typeId;

    /**
     * 所属部门主键
     */
    private Long deptId;

    /**
     * 所属产品id
     */
    private Long productId;

    /**
     * AI知识库id
     */
    private String aiLibraryId;

    /**
     * AI文档id
     */
    private String aiDocumentId;
}
