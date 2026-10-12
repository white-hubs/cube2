package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeTypeStatVO {
    private Integer total;
    private List<KeyValueStringInteger> statList;
}
