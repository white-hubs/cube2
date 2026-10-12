package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * AI知识库配置内容实体对象
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiLibraryConfigVO {
    /**
     * 服务器基地址
     */
    private String libraryBaseUrl;
    /**
     * API Key
     */
    private String libraryApiKey;
    /**
     * 默认库id
     */
    private String libraryId;
    /**
     * 解析规则
     */
    private String libraryRule;
}
