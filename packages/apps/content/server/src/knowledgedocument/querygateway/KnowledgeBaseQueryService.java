package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway;

import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo.KnowledgeBaseVO;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo.KnowledgeTypeStatVO;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway.query.KnowledgeBaseQuery;
import com.guhecloud.platform.base.model.Pagination;

public interface KnowledgeBaseQueryService {
    KnowledgeBaseVO getDetail(KnowledgeBaseQuery query);

    Pagination<KnowledgeBaseVO> getPage(KnowledgeBaseQuery query);

    KnowledgeTypeStatVO statKnowledgeByType();
}
