package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway;

import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo.KnowledgeTypeTreeVO;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.query.KnowledgeTypeQuery;

import java.util.List;

public interface KnowledgeTypeQueryService {
    /**
     * 树形类型列表
     *
     * @param query 条件
     *
     * @return 树形列表
     */
    List<KnowledgeTypeTreeVO> getKnowledgeTypeTree(KnowledgeTypeQuery query);
}
