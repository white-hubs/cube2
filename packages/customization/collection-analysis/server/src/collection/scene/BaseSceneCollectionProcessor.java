package com.guhecloud.cube.biz.collection.analysis.collection.scene;

import com.guhecloud.cube.biz.collection.analysis.collection.ICollectionProcessor;
import lombok.Getter;

@Getter
public abstract class BaseSceneCollectionProcessor<REQ, REP> implements ICollectionProcessor<REQ, REP> {

    private final String sceneCode;

    public BaseSceneCollectionProcessor(String sceneCode) {
        this.sceneCode = sceneCode;
    }
}
