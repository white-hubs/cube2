package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class KnowledgeBaseQuery extends Paging {
    private Long id;
    private String name;
    private Long typeId;
    private Long deptId;
    private Long productId;
}
