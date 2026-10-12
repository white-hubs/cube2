package com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.entity.IotEventFileConsume;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface IotEventFileConsumeService extends IService<IotEventFileConsume> {

    Long save(String iotEventInstanceId, String additional);

    void save(String iotEventInstanceId, Set<String> additions);

    List<IotEventFileConsume> getStayEventFileConsume(Integer size);

    List<IotEventFileConsume> getStayEventFileConsume(LocalDateTime beginTime);

    List<IotEventFileConsume> getStayEventFileConsume(LocalDateTime beginTime, int batchSize);
}
