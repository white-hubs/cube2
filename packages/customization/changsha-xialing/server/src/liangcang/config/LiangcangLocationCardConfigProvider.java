package com.guhecloud.cube.customization.xialing.server.liangcang.config;

import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.MsgAuth;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.TenantDictionary;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.LiangcangLocationCardConfigDTO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;

/** 租户字典 variable → 货位卡 DTO。 */
@Component
@RequiredArgsConstructor
public class LiangcangLocationCardConfigProvider {

    private final DataDictionaryClient dataDictionaryClient;

    public LiangcangLocationCardConfigDTO resolve() {
        if (!StringUtils.hasText(TenantDictionary.ITEM_THIRD_CONFIG)
                || !StringUtils.hasText(TenantDictionary.CODE_LIANGCANG_LOCATION_CARD)) {
            return emptyConfig();
        }
        var p = new DataDictionaryaramByItemAndCodeParam();
        p.setItemCode(TenantDictionary.ITEM_THIRD_CONFIG);
        p.setCode(TenantDictionary.CODE_LIANGCANG_LOCATION_CARD);
        var dict = dataDictionaryClient.getDataDictionaryByItemAndCode(p);
        if (dict == null || !StringUtils.hasText(dict.getVariable())) {
            return emptyConfig();
        }
        try {
            var parsed = JsonUtils.deserialize(dict.getVariable().trim(), LiangcangLocationCardConfigDTO.class);
            if (parsed == null) {
                return emptyConfig();
            }
            if (parsed.getTemperatureProductCodes() == null) {
                parsed.setTemperatureProductCodes(new ArrayList<>());
            }
            if (parsed.getHumidityProductCodes() == null) {
                parsed.setHumidityProductCodes(new ArrayList<>());
            }
            if (parsed.getGasProductCodes() == null) {
                parsed.setGasProductCodes(new ArrayList<>());
            }
            return parsed;
        } catch (RuntimeException e) {
            var ex = new ServiceException(MsgAuth.TENANT_DICT_LIANGCANG_LOCATION_CARD_PARSE_FAILED);
            ex.initCause(e);
            throw ex;
        }
    }

    private static LiangcangLocationCardConfigDTO emptyConfig() {
        var c = new LiangcangLocationCardConfigDTO();
        c.setTemperatureProductCodes(new ArrayList<>());
        c.setHumidityProductCodes(new ArrayList<>());
        c.setGasProductCodes(new ArrayList<>());
        return c;
    }
}
