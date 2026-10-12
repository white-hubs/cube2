package com.guhecloud.cube.base.file.server.spi;

import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.common.Constants;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.shaded.com.google.common.base.Preconditions;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.SneakyThrows;
import org.apache.shardingsphere.infra.url.spi.ShardingSphereURLLoader;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/18 15:45
 * @Description:
 */
public class ShardingJdbcCustomNacosURLLoader implements ShardingSphereURLLoader {

    /**
     * 定义jdbc:shardingsphere:后的类型为nacos:
     */
    private static final String NACOS_TYPE = "nacos:";

    /**
     * 公共变量
     */
    private static final String COMMON_ENV = "common-env.yaml";

    /**
     * <p>
     * description: 接收nacos:后的参数shardingsphere-config.yaml?serverAddr=${quick.nacos.server-addr}&namespace=${quick.nacos.config.namespace}&group=${quick.nacos.config.group}
     * </p>
     *
     * @param configurationSubject configuration
     * @param queryProps           url参数，已经解析成为Properties
     *
     * @return: java.lang.String
     */
    @Override
    @SneakyThrows
    public String load(String configurationSubject, Properties queryProps) {
        ConfigService configService = NacosFactory.createConfigService(queryProps);
        //获取nacos配置
        String config = configService.getConfig(configurationSubject, queryProps.getProperty(Constants.GROUP, Constants.DEFAULT_GROUP), 500);
        String commonEnvConfig = configService.getConfig(COMMON_ENV, queryProps.getProperty(Constants.GROUP, Constants.DEFAULT_GROUP), 500);
        //todo  统一异常校验
        Preconditions.checkArgument(config != null, "Nacos config [" + configurationSubject + "] is Empty.");
        Yaml commonYaml = new Yaml();
        Map<String, Object> commonConfigYaml = commonYaml.load(commonEnvConfig);
        Map<String, String> perperties = new HashMap<>();
        this.buildConfigMap(null, perperties, commonConfigYaml);
        return StringUtils.placeholderReplace(config, perperties);
    }

    private void buildConfigMap(String key, Map<String, String> perperties, Map<String, Object> map) {
        if (!MapUtils.isNotEmpty(map)) {
            return;
        }
        for (Map.Entry<String, Object> e : map.entrySet()) {
            String localKey;
            if (StringUtils.isNotBlank(key)) {
                localKey = key + "." + e.getKey();
            } else {
                localKey = e.getKey();
            }
            if (e.getValue() instanceof Map) {

                buildConfigMap(localKey, perperties, (Map<String, Object>) e.getValue());
                continue;
            }
            perperties.put(localKey, ObjectUtils.isNotNull(e.getValue()) ? e.getValue().toString() : null);
        }
    }

    @Override
    public Object getType() {
        return NACOS_TYPE;
    }
}
