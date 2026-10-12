package com.guhecloud.cube.biz.linkage.server.linkage.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Event;
import com.guhecloud.cube.biz.linkage.server.linkage.param.EventQueryParam;
import com.guhecloud.cube.biz.linkage.server.rule.base.entity.Rule;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService extends IService<Event> {

    /**
     * 根据iot事件主键获取业务事件集合
     *
     * @param iotEventInstanceId
     *
     * @return
     */
    List<Event> getEventByIotEventId(String iotEventInstanceId);

    /**
     * 根据iot事件主键和设备主键获取业务事件集合
     *
     * @param iotEventInstanceId
     * @param deviceId
     *
     * @return
     */
    List<Event> getEventByIotEventIdAndDeviceId(String iotEventInstanceId, String deviceId);

    /**
     * 事件保存
     *
     * @param rule
     * @param triggerTargetId
     * @param triggerTime
     * @param eventConfig
     * @param iotEventInstanceId
     * @param snapshot
     *
     * @return
     */
    Event save(Rule rule, String triggerTargetId, LocalDateTime triggerTime, String eventConfig, String iotEventInstanceId,
               String snapshot
              );

    /**
     * 事件保存
     *
     * @param event
     *
     * @return
     */
    Event saveByEntity(Event event);

    /**
     * 事件分页列表
     *
     * @param param
     *
     * @return
     */
    Page<Event> page(EventQueryParam param);
}
