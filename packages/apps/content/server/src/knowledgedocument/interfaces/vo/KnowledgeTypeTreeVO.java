package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo;

import com.guhecloud.platform.base.model.Tree;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class KnowledgeTypeTreeVO extends Tree.Simple<Long, KnowledgeTypeTreeVO> {
    private Long id;
    //类型名称
    private String name;
    // 描述
    private String description;
}
