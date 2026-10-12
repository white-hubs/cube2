package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway;

import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.command.KnowledgeBaseCommand;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo.KnowledgeBatchVO;

import java.util.List;

/**
 * 知识库
 */
public interface KnowledgeBaseCommandService {

    Long addCommand(KnowledgeBaseCommand command);

    Long editCommand(KnowledgeBaseCommand command);

    boolean removeBatch(KnowledgeBaseCommand command);

    KnowledgeBatchVO addBatch(List<KnowledgeBaseCommand> batchCommand);
}
