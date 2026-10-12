package com.guhecloud.cube.biz.collection.analysis.collection.scene;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 场景数据采集工厂类
 *
 * @author lanchun
 * @date 2024/12/9 11:50
 */
@Component
public class SceneCollectionProcessFactory {

    private final Map<String, BaseSceneCollectionProcessor<?, ?>> SCENE_COLLECTION_PROCESSOR_MAP = new HashMap<>();

    @Autowired
    private void settingBaseSceneCollectionProcessor(List<BaseSceneCollectionProcessor<?, ?>> baseSceneCollectionProcessors) {
        for (BaseSceneCollectionProcessor<?, ?> baseSceneCollectionProcessor : baseSceneCollectionProcessors) {
            SCENE_COLLECTION_PROCESSOR_MAP.putIfAbsent(baseSceneCollectionProcessor.getSceneCode(), baseSceneCollectionProcessor);
        }
    }

    public BaseSceneCollectionProcessor<?, ?> getBaseSceneCollectionProcessor(String sceneCode) {
        return SCENE_COLLECTION_PROCESSOR_MAP.get(sceneCode);
    }
}
