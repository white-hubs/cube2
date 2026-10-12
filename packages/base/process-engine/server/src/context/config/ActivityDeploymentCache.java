package com.guhecloud.cube.base.flow.context.config;

import com.guhecloud.platform.base.util.JsonUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ActivitiException;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.deploy.DeploymentCache;
import org.activiti.engine.impl.persistence.deploy.ProcessDefinitionCacheEntry;
import org.activiti.engine.impl.persistence.entity.DeploymentEntityImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntityImpl;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/8/27 10:06
 * @Description:
 */
@Slf4j
public class ActivityDeploymentCache implements DeploymentCache<ProcessDefinitionCacheEntry> {
    public static final String DEFAULT_ACTIVITY_DEF_CACHE_BEAN_NAME = "activityDeploymentCache";

    private static final String PROCESS_DEFINITION_NAME = "processDefinition";

    private static final String BPMN_MODEL_NAME = "BpmnModel";

    private static final String PROCESS_DEFINITION_CACHE = "PROCESS_DEFINITION_CACHE:";

    private final RedisTemplate<String, byte[]> redisTemplate;

    private final BpmnXMLConverter bpmnXMLConverter;

    private static final ThreadLocal<Map<String, ProcessDefinitionCacheEntry>> LOCAL_CACHE =  ThreadLocal.withInitial(HashMap::new);

    public ActivityDeploymentCache(RedisTemplate<String, byte[]> redisTemplate, BpmnXMLConverter bpmnXMLConverter
                                  ) {
        this.redisTemplate = redisTemplate;
        this.bpmnXMLConverter = bpmnXMLConverter;
    }
    public static void clearLocalCache() {
        LOCAL_CACHE.remove();
    }

    public static void initLocalCache() {
        Map<String, ProcessDefinitionCacheEntry> cacheEntryMap = LOCAL_CACHE.get();
        if (cacheEntryMap != null) {
            cacheEntryMap.clear();
        }else {
            LOCAL_CACHE.set(new HashMap<>());
        }
    }


    @Override
    public ProcessDefinitionCacheEntry get(String id)  {
        Map<String, ProcessDefinitionCacheEntry> cache = LOCAL_CACHE.get();
        if (cache != null) {
            ProcessDefinitionCacheEntry localEntry = cache.get(id);
            if (localEntry != null) {
                return localEntry;
            }
        }
        ProcessDefinitionCacheEntry entry = getProcessDefinitionCacheEntry(
                redisTemplate.opsForValue().get(getProcessDefinitionId(id)), redisTemplate.opsForValue().get(getBpmnId(id)));
        if (entry != null && cache != null) {
            cache.put(id, entry);
        }
        return entry;
    }

    @Override
    public boolean contains(String id) {
        Map<String, ProcessDefinitionCacheEntry> cache = LOCAL_CACHE.get();
        if (cache != null && cache.containsKey(id)) {
            return true;
        }
        return redisTemplate.hasKey(getProcessDefinitionId(id))
               && redisTemplate.hasKey(getBpmnId(id));
    }

    @SneakyThrows
    @Override
    public void add(String id, ProcessDefinitionCacheEntry entry) {
        redisTemplate.opsForValue().set(getProcessDefinitionId(id), JsonUtils.serializeBytes(entry.getProcessDefinition()));
        redisTemplate.opsForValue().set(getBpmnId(id), bpmnXMLConverter.convertToXML(entry.getBpmnModel()));
        Map<String, ProcessDefinitionCacheEntry> cache = LOCAL_CACHE.get();
        if (cache != null) {
            cache.put(id, entry);
        }
    }

    @Override
    public void remove(String id) {
        redisTemplate.delete(Arrays.asList(getProcessDefinitionId(id), getBpmnId(id)));
        Map<String, ProcessDefinitionCacheEntry> cache = LOCAL_CACHE.get();
        if (cache != null) {
            cache.remove(id);
        }
    }

    @Override
    public void clear() {
        redisTemplate.delete(PROCESS_DEFINITION_CACHE + "*");
        LOCAL_CACHE.remove();
    }

    private ProcessDefinitionCacheEntry getProcessDefinitionCacheEntry(byte[] processBytes, byte[] bpmnBytes) {
        if (null == processBytes || null == bpmnBytes) {
            return null;
        }
        ProcessDefinitionEntityImpl processDefinition = JsonUtils.deserialize(processBytes, ProcessDefinitionEntityImpl.class);
        if (null == processDefinition) {
            return null;
        }
        ProcessEngineConfigurationImpl processEngineConfiguration = Context.getProcessEngineConfiguration();
        DeploymentEntityImpl deploymentEntity = new DeploymentEntityImpl();
        deploymentEntity.setEngineVersion(processDefinition.getEngineVersion());
        deploymentEntity.setId(processDefinition.getDeploymentId());
        BpmnModel bpmnModel;
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(bpmnBytes)) {
            BpmnParse bpmnParse = processEngineConfiguration.getBpmnParser().createParse()
                    .sourceInputStream(inputStream)
                    .setSourceSystemId(processDefinition.getResourceName())
                    .deployment(deploymentEntity)
                    .name(processDefinition.getResourceName());

            bpmnParse.setValidateSchema(false);
            bpmnParse.setValidateProcess(false);

            bpmnParse.execute();
            bpmnModel = bpmnParse.getBpmnModel();
        } catch (IOException e) {
            throw new ActivitiException(e.getMessage(), e);
        }

        return new ProcessDefinitionCacheEntry(processDefinition, bpmnModel, bpmnModel.getMainProcess());

    }

    String getProcessDefinitionId(String id) {
        return PROCESS_DEFINITION_CACHE + id + PROCESS_DEFINITION_NAME;
    }

    String getBpmnId(String id) {
        return PROCESS_DEFINITION_CACHE + id + BPMN_MODEL_NAME;
    }
}
