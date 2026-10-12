package com.guhecloud.cube.biz.emergency.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyType;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyTypeModifyParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyTypeQueryParam;

import java.io.Serializable;
import java.util.List;

public interface EmergencyTypeService extends IService<EmergencyType> {

    Long save(EmergencyTypeModifyParam param);

    void update(Long id, EmergencyTypeModifyParam param);

    EmergencyType getById(Serializable id);

    EmergencyType getEmergencyTypeByAppCode(String applicationCode);

    List<EmergencyType> listEmergencyTypes(EmergencyTypeQueryParam param);

    void remove(Long id);

}
