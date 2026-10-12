package com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.apps.linkage.metadata.enumeration.LinkageActionTypeEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Event;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.EventAction;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventActionService;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.dto.LinkageVideoDTO;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.entity.EventVideo;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.handler.LinkageVideoHandler;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.param.VideoCtrlCommandParam;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.service.EventVideoService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.vo.EventVideoVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 事件摄像机管理接口
 *
 * @author lanchun
 * @date 2022/7/5 16:10
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventVideoController {

    private final EventService eventService;

    private final EventVideoService eventVideoService;

    private final LinkageVideoHandler linkageVideoHandler;

    private final EventActionService eventActionService;

    /**
     * 摄像机控制
     *
     * @param param
     */
    @PostMapping("/event-video/ctl")
    public Long videoCtrlCommandParam(@Valid @RequestBody VideoCtrlCommandParam param) {
        return this.linkageVideoHandler.videoCtrl(param);
    }

    /**
     * 根据事件获取摄像机动作列表
     *
     * @param eventId
     *
     * @return
     */
    @GetMapping("/event-video/{eventId}")
    public List<EventVideoVO> listByEventId(@PathVariable("eventId") Long eventId) {
        List<EventVideo> eventVideos = this.eventVideoService.listByEventId(eventId);
        if (CollectionUtils.isEmpty(eventVideos)) {
            //事件
            Event event = this.eventService.getById(eventId);
            //如果事件没有关联摄像机（有可能是异步摄像机动作还未执行完成）,再查看事件是否配置了摄像机动作
            EventAction eventAction = this.eventActionService.getOne(Wrappers.<EventAction>lambdaQuery()
                                                                             .eq(EventAction::getEventId, eventId)
                                                                             .eq(EventAction::getActionType, LinkageActionTypeEnum.VIDEO_DEVICE_ACTION));
            if (Objects.isNull(eventAction)) {
                return Collections.emptyList();
            }
            List<LinkageVideoDTO> linkageVideoDTOS = linkageVideoHandler.getEventVideos(event, eventAction);
            linkageVideoDTOS.stream().forEach(linkageVideoDTO -> {
                EventVideo eventVideo = EventVideo.builder()
                        .eventId(eventId)
                        .deviceId(linkageVideoDTO.getId())
                        .deviceCode(linkageVideoDTO.getCode())
                        .deviceName(linkageVideoDTO.getName())
                        .token(linkageVideoDTO.getToken())
                        .executeMode(linkageVideoDTO.getExecuteMode())
                        .type(linkageVideoDTO.getType())
                        .time(linkageVideoDTO.getTime())
                        .timeUnit(linkageVideoDTO.getTimeUnit())
                        .intervalTime(linkageVideoDTO.getIntervalTime())
                        .durationTime(linkageVideoDTO.getDurationTime())
                        .build();
                eventVideos.add(eventVideo);
            });
        }
        List<EventVideoVO> eventVideoVOS = ObjectUtils.copying(eventVideos, EventVideoVO.class);
        return eventVideoVOS.stream().collect(Collectors.
                                                      collectingAndThen(
                                                              Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(EventVideoVO::getDeviceId))), ArrayList::new)
                                             );
    }
}
