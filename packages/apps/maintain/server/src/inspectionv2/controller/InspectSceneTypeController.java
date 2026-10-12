package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.controller;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.InspectSceneTypeService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.SceneTypeVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author wql
 * @describe 现场点类型
 * @date 2023/6/5 15:58
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectSceneTypeController {
    private final InspectSceneTypeService inspectSceneTypeService;

    /**
     * 新增现场点类型
     *
     * @param typeName 现场点类型名称
     */
    @PutMapping("/inspect/addType/{typeName}")
    public void addType(@PathVariable("typeName") String typeName) {
        inspectSceneTypeService.addType(typeName);
    }

    /**
     * 现场点类型列表
     *
     * @return 现场点类型列表
     */
    @GetMapping("/inspect/sceneTypeList")
    public List<SceneTypeVO> sceneTypeList() {
        return inspectSceneTypeService.sceneTypeList();
    }

    /**
     * 更新现场点类型信息
     *
     * @param typeId   现场点类型id
     * @param typeName 现场点类型名称
     */
    @PutMapping("/inspect/updateType/{typeId}/{typeName}")
    public void updateType(@PathVariable("typeId") String typeId, @PathVariable("typeName") String typeName) {
        inspectSceneTypeService.updateType(typeId, typeName);
    }

    /**
     * 删除现场点类型信息
     *
     * @param typeId 现场点类型id
     */
    @DeleteMapping("/inspect/deleteType/{typeId}")
    public void deleteType(@PathVariable("typeId") String typeId) {
        inspectSceneTypeService.deleteType(typeId);
    }

}
