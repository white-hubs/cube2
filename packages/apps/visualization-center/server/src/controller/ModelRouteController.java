package com.guhecloud.cube.base.visualization.server.controller;

import com.guhecloud.cube.base.visualization.server.param.ModelRouteParam;
import com.guhecloud.cube.base.visualization.server.service.ModelRouteService;
import com.guhecloud.cube.base.visualization.server.vo.ModelRouteVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 模型导航路径
 *
 * @Author: lijie
 * @CreateTime: 2023-08-29  14:06
 * @Description: 模型导航路径
 * @Version: 1.0
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/model/route")
public class ModelRouteController {
    private final ModelRouteService modelRouteService;

    /**
     * 查询所有模型
     *
     * @param modelId 模型id
     * @param name    模型导航名称
     *
     * @return 模型信息
     */
    @GetMapping("/queryAll")
    public List<ModelRouteVO> queryAll(@RequestParam(value = "modelId") Long modelId,
                                       @RequestParam(value = "name", required = false) String name
                                      ) {
        return modelRouteService.queryAll(modelId, name);
    }

    /**
     * 同步区域数据
     *
     * @param modelId 模型id
     *
     * @return 同步结果
     */
    @GetMapping("/insertAreaAll")
    public Boolean insertAreaAll(@RequestParam(value = "modelId") Long modelId) {
        return modelRouteService.insertAreaAll(modelId);
    }

    /**
     * 新增模型
     *
     * @param param 模型信息
     *
     * @return 新增结果
     */
    @PostMapping("/insert")
    public Boolean insert(@RequestBody ModelRouteParam param) {
        return modelRouteService.insert(param);
    }

    /**
     * 更新模型
     *
     * @param param 模型信息
     *
     * @return 新增结果
     */
    @PutMapping("/update")
    public Boolean update(@RequestBody ModelRouteParam param) {
        return modelRouteService.update(param);
    }

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    @DeleteMapping("/{id}")
    public Boolean deleteById(@PathVariable Long id) {
        return modelRouteService.deleteById(id);
    }
}
