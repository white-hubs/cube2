package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.controller;

import com.guhecloud.cube.srv.gen.maintain.server.common.vo.ScenePointVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公共现场点接口
 *
 * @author lanchun
 * @date 2025/1/3 14:49
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PublicInspectScenePointController {

    private final InspectScenePointController inspectScenePointController;

    /**
     * 现场点详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/public/inspect/detail/{id}")
    public ScenePointVO scenePointDetail(@PathVariable("id") Long id) {
        return inspectScenePointController.scenePointDetail(id);
    }
}
