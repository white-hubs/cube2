package com.guhecloud.cube.biz.door.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.door.server.entity.DoorType;
import com.guhecloud.cube.biz.door.server.param.DoorTypeParam;

import java.util.List;

public interface DoorTypeService extends IService<DoorType> {
    List<DoorType> getDoorType();

    void updateDoorType(DoorTypeParam doorTypeParam);

    void addDoorType(DoorTypeParam doorTypeParam);

    void deleteDoorType(Long id);
}
