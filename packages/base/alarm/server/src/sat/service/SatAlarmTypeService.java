package com.guhecloud.cube.base.alarm.server.sat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.alarm.server.sat.entity.SatAlarmType;
import com.guhecloud.cube.base.alarm.server.sat.param.SatAlarmTypeModifyParam;
import com.guhecloud.cube.base.alarm.server.sat.param.SatAlarmTypeQueryParam;

import java.io.Serializable;
import java.util.List;

public interface SatAlarmTypeService extends IService<SatAlarmType> {

    /**
     * 报警类型新增
     *
     * @param param
     *
     * @return
     */
    Long save(SatAlarmTypeModifyParam param);

    /**
     * 报警类型更新
     *
     * @param param
     *
     * @return
     */
    void update(Long id, SatAlarmTypeModifyParam param);

    /**
     * 报警类型详情
     *
     * @param id
     *
     * @return
     */
    SatAlarmType getById(Serializable id);

    /**
     * 报警类型详情
     *
     * @param applicationCode
     *
     * @return
     */
    SatAlarmType getByApplicationCode(String applicationCode);

    /**
     * 报警类型列表
     *
     * @param param
     *
     * @return
     */
    List<SatAlarmType> listAlarmTypes(SatAlarmTypeQueryParam param);

    /**
     * 报警类型删除
     *
     * @param id
     */
    void remove(Long id);
}
