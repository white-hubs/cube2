package com.guhecloud.cube.base.file.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.file.server.entity.FileConfig;
import com.guhecloud.cube.base.file.server.entity.SceneInfo;

import java.util.List;

/**
 * @Author: lijie
 * @CreateTime: 2024-12-11  10:43
 * @Description:
 * @Version: 1.0
 */
public interface SceneInfoService extends IService<SceneInfo> {
    List<SceneInfo> getSceneInfoAll();

    FileConfig getConfig(String sceneCode, Integer tenant);
}
