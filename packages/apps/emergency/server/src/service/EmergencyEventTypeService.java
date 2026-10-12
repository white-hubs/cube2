package com.guhecloud.cube.biz.emergency.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyEventType;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyEventTypeModifyParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyEventTypeQueryParam;

import java.util.Collection;
import java.util.List;

public interface EmergencyEventTypeService extends IService<EmergencyEventType> {

    Long save(EmergencyEventTypeModifyParam param);

    void update(Long id, EmergencyEventTypeModifyParam param);

    void initEventTypes(Integer tenant);

    EmergencyEventType getByCode(String code);

    List<EmergencyEventType> getByCodes(Collection<String> codes);

    Page<EmergencyEventType> getPage(EmergencyEventTypeQueryParam query);

    List<EmergencyEventType> getList(EmergencyEventTypeQueryParam query);

    void remove(Long id);
}
