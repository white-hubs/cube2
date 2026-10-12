package com.guhecloud.cube.srv.gen.attendance.management.server.attend.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramParam;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 模块应用配置处理
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttModuleConfigCheck {

    private static final String ATT_MODULE_ITEM_CODE = "APP_BUSINESS_CONFIG";
    private static final String ATT_MODULE_CODE = "ATTENDANCE_MANAGEMENT_ABILITY";

    private final DataDictionaryClient dataDictionaryClient;

    public static boolean checkModuleHave(String code, DataDictionaryDTO config) {
        if (config == null) {
            return false;
        }
        String variable = config.getVariable();
        if (StringUtils.isBlank(variable)) {
            return false;
        }
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.createObjectNode();
        try {
            jsonNode = mapper.readTree(variable);
        } catch (Exception e) {
            log.error("转换模块配置出错：{}", e.getMessage());
        }

        return jsonNode.get(code) != null && jsonNode.get(code).get("value").booleanValue();
    }

    public boolean checkModuleHave(String code) {
        DataDictionaryaramParam dataDictionaryaramParam = new DataDictionaryaramParam();
        dataDictionaryaramParam.setItemCode(ATT_MODULE_ITEM_CODE);
        dataDictionaryaramParam.setCode(ATT_MODULE_CODE);
        List<DataDictionaryDTO> modelConfigList = dataDictionaryClient.getDataDictionaryByItem(dataDictionaryaramParam);
        if (CollectionUtils.isEmpty(modelConfigList)) {
            return false;
        }
        return AttModuleConfigCheck.checkModuleHave(code, modelConfigList.getFirst());
    }
}
