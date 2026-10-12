package com.guhecloud.cube.biz.emergency.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.emergency.server.entity.EmergencyFileType;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyFileTypeModifyParam;
import com.guhecloud.cube.biz.emergency.server.param.EmergencyFileTypeQueryParam;

import java.util.List;

public interface EmergencyFileTypeService extends IService<EmergencyFileType> {

    Long save(EmergencyFileTypeModifyParam param);

    void update(Long id, EmergencyFileTypeModifyParam param);

    EmergencyFileType getById(Long id);

    Page<EmergencyFileType> page(EmergencyFileTypeQueryParam query);

    List<EmergencyFileType> list(EmergencyFileTypeQueryParam query);

    void remove(Long id);
}
