package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgeBatchVO {
    private Integer errorCount = 0;
    private Integer successCount = 0;
    private List<String> errorList = new ArrayList<>();
}
