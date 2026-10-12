package com.guhecloud.cube.srv.gen.business.info.sync.server.job;

import com.guhecloud.cube.srv.gen.business.info.sync.server.service.dto.CustomTableDataSyncParam;
import com.guhecloud.cube.srv.gen.business.info.sync.server.sync.config.DynamicDataSourceRegistrar;
import com.guhecloud.cube.srv.gen.business.info.sync.server.sync.thread.CustomTableDataSyncService;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.job.jobtask.annotation.JobAnnotation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/4/22 15:48
 * @Description: 定时数据同步
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomTableDataSyncJob {
    private final RedisTemplate<String, Object> redisTemplate;

    private final DynamicDataSourceRegistrar dynamicDataSourceRegistrar;

    /**
     * 数据同步job
     */
    @JobAnnotation(name = "customTableDataSyncJob", cron = "* * * * * ?", desc = "苏州公安-数据同步")
    public void customTableDataSyncJob(String param) {
        var tableDataSyncParam = JsonUtils.deserialize(param, CustomTableDataSyncParam.class);
        var available = dynamicDataSourceRegistrar.checkDataSourceAvailable();
        if (available) {
            new CustomTableDataSyncService(redisTemplate,
                                           dynamicDataSourceRegistrar.getOrCreateDataSource("SOURCE_DATABASE"),
                                           dynamicDataSourceRegistrar.getOrCreateDataSource("TARGET_DATABASE"))
                    .executeDataSync(tableDataSyncParam);
        }
    }
}
