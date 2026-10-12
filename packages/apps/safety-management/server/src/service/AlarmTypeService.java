package com.guhecloud.cube.srv.gen.safety.management.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.safety.management.dto.request.query.AlarmTypeQuery;
import com.guhecloud.cube.srv.gen.safety.management.po.AlarmTypePO;

import java.util.List;

public interface AlarmTypeService extends IService<AlarmTypePO> {

    Page<AlarmTypePO> getPage(AlarmTypeQuery query);

    List<AlarmTypePO> getAlarmTypes(AlarmTypeQuery query);
}
