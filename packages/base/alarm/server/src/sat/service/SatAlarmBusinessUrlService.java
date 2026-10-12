package com.guhecloud.cube.base.alarm.server.sat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.alarm.server.sat.entity.SatAlarmBusinessUrl;
import com.guhecloud.cube.base.alarm.server.sat.param.SatAlarmBusinessUrlModifyParam;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface SatAlarmBusinessUrlService extends IService<SatAlarmBusinessUrl> {

    Long save(SatAlarmBusinessUrlModifyParam param);

    void update(Long id, SatAlarmBusinessUrlModifyParam param);

    SatAlarmBusinessUrl getById(Serializable id);

    List<SatAlarmBusinessUrl> listByBusinessTypes(Collection<String> businessTypes, String terminal);

    SatAlarmBusinessUrl getSatAlarmBusinessUrl(String businessType, String applicationCode, String terminal);
}
