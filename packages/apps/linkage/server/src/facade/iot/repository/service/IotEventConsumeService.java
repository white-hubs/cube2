package com.guhecloud.cube.biz.linkage.server.facade.iot.repository.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.linkage.server.facade.iot.repository.entity.IotEventConsume;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface IotEventConsumeService extends IService<IotEventConsume> {

    Long save(String iotEventInstanceId, String additional, Boolean consumed);

    void save(String iotEventInstanceId, Set<String> additions);

    List<IotEventConsume> getStayEventConsume(Integer size);

    List<IotEventConsume> getStayEventConsume(LocalDateTime beginTime);
}
