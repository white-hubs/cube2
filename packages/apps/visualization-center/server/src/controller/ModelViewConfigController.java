package com.guhecloud.cube.base.visualization.server.controller;

import com.guhecloud.cube.base.visualization.server.entity.ModelViewConfig;
import com.guhecloud.cube.base.visualization.server.param.ModelViewConfigParam;
import com.guhecloud.cube.base.visualization.server.param.VisualizationBaseQueryParam;
import com.guhecloud.cube.base.visualization.server.service.ModelViewConfigService;
import com.guhecloud.cube.base.visualization.server.vo.ModelViewConfigVO;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 模型视角配置接口
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/1 18:36
 * @Description:
 */

@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/model/view/config")
public class ModelViewConfigController {

    private final ModelViewConfigService modelViewConfigService;

    /**
     * 通过项目id查询视角配置
     *
     * @param param 查询参数
     *
     * @return 实例对象
     */
    @GetMapping("/list")
    public List<ModelViewConfigVO> queryModelViewConfigByProjectId(VisualizationBaseQueryParam param) {
        return modelViewConfigService.queryModelViewConfigByProjectId(param);
    }

    /**
     * 新增或更新视角配置
     *
     * @param modelViewConfigParam 实例对象
     *
     * @return 实例对象
     */
    @PostMapping("/save/or/update")
    public Boolean saveOrUpdateModelViewConfig(@RequestBody ModelViewConfigParam modelViewConfigParam) {
        return modelViewConfigService.saveOrUpdateModelViewConfig(modelViewConfigParam);
    }

    /**
     * 通过主键删除视角数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    public Boolean deleteModelViewConfigById(@PathVariable Long id) {
        return DatabaseContextHolder.softDelete(ModelViewConfig.class, id);
    }
}
