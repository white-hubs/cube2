package com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.facade;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.guhecloud.cube.base.dictionary.adapter.DataDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class KeyPersonSensitiveConfigService {
    private static final String ITEM_CODE = "SERVICE_CONFIGURATION";
    private static final String CODE = "SECURITY_KEY_PERSONNEL_DESENSITIZE_CONFIG";

    private final DataDictionaryAdapter dataDictionaryAdapter;

    public Config getConfig() {
        try {
            DataDictionaryaramByItemAndCodeParam param = new DataDictionaryaramByItemAndCodeParam();
            param.setItemCode(ITEM_CODE);
            param.setCode(CODE);
            DataDictionaryDTO dictionary = dataDictionaryAdapter.getDataDictionaryByItemAndCode(param);
            if (dictionary == null || StringUtils.isBlank(dictionary.getVariable())) {
                return new Config();
            }
            Config config = JsonUtils.deserialize(dictionary.getVariable(), Config.class);
            return config == null ? new Config() : config;
        } catch (Exception ex) {
            log.warn("Failed to load key personnel desensitize config", ex);
            return new Config();
        }
    }

    @Data
    public static class Config {
        @JsonProperty("phone_desensitize_enabled")
        private Boolean phoneDesensitizeEnabled;
        @JsonProperty("id_card_desensitize_enabled")
        private Boolean idCardDesensitizeEnabled;

        public boolean phoneEnabled() {
            return Boolean.TRUE.equals(phoneDesensitizeEnabled);
        }

        public boolean idCardEnabled() {
            return Boolean.TRUE.equals(idCardDesensitizeEnabled);
        }
    }
}
