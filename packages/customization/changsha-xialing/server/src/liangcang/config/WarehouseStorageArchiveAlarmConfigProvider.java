package com.guhecloud.cube.customization.xialing.server.liangcang.config;

import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.customization.xialing.server.liangcang.constants.TenantDictionary;
import com.guhecloud.cube.customization.xialing.server.liangcang.dto.WarehouseStorageArchiveAlarmConfigDTO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/** 储量档案报警配置提供者。 */
@Slf4j
@Component
@RequiredArgsConstructor
public class WarehouseStorageArchiveAlarmConfigProvider {

    private final DataDictionaryClient dataDictionaryClient;

    public WarehouseStorageArchiveAlarmConfigDTO load() {
        var query = new DataDictionaryaramByItemAndCodeParam();
        query.setItemCode(TenantDictionary.ITEM_THIRD_CONFIG);
        query.setCode(TenantDictionary.CODE_STORAGE_ARCHIVE_PROP_CODE_CONFIG);
        var dict = dataDictionaryClient.getDataDictionaryByItemAndCode(query);
        if (dict == null || StringUtils.isBlank(dict.getVariable())) {
            return null;
        }
        try {
            WarehouseStorageArchiveAlarmConfigDTO config =
                    JsonUtils.deserialize(dict.getVariable().trim(), WarehouseStorageArchiveAlarmConfigDTO.class);
            if (config == null) {
                return null;
            }
            config.trim();
            return config;
        } catch (RuntimeException e) {
            log.warn("储量档案报警配置字典 variable 解析失败: {}", dict.getVariable(), e);
            return null;
        }
    }
}
