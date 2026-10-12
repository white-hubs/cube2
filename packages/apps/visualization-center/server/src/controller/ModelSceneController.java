package com.guhecloud.cube.base.visualization.server.controller;

import com.guhecloud.cube.base.visualization.server.entity.ModelScene;
import com.guhecloud.cube.base.visualization.server.param.ModelSceneParam;
import com.guhecloud.cube.base.visualization.server.param.VisualizationBaseQueryParam;
import com.guhecloud.cube.base.visualization.server.service.ModelSceneService;
import com.guhecloud.cube.base.visualization.server.vo.ModelSceneVO;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 模型场景配置接口
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/1 15:17
 * @Description:
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/model/scene")
public class ModelSceneController {
    private final ModelSceneService modelSceneService;

    /**
     * 通过项目id查询模型场景配置
     *
     * @param param 查询参数
     *
     * @return 实例对象
     */
    @GetMapping("/list")
    public List<ModelSceneVO> queryModelSceneByProjectId(VisualizationBaseQueryParam param) {
        return modelSceneService.queryModelSceneByProjectId(param);
    }

    /**
     * 新增或更新模型场景配置
     *
     * @param modelSceneParam 实例对象
     *
     * @return 实例对象
     */
    @PostMapping("/save/or/update")
    public Boolean saveOrUpdateModelScene(@RequestBody ModelSceneParam modelSceneParam) {
        return modelSceneService.saveOrUpdateModelScene(modelSceneParam);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    public Boolean deleteModelSceneById(@PathVariable Long id) {
        return DatabaseContextHolder.softDelete(ModelScene.class, id);
    }

    /**
     * 自动关联区域模板获取
     */
    @GetMapping("/auto/relate/template")
    public void autoRelateTemplateWrite(HttpServletResponse response) {
        modelSceneService.autoRelateTemplateWrite(response);
    }

    /**
     * 自动关联区域和模型结构
     */
    @PostMapping("/auto/relate")
    public void autoRelateAreaAndModel(@RequestParam("file") MultipartFile file, ModelSceneParam param) {
        modelSceneService.autoRelateAreaAndModel(file, param);
    }

}
