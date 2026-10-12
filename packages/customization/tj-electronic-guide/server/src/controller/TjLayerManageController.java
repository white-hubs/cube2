package com.guhecloud.cube.srv.tj.electronic.guide.controller;

import com.guhecloud.cube.srv.tj.electronic.guide.param.LayerManagePageParam;
import com.guhecloud.cube.srv.tj.electronic.guide.param.LayerManageParam;
import com.guhecloud.cube.srv.tj.electronic.guide.service.TjLayerManageService;
import com.guhecloud.cube.srv.tj.electronic.guide.vo.LayerManageDetailsVO;
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
public class TjLayerManageController {
    private final TjLayerManageService tjLayerManageService;

    /**
     * 查询详情
     */
    @GetMapping("/public/tj/layer/{id}")
    public LayerManageDetailsVO getLayerManageById(@PathVariable Long id) {
        return tjLayerManageService.getLayerManageById(id);
    }

    /**
     * 查询使用中的图层
     */
    @GetMapping("/public/tj/layer/state")
    public LayerManageDetailsVO getLayerManageByState() {
        return tjLayerManageService.getLayerManageByState();
    }

    /**
     * 查询列表
     */
    @GetMapping("/public/tj/layer/page")
    public Pagination<LayerManageDetailsVO> getLayerManageByPage(LayerManagePageParam param) {
        return tjLayerManageService.getLayerManageByPage(param);
    }

    /**
     * 新增图层
     */
    @PostMapping("/tj/layer/add")
    public Boolean addLayerManage(@RequestBody LayerManageParam param) {
        return tjLayerManageService.addLayerManage(param);
    }

    /**
     * 编辑图层
     */
    @PutMapping("/tj/layer/update")
    public Boolean updateLayerManage(@RequestBody LayerManageParam param) {
        return tjLayerManageService.updateLayerManage(param);
    }

    /**
     * 使用图层
     */
    @PostMapping("/tj/layer/updateState")
    public Boolean updateLayerManageByState(@RequestBody LayerManageParam param) {
        return tjLayerManageService.updateLayerManageByState(param);
    }

    /**
     * 删除图层
     */
    @DeleteMapping("/tj/layer/{id}")
    public Boolean deleteLayerManage(@PathVariable Long id) {
        return tjLayerManageService.deleteLayerManage(id);
    }
}
