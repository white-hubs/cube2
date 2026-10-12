package com.guhecloud.cube.biz.mdata.server.scene.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.scene.entity.AreaSceneConfig;
import com.guhecloud.cube.biz.mdata.server.scene.param.AreaSceneConfigModifyParam;
import com.guhecloud.cube.biz.mdata.server.scene.param.AreaScenePanelConfigBatchModifyParam;

import java.util.List;

public interface AreaSceneConfigService extends IService<AreaSceneConfig> {

    Long save(AreaSceneConfigModifyParam param);

    void disposableSave(AreaScenePanelConfigBatchModifyParam param);

    List<AreaSceneConfig> listAreaSceneConfigsByAreaId(Long projectId, Long areaId);

    List<AreaSceneConfig> listAreaSceneConfigsBySceneType(Long sceneType);

    AreaSceneConfig getByAreaScene(Long areaId, Long sceneType);

    void remove(Long areaId, Long sceneType);
}
