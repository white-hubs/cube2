package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway;

import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.command.KnowledgeTypeCommand;

public interface KnowledgeTypeCommandService {
    Long insert(KnowledgeTypeCommand command);

    Long update(KnowledgeTypeCommand command);

    Boolean remove(Long id);
}
