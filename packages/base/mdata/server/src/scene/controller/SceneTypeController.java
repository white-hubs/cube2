package com.guhecloud.cube.biz.mdata.server.scene.controller;

import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.mdata.server.scene.entity.SceneType;
import com.guhecloud.cube.biz.mdata.server.scene.param.SceneTypeModifyParam;
import com.guhecloud.cube.biz.mdata.server.scene.param.SceneTypeQueryParam;
import com.guhecloud.cube.biz.mdata.server.scene.service.SceneTypeService;
import com.guhecloud.cube.biz.mdata.server.scene.vo.SceneTypeVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.base.util.TreeUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

/**
 * 场景类型接口
 *
 * @author lzy
 * @date 2021/11/17
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SceneTypeController {

    private final SceneTypeService sceneTypeService;

    private final FileClient fileClient;

    /**
     * 场景类型新增
     *
     * @param param 新增参数
     */
    @PostMapping("/scene-type")
    public Long save(@Valid @RequestBody SceneTypeModifyParam param) {
        return this.sceneTypeService.save(param);
    }

    /**
     * 场景类型修改
     *
     * @param id    场景类型主键
     * @param param 修改参数
     */
    @PutMapping("/scene-type/{id}")
    public void update(@PathVariable("id") @Min(value = 1, message = "场景类型主键不能小于1") Long id,
                       @Valid @RequestBody SceneTypeModifyParam param
                      ) {
        this.sceneTypeService.update(id, param);
    }

    /**
     * 场景类型列表
     *
     * @return 场景类型树形列表
     */
    @GetMapping("/scene-type/page")
    public List<SceneTypeVO> page(@RequestParam("projectId") Long projectId) {
        List<SceneType> sceneTypes = this.sceneTypeService.list(projectId);
        List<SceneTypeVO> sceneTypeVOS = ObjectUtils.copying(sceneTypes, SceneTypeVO.class);
        if (!CollectionUtils.isEmpty(sceneTypeVOS)) {
            sceneTypeVOS.forEach(sceneTypeVO -> {
                List<FileVO> fileVOS = this.fileClient.getByBusinessKey(sceneTypeVO.getId());
                if (!CollectionUtils.isEmpty(fileVOS)) {
                    sceneTypeVO.setFileVO(fileVOS.get(0));
                }
                if (StringUtils.isNotEmpty(sceneTypeVO.getScenarioType())) {
                    List<String> list = Arrays.stream(sceneTypeVO.getScenarioType().split("-")).toList();
                    sceneTypeVO.setScenarioTypes(list);
                }
            });
        }
        return TreeUtils.assembleTrees(sceneTypeVOS);
    }

    /**
     * 场景类型列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/scene-type/list")
    public List<SceneTypeVO> listSceneTypes(@SpringQueryMap SceneTypeQueryParam param) {
        List<SceneType> sceneTypes = this.sceneTypeService.getByIds(param.getProjectId(), param.getIds());
        List<SceneTypeVO> copying = ObjectUtils.copying(sceneTypes, SceneTypeVO.class);
        copying.forEach(s -> {
            if (StringUtils.isNotEmpty(s.getScenarioType())) {
                List<String> list = Arrays.stream(s.getScenarioType().split("-")).toList();
                s.setScenarioTypes(list);
            }
        });
        return copying;
    }

    /**
     * 场景类型删除
     *
     * @param id 场景类型主键
     */
    @DeleteMapping("/scene-type/{id}")
    public void remove(@PathVariable("id") @Min(value = 1, message = "场景类型主键不能小于1") Long id) {
        this.sceneTypeService.remove(id);
    }
}
