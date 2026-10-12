package com.guhecloud.cube.base.message2.server.base.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.message2.server.base.entity.MsgManufacturer;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.base.param.MsgManufacturerModifyParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgManufacturerQueryParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgManufacturerUpdateParam;

import java.util.Collection;
import java.util.List;

public interface MsgManufacturerService extends IService<MsgManufacturer> {

    Long save(MsgManufacturerModifyParam param);

    void update(String code, MsgManufacturerUpdateParam param);

    MsgManufacturer getByCode(String code);

    List<MsgManufacturer> listMsgManufacturers(MsgManufacturerQueryParam param);

    List<MsgManufacturer> listMsgManufacturersByCodes(Collection<ManufacturerCodeEnum> codes);

    void remove(String code);
}
