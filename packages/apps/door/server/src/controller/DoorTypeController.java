package com.guhecloud.cube.biz.door.server.controller;

import com.guhecloud.cube.biz.door.server.entity.DoorType;
import com.guhecloud.cube.biz.door.server.param.DoorTypeParam;
import com.guhecloud.cube.biz.door.server.service.DoorTypeService;
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

import java.util.List;

/**
 * @author lrj
 * @Description 门禁类型
 * @date 2022-07-04 15:12:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DoorTypeController {
    private final DoorTypeService doorTypeService;

    /**
     * @Description 门禁类型
     * @Author lrj
     */
    @GetMapping("/getDoorType")
    public List<DoorType> getDoorType() {
        return doorTypeService.getDoorType();
    }

    /**
     * @Description 门禁类型修改
     * @Author lrj
     */
    @PutMapping("/updateDoorType")
    public void updateDoorType(DoorTypeParam param) {
        doorTypeService.updateDoorType(param);
    }

    /**
     * @Description 门禁类型添加
     * @Author lrj
     */
    @PostMapping("/addDoorType")
    public void addDoorType(@RequestBody DoorTypeParam param) {
        doorTypeService.addDoorType(param);
    }

    /**
     * @Description 门禁删除类型
     * @Author lrj
     */
    @DeleteMapping("/deleteDoorType/{id}")
    public void deleteDoorType(@PathVariable Long id) {
        doorTypeService.deleteDoorType(id);
    }

}
