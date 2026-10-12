package com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.entity.EventIotDeviceProRecord;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.devicectl.enumeration.AttributeControlExecuteResultEnum;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

public interface EventIotDeviceProRecordService extends IService<EventIotDeviceProRecord> {

    /**
     * 设备控制记录详情
     *
     * @param id
     *
     * @return
     */
    EventIotDeviceProRecord getById(Serializable id);

    /**
     * 批量修改执行状态
     *
     * @param ids
     * @param executeStatus
     * @param executeStatusDescribe
     */
    void updateCommandStatus(Set<Long> ids, AttributeControlExecuteResultEnum executeStatus, String executeStatusDescribe);

    /**
     * 修改执行状态
     *
     * @param id
     * @param executeStatus
     * @param executeStatusDescribe
     */
    void updateCommandStatus(Long id, AttributeControlExecuteResultEnum executeStatus, String executeStatusDescribe);

    /**
     * 通过事件查询执行记录列表
     *
     * @param eventId
     *
     * @return
     */
    List<EventIotDeviceProRecord> listByEventId(Long eventId);
}
