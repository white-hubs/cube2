package com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.querygateway;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.srv.gen.content.management.server.knowledgedocument.interfaces.vo.AiLibraryConfigVO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.StringUtils;

public interface AiLibraryQueryService {
    /**
     * AI知识库 基本信息配置 ITEM_CODE
     */
    String libraryItemCode = "THIRD_SERVICE";
    /**
     * AI知识库 基本信息配置 CODE
     */
    String libraryCode = "AI_AGENT";

    /**
     * 获取AI知识文档配置
     *
     * @return 配置对象
     */
    static AiLibraryConfigVO getAiLibraryConfig() {
        AiLibraryConfigVO result = new AiLibraryConfigVO();
        DataDictionaryClient dictionaryClient = ApplicationContextHolder.getBean(DataDictionaryClient.class);
        DataDictionaryaramByItemAndCodeParam dictionaryaram = new DataDictionaryaramByItemAndCodeParam();
        dictionaryaram.setItemCode(libraryItemCode);
        dictionaryaram.setCode(libraryCode);
        DataDictionaryDTO dictionaryDTO = dictionaryClient.getDataDictionaryByItemAndCode(dictionaryaram);
        if (dictionaryDTO == null || StringUtils.isBlank(dictionaryDTO.getVariable())) {
            return result;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode variableNode;
        try {
            variableNode = objectMapper.readTree(dictionaryDTO.getVariable());
        } catch (Exception e) {
            return result;
        }
        if (variableNode != null) {
            result.setLibraryBaseUrl(variableNode.get("libraryBaseUrl") == null ? "" : variableNode.get("libraryBaseUrl").asText());
            result.setLibraryApiKey(variableNode.get("libraryApiKey") == null ? "" : variableNode.get("libraryApiKey").asText());
            result.setLibraryId(variableNode.get("libraryId") == null ? "" : variableNode.get("libraryId").asText());
            if (variableNode.get("libraryRule") != null) {
                String rule = "";
                try {
                    rule = objectMapper.writeValueAsString(variableNode.get("libraryRule"));
                } catch (Exception e) {
                    return result;
                }
                result.setLibraryRule(rule);
            }
        }
        return result;
    }

}
