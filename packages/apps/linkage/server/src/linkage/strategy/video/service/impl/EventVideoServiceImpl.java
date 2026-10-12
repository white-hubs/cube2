package com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteModeEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteStatusEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Event;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.dao.EventVideoMapper;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.dto.LinkageVideoDTO;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.entity.EventVideo;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.entity.EventVideoRecord;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.service.EventVideoRecordService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.service.EventVideoService;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventVideoServiceImpl extends ServiceImpl<EventVideoMapper, EventVideo> implements EventVideoService {

    private final EventVideoRecordService eventVideoRecordService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<EventVideoRecord> saveAndReturnRecords(@NonNull Event event, @NonNull Long linkageId, @NonNull Long eventLinkageId,
                                                       @NonNull List<LinkageVideoDTO> linkageVideoDTOS
                                                      ) {
        LocalDateTime executeDate = DateUtils.now();
        //事件摄像机动作
        List<EventVideo> eventVideos = new ArrayList<>();
        //事件摄像机动作记录
        List<EventVideoRecord> eventVideoRecords = new ArrayList<>();
        Principal principal = SecurityContextHolder.getPrincipal(true);
        linkageVideoDTOS.stream().forEach(linkageVideoDTO -> {
            //设备不为空时存入
            if (Objects.nonNull(linkageVideoDTO.getId())) {
                EventVideo eventVideo = EventVideo.builder()
                        .eventLinkageId(eventLinkageId)
                        .eventId(event.getId())
                        .linkageId(linkageId)
                        .deviceId(linkageVideoDTO.getId())
                        .deviceCode(linkageVideoDTO.getCode())
                        .deviceName(linkageVideoDTO.getName())
                        .token(linkageVideoDTO.getToken())
                        .type(linkageVideoDTO.getType())
                        .time(linkageVideoDTO.getTime())
                        .timeUnit(linkageVideoDTO.getTimeUnit())
                        .intervalTime(linkageVideoDTO.getIntervalTime())
                        .durationTime(linkageVideoDTO.getDurationTime())
                        .executeMode(linkageVideoDTO.getExecuteMode())
                        .build();
                eventVideos.add(eventVideo);
                //自动执行
                if (ExecuteModeEnum.AUTOMATIC == linkageVideoDTO.getExecuteMode()) {
                    EventVideoRecord eventVideoRecord = ObjectUtils.copying(eventVideo, EventVideoRecord.class);
                    eventVideoRecord.setId(IdWorker.getId());
                    eventVideoRecord.setExecuteStatus(ExecuteStatusEnum.IN_EXECUTION);
                    eventVideoRecord.setOperatorBy(Objects.nonNull(principal) ? principal.getAccount() : null);
                    eventVideoRecord.setOperatorName(
                            Objects.nonNull(principal) && StringUtils.isNotEmpty(principal.getName()) ? principal.getName() : "系统");
                    eventVideoRecord.setExecuteTime(executeDate);
                    eventVideoRecord.setOperatorTime(executeDate);
                    eventVideoRecords.add(eventVideoRecord);
                }
            }
        });
        this.saveBatch(eventVideos);
        this.eventVideoRecordService.saveBatch(eventVideoRecords);
        return eventVideoRecords;
    }

    @Override
    public List<EventVideo> listByEventId(@NonNull Long eventId) {
        return this.lambdaQuery().eq(EventVideo::getEventId, eventId).list();
    }
}
