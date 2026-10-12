package com.guhecloud.cube.base.alarm.server.sat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.alarm.server.sat.entity.SatAlarmLevel;
import com.guhecloud.cube.base.alarm.server.sat.param.SatAlarmLevelModifyParam;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface SatAlarmLevelService extends IService<SatAlarmLevel> {

    /**
     * 报警级别新增
     *
     * @param param
     *
     * @return
     */
    Long save(SatAlarmLevelModifyParam param);

    /**
     * 报警级别修改
     *
     * @param id
     * @param param
     */
    void update(Long id, SatAlarmLevelModifyParam param);

    /**
     * 报警级别详情
     *
     * @param id
     *
     * @return
     */
    SatAlarmLevel getById(Serializable id);

    /**
     * 报警级别列表
     *
     * @return
     */
    List<SatAlarmLevel> listAlarmLevels(String keyword);

    /**
     * 报警级别列表
     *
     * @param ids
     *
     * @return
     */
    List<SatAlarmLevel> listAlarmLevels(Collection<Long> ids);

    /**
     * 报警级别删除
     *
     * @param id
     */
    void remove(Long id);
}
