package com.guhecloud.cube.sn.meattrade.plugin.common.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramParam;
import com.guhecloud.cube.sn.meattrade.plugin.common.entity.ThirdPartyGoodsMapping;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SnThirdPartyGoodsMappingQueryService {

    private final DataDictionaryClient dataDictionaryClient;

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String SUB_SYSTEM_CODE = "THIRD_SERVICE";

    private static final String KEY = "THIRD_SERVICE::shenNong_meattrade_dbc::category_mapping";

    private static final String GM_MAPPING_REDIS_KEY = "MEAT_RADE:DBC:CATEGORY_MAPPING";

    private static final String SPLIT_CHAR = "@";

    public Map<String, ThirdPartyGoodsMapping> getThirdPartyGoodsMappings() {
        Object o = this.redisTemplate.opsForValue().get(getRedisCacheKey());
        if (Objects.isNull(o)) {
            DataDictionaryaramParam dataDictionaryaramParam = new DataDictionaryaramParam();
            dataDictionaryaramParam.setItemCode(SUB_SYSTEM_CODE);
            dataDictionaryaramParam.setCode(KEY);
            List<DataDictionaryDTO> dataDictionaryByItem = dataDictionaryClient.getDataDictionaryByItem(dataDictionaryaramParam);
            if (CollectionUtils.isEmpty(dataDictionaryByItem)) {
                log.warn("SN_meat_trade_category_mapping_config is null");
                return Collections.emptyMap();
            }
            List<ThirdPartyGoodsMapping> thirdPartyGoodsMappings = JsonUtils.deserialize(dataDictionaryByItem.get(0)
                                                                                                 .getVariable(), new TypeReference<List<ThirdPartyGoodsMapping>>() {});
            Map<String, ThirdPartyGoodsMapping> thirdPartyGoodsMappingMap = this.buildMappingMatchMap(thirdPartyGoodsMappings);
            this.redisTemplate.opsForValue().set(getRedisCacheKey(), thirdPartyGoodsMappingMap, 60, TimeUnit.MINUTES);
            return thirdPartyGoodsMappingMap;
        }
        return (Map<String, ThirdPartyGoodsMapping>) o;
    }

    public Optional<ThirdPartyGoodsMapping> convertThirdPartyGoodsToStander(String goodsType
                                                                           ) {
        Map<String, ThirdPartyGoodsMapping> thirdPartyGoodsMappings = this.getThirdPartyGoodsMappings();
        return Optional.ofNullable(thirdPartyGoodsMappings.get(goodsType + SPLIT_CHAR));
    }

    private String getRedisCacheKey() {
        return GM_MAPPING_REDIS_KEY + ":" + SessionContextHolder.getRequester().getTenant();
    }

    private Map<String, ThirdPartyGoodsMapping> buildMappingMatchMap(List<ThirdPartyGoodsMapping> thirdPartyGoodsMappings) {
        Map<String, ThirdPartyGoodsMapping> thirdPartyGoodsMappingMap = new HashMap<>();
        for (ThirdPartyGoodsMapping thirdPartyGoodsMapping : thirdPartyGoodsMappings) {
            String key = thirdPartyGoodsMapping.getThirdPartyGoodsCategory() + SPLIT_CHAR;
            thirdPartyGoodsMappingMap.put(key, thirdPartyGoodsMapping);
        }
        return thirdPartyGoodsMappingMap;
    }
}
