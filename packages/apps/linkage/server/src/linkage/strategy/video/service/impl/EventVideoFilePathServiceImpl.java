package com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.apps.linkage.metadata.enumeration.VideoActionTypeEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.dao.EventVideoFilePathMapper;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.entity.EventVideoFilePath;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.enumeration.VideoFileSourceEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.service.EventVideoFilePathService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventVideoFilePathServiceImpl extends ServiceImpl<EventVideoFilePathMapper, EventVideoFilePath> implements EventVideoFilePathService {

    @Override
    public Long saveIotFile(@NonNull Long eventId, @NonNull Long deviceId, @NonNull String deviceCode, @NonNull String deviceName,
                            @NonNull String extranetFilePath, @NonNull String intranetFilePath
                           ) {
        return saveIotFile(eventId, deviceId, deviceCode, deviceName, extranetFilePath, intranetFilePath, VideoActionTypeEnum.PHOTOGRAPH);
    }

    @Override
    public Long saveIotFile(Long eventId, Long deviceId, String deviceCode, String deviceName, String extranetFilePath,
                            String intranetFilePath, VideoActionTypeEnum type
                           ) {
        EventVideoFilePath eventVideoFilePath = EventVideoFilePath.builder()
                .deviceId(deviceId)
                .deviceCode(deviceCode)
                .deviceName(deviceName)
                .type(type)
                .eventId(eventId)
                .extranetFilePath(extranetFilePath)
                .intranetFilePath(intranetFilePath)
                .source(VideoFileSourceEnum.IOT_FILE)
                .build();
        this.baseMapper.insert(eventVideoFilePath);
        return eventVideoFilePath.getId();
    }

    @Override
    public List<EventVideoFilePath> listByEventId(@NonNull Long eventId) {
        return this.lambdaQuery().eq(EventVideoFilePath::getEventId, eventId).orderByAsc(EventVideoFilePath::getExecuteTime).list();
    }
}
