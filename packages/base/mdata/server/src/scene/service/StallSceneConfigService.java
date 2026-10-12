package com.guhecloud.cube.biz.mdata.server.scene.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.mdata.server.scene.entity.BusinessSceneConfig;
import com.guhecloud.cube.biz.mdata.server.scene.param.StallSceneConfigModifyParam;

import java.util.List;
import java.util.Set;

public interface StallSceneConfigService extends IService<BusinessSceneConfig> {

    void save(StallSceneConfigModifyParam param);

    List<BusinessSceneConfig> listBusinessSceneConfigByBusinessId(Long projectId, Long stallId);

    BusinessSceneConfig getByBusinessScene(Long stallId, Long sceneType);

    void removeByProjectId(Long projectId, Long stallId);

    void remove(Long stallId, Long sceneType);

    void remove(Set<Long> stallIds, Long sceneType);
}
