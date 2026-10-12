package com.guhecloud.cube.biz.door.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.door.server.dao.DoorTypeMapper;
import com.guhecloud.cube.biz.door.server.entity.DoorType;
import com.guhecloud.cube.biz.door.server.param.DoorTypeParam;
import com.guhecloud.cube.biz.door.server.service.DoorTypeService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DoorTypeServiceImpl extends ServiceImpl<DoorTypeMapper, DoorType> implements DoorTypeService {
    @Override
    public List<DoorType> getDoorType() {
        return this.list();
    }

    @Override
    public void updateDoorType(DoorTypeParam doorTypeParam) {
        DoorType one = this.getOne(Wrappers.<DoorType>lambdaQuery()
                                           .eq(DoorType::getDoorTypeName, doorTypeParam.getDoorTypeName())
                                           .ne(DoorType::getId, doorTypeParam.getId()));
        Assert.isConflict(Objects.nonNull(one), "名称重复");
        DoorType updateDoorType = ObjectUtils.copying(doorTypeParam, DoorType.class);
        this.updateById(updateDoorType);
    }

    @Override
    public void addDoorType(DoorTypeParam doorTypeParam) {
        DoorType one = this.getOne(Wrappers.<DoorType>lambdaQuery()
                                           .eq(DoorType::getDoorTypeName, doorTypeParam.getDoorTypeName()));
        Assert.isConflict(Objects.nonNull(one), "名称重复");
        DoorType addDoorType = ObjectUtils.copying(doorTypeParam, DoorType.class);
        this.save(addDoorType);
    }

    @Override
    public void deleteDoorType(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }
}
