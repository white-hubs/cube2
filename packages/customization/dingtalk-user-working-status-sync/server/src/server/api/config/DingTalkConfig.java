package com.guhecloud.cube.customized.dingtalkuserworkingstatussync.server.api.config;

import com.guhecloud.cube.base.dictionary.client.DataDictionaryClient;
import com.guhecloud.cube.base.dictionary.dto.DataDictionaryDTO;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramParam;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DingTalkConfig {

    /**
     * 接口请求基地址
     */
    private String baseUrl;

    /**
     * 操作人userId
     */
    private String opUserId;

    /**
     * 已创建的企业内部应用的 Cilent ID（原企业内部应用AppKey）。
     */
    private String appKey;

    /**
     * 已创建的企业内部应用的 Cilent Secre （原企业内部应用AppSecret）。
     */
    private String appSecret;

    /**
     * 租户信息
     */
    private Integer tenant;

    private static final String SUB_SYSTEM_CODE = "THIRD_SERVICE";

    private static final String KEY = "ding_talk_service";

    public static DingTalkConfig getDingTalkConfig() {
        var dataDictionaryClient = ApplicationContextHolder.getBean(DataDictionaryClient.class);
        DataDictionaryaramParam dataDictionaryaramParam = new DataDictionaryaramParam();
        dataDictionaryaramParam.setItemCode(SUB_SYSTEM_CODE);
        dataDictionaryaramParam.setCode(KEY);
        List<DataDictionaryDTO> dataDictionaryByItem = dataDictionaryClient.getDataDictionaryByItem(dataDictionaryaramParam);
        if (CollectionUtils.isEmpty(dataDictionaryByItem)) {
            return null;
        }
        return JsonUtils.deserialize(dataDictionaryByItem.getFirst().getVariable(), DingTalkConfig.class);
    }

    public static List<DingTalkConfig> getAllDingTalkConfig() {
        var redisTemplate = ApplicationContextHolder.getBean(RedisTemplate.class);
        var keys = redisTemplate.keys("DICTIONARY:" + SUB_SYSTEM_CODE + ":*" + KEY);
        List<DingTalkConfig> dingTalkConfigs = new ArrayList<>();
        keys.forEach(key -> {
            var o = JsonUtils.deserialize((String) redisTemplate.opsForValue().get(key), DataDictionaryDTO.class);
            if (o != null) {
                var dingTalkConfig = JsonUtils.deserialize(o.getVariable(), DingTalkConfig.class);
                if (dingTalkConfig != null) {
                    dingTalkConfigs.add(dingTalkConfig);
                }
            }
        });
        return dingTalkConfigs;
    }
}
