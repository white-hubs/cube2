package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class KnowledgeTypeQuery extends Paging {
    private String name;
}
