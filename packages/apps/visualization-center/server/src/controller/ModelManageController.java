package com.guhecloud.cube.base.visualization.server.controller;

import com.guhecloud.cube.base.visualization.server.param.ModelManagePageParam;
import com.guhecloud.cube.base.visualization.server.param.ModelManageParam;
import com.guhecloud.cube.base.visualization.server.service.ModelManageService;
import com.guhecloud.cube.base.visualization.server.vo.ModelManageVO;
import com.guhecloud.platform.base.model.Pagination;
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
 * 可视化配置模型管理
 *
 * @Author: lijie
 * @CreateTime: 2023-08-29  10:32
 * @Description: 模型管理
 * @Version: 1.0
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/model")
public class ModelManageController {
    private final ModelManageService modelManageService;

    /**
     * 通过ID查询模型详情
     *
     * @param id 主键
     *
     * @return 模型详情
     */
    @GetMapping("/{id}")
    public ModelManageVO queryById(@PathVariable Long id) {
        return modelManageService.queryById(id);
    }

    /**
     * 分页查询模型
     *
     * @param param 分页条件
     *
     * @return 模型信息
     */
    @GetMapping("/queryPage")
    public Pagination<ModelManageVO> queryPage(ModelManagePageParam param) {
        return modelManageService.queryPage(param);
    }

    /**
     * 查询所有模型
     *
     * @param modelType 模型类型
     *
     * @return 模型信息
     */
    @GetMapping("/queryAll")
    public List<ModelManageVO> queryAll(@RequestParam(value = "modelType") String modelType) {
        return modelManageService.queryAll(modelType);
    }

    /**
     * 新增模型
     *
     * @param param 模型信息
     *
     * @return 新增结果
     */
    @PostMapping("/insert")
    public ModelManageVO insert(@RequestBody ModelManageParam param) {
        return modelManageService.insert(param);
    }

    /**
     * 更新模型
     *
     * @param param 模型信息
     *
     * @return 新增结果
     */
    @PutMapping("/update")
    public Boolean update(@RequestBody ModelManageParam param) {
        return modelManageService.update(param);
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
        return modelManageService.deleteById(id);
    }
}
