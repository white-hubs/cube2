package com.guhecloud.cube.base.message2.server.sat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgManufacturer;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgManufacturerModifyParam;

import java.util.Collection;
import java.util.List;

public interface SatMsgManufacturerService extends IService<SatMsgManufacturer> {

    Long save(SatMsgManufacturerModifyParam param);

    void update(Long id, SatMsgManufacturerModifyParam param);

    List<SatMsgManufacturer> listSatMsgManufacturerByIds(Collection<Long> ids);

    SatMsgManufacturer getById(Long id);

    void remove(Long id);
}
