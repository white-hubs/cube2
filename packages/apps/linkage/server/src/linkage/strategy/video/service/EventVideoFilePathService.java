package com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.apps.linkage.metadata.enumeration.VideoActionTypeEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.entity.EventVideoFilePath;

import java.util.List;

public interface EventVideoFilePathService extends IService<EventVideoFilePath> {

    Long saveIotFile(Long eventId, Long deviceId, String deviceCode, String deviceName, String extranetFilePath, String intranetFilePath);

    Long saveIotFile(Long eventId, Long deviceId, String deviceCode, String deviceName, String extranetFilePath, String intranetFilePath,
                     VideoActionTypeEnum type
                    );

    List<EventVideoFilePath> listByEventId(Long eventId);
}
