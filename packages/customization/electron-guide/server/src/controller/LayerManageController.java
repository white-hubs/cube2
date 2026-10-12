package com.guhecloud.cube.srv.electron.guide.controller;

import com.guhecloud.cube.srv.electron.guide.param.LayerManagePageParam;
import com.guhecloud.cube.srv.electron.guide.param.LayerManageParam;
import com.guhecloud.cube.srv.electron.guide.service.LayerManageService;
import com.guhecloud.cube.srv.electron.guide.vo.LayerManageDetailsVO;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 图层管理
 *
 * @Author: lijie
 * @CreateTime: 2024-01-08  11:40
 * @Description: 图层管理
 * @Version: 1.0
 */
@RestController
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LayerManageController {
    private final LayerManageService layerManageService;

    /**
     * 查询详情
     */
    @GetMapping("/public/layer/{id}")
    public LayerManageDetailsVO getLayerManageById(@PathVariable Long id) {
        return layerManageService.getLayerManageById(id);
    }

    /**
     * 查询使用中的图层
     */
    @GetMapping("/public/layer/state")
    public LayerManageDetailsVO getLayerManageByState(@RequestParam Long areaId) {
        return layerManageService.getLayerManageByAreaId(areaId);
    }

    /**
     * 查询列表
     */
    @GetMapping("/public/layer/page")
    public Pagination<LayerManageDetailsVO> getLayerManageByPage(LayerManagePageParam param) {
        return layerManageService.getLayerManageByPage(param);
    }

    /**
     * 新增图层
     */
    @PostMapping("/layer/add")
    public Boolean addLayerManage(@RequestBody LayerManageParam param) {
        return layerManageService.addLayerManage(param);
    }

    /**
     * 编辑图层
     */
    @PutMapping("/layer/update")
    public Boolean updateLayerManage(@RequestBody LayerManageParam param) {
        return layerManageService.updateLayerManage(param);
    }

    /**
     * 使用图层
     */
    @PostMapping("/layer/updateState")
    public Boolean updateLayerManageByState(@RequestBody LayerManageParam param) {
        return layerManageService.updateLayerManageByState(param);
    }

    /**
     * 删除图层
     */
    @DeleteMapping("/layer/{id}")
    public Boolean deleteLayerManage(@PathVariable Long id) {
        return layerManageService.deleteLayerManage(id);
    }
}
