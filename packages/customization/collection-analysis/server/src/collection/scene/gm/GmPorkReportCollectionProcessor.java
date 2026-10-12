package com.guhecloud.cube.biz.collection.analysis.collection.scene.gm;

import com.guhecloud.cube.biz.collection.analysis.collection.scene.BaseSceneCollectionProcessor;
import com.guhecloud.cube.biz.collection.analysis.gm.enumeration.CollectionSceneEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class GmPorkReportCollectionProcessor extends BaseSceneCollectionProcessor<String, String> {

    public GmPorkReportCollectionProcessor() {
        super(CollectionSceneEnum.GM_STATISTICS_COLLECTION.name());
    }

    @Override
    public List<String> cleanData(List<String> strings) {
        return strings;
    }

    @Override
    public List<String> getCollectionData() {
        return Collections.emptyList();
    }
}
