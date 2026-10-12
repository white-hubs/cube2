package com.guhecloud.platform.redis.config;

import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;

@Slf4j
@Configuration
public class RedissonConfiguration {
    @Autowired
    private RedisProperties redisProperties;

    /*
     * 初始化RedissonClient客户端
     * 注意：
     * 此实例集群为3节点，各节点1主1从
     * 集群模式,集群节点的地址须使用“redis://”前缀，否则将会报错。
     *
     * @return {@link RedissonClient}

     */

    @Bean
    public RedissonClient getRedissonClient() {
        var config = new Config();
        config.setCodec(new StringCodec());
        if (redisProperties.getCluster() != null) {
            //集群模式配置
            var nodes = redisProperties.getCluster().getNodes();

            var clusterNodes = new ArrayList<String>();
            for (var node : nodes) {
                clusterNodes.add("redis://" + node);
            }
            var clusterServersConfig = config.useClusterServers()
                    .addNodeAddress(clusterNodes.toArray(new String[0]));

            if (!StringUtils.isEmpty(redisProperties.getPassword())) {
                clusterServersConfig.setPassword(redisProperties.getPassword());
            }
        } else {
            //单节点配置
            var address = "redis://" + redisProperties.getHost() + ":" + redisProperties.getPort();
            var serverConfig = config.useSingleServer();
            serverConfig.setAddress(address);
            if (!StringUtils.isEmpty(redisProperties.getPassword())) {
                serverConfig.setPassword(redisProperties.getPassword());
            }
            serverConfig.setDatabase(redisProperties.getDatabase());
        }
        //看门狗的锁续期时间，默认30000ms，这里配置成15000ms
        config.setLockWatchdogTimeout(15000);
        return Redisson.create(config);
    }
}
