package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.commandgateway.command;

import lombok.Data;

@Data
public class KnowledgeTypeCommand {
    private Long id;
    private Long parentId;
    private String name;
    private String description;
}
