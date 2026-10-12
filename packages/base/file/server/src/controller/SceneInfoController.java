package com.guhecloud.cube.base.file.server.controller;

import com.guhecloud.cube.base.file.server.entity.SceneInfo;
import com.guhecloud.cube.base.file.server.service.SceneInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 场景管理
 *
 * @Author: lijie
 * @CreateTime: 2024-12-13  17:01
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SceneInfoController {
    private final SceneInfoService sceneInfoService;

    /**
     * 查询所有场景
     */
    @GetMapping("/get/scene/info/all")
    public List<SceneInfo> getSceneInfoAll() {
        return sceneInfoService.getSceneInfoAll();
    }
}
