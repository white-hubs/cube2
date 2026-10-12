package com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.handler;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteModeEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.ExecuteStatusEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.LinkageActionTypeEnum;
import com.guhecloud.apps.linkage.metadata.enumeration.VideoActionTypeEnum;
import com.guhecloud.cube.base.videogw.client.VideoClient;
import com.guhecloud.cube.base.videogw.client.command.VideoCaptureCommand;
import com.guhecloud.cube.base.videogw.client.command.VideoSnapCaptureCommand;
import com.guhecloud.cube.base.videogw.client.dto.VideoCaptureDTO;
import com.guhecloud.cube.base.videogw.client.dto.VideoSnapCaptureDTO;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.Event;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.EventAction;
import com.guhecloud.cube.biz.linkage.server.linkage.service.EventService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.LinkageActionAbstractHandler;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.dto.request.LinkageActionExecuteRequest;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.dto.request.LinkageActionValidRequest;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.dto.LinkageVideoDTO;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.entity.EventVideo;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.entity.EventVideoFilePath;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.entity.EventVideoRecord;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.enumeration.VideoFileSourceEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.param.VideoCtrlCommandParam;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.service.EventVideoFilePathService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.service.EventVideoRecordService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.service.EventVideoService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.videodevicestrategy.VideoDeviceDynamicHandler;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.ExecutorContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.guhecloud.cube.biz.linkage.server.constants.FileSceneConstant.ALARM_SNAP_CAPTURE_PRE_FIX;
import static com.guhecloud.cube.biz.linkage.server.constants.FileSceneConstant.ALARM_VIDEOTAPE_PRE_FIX;

/**
 * 摄像机动作
 *
 * @author lanchun
 * @date 2022/7/4 15:57
 */
@Slf4j
@Component
public class LinkageVideoHandler extends LinkageActionAbstractHandler {

    @Autowired
    private EventVideoService eventVideoService;

    @Autowired
    private EventVideoRecordService eventVideoRecordService;

    @Autowired
    private EventVideoFilePathService eventVideoFilePathService;

    @Autowired
    private VideoClient videoClient;

    @Autowired
    private VideoDeviceDynamicHandler videoDeviceDynamicHandler;

    public LinkageVideoHandler() {
        super(LinkageActionTypeEnum.VIDEO_DEVICE_ACTION);
    }

    public List<LinkageVideoDTO> buildVideo(Object o) {
        return JsonUtils.deserialize(o, new TypeReference<List<LinkageVideoDTO>>() {
        });
    }

    @Override
    public ExecuteStatusEnum eventActionExecute(LinkageActionExecuteRequest request) {
        ExecuteStatusEnum executeStatusEnum = ExecuteStatusEnum.SUCCESSFUL_EXECUTION;
        try {
            if (Objects.isNull(request.getLinkageActionDetailRequest().getActionConfig())) {
                //动作执行结果
                return executeStatusEnum;
            }
            List<LinkageVideoDTO> linkageVideoDTOS = this.buildVideo(request.getLinkageActionDetailRequest().getActionConfig());
            List<LinkageVideoDTO> deviceLinkageVideoDTOS = new ArrayList<>();
            linkageVideoDTOS.forEach(linkageVideoDTO -> {
                //通过不同类型策略获取执行的监控设备列表
                deviceLinkageVideoDTOS.addAll(videoDeviceDynamicHandler.getVideoDevice(request.getEvent()
                                                                                               .getEventConfig(), linkageVideoDTO));
            });
            //保存联动摄像机动作记录
            List<EventVideoRecord> eventVideoRecords = this.eventVideoService.saveAndReturnRecords(request.getEvent(), request
                    .getLinkageActionDetailRequest().getLinkageId(), request.getLinkageActionDetailRequest()
                                                                                                           .getEventLinkageId(), deviceLinkageVideoDTOS);
            Integer tenant = SessionContextHolder.getRequester().getTenant();
            //并行执行摄像机动作
            eventVideoRecords.stream().filter(eventVideoRecord -> ExecuteModeEnum.AUTOMATIC == eventVideoRecord.getExecuteMode())
                    .forEach(eventVideoRecord -> ExecutorContextHolder.run(() -> {
                        //传递租户信息
                        SessionContextHolder.requestTenant(tenant);
                        this.executeVideoAction(eventVideoRecord, request.getEvent());
                    }));
        } catch (Exception e) {
            executeStatusEnum = ExecuteStatusEnum.FAILED_EXECUTION;
            log.error("执行摄像机动作失败,事件主键为:{}---->堆栈信息:", request.getEvent().getId(), e);
        }
        //动作执行结果
        return executeStatusEnum;
    }

    public List<LinkageVideoDTO> getEventVideos(Event event, EventAction eventAction) {
        if (Objects.isNull(eventAction.getActionConfig())) {
            //动作执行结果
            return Collections.emptyList();
        }
        List<LinkageVideoDTO> linkageVideoDTOS = this.buildVideo(JsonUtils.deserialize(eventAction.getActionConfig(), Object.class));
        List<LinkageVideoDTO> deviceLinkageVideoDTOS = new ArrayList<>();
        linkageVideoDTOS.forEach(linkageVideoDTO -> {
            //通过不同类型策略获取执行的监控设备列表
            deviceLinkageVideoDTOS.addAll(videoDeviceDynamicHandler.getVideoDevice(event.getEventConfig(), linkageVideoDTO));
        });
        return deviceLinkageVideoDTOS;
    }

    @Override
    public void verification(LinkageActionValidRequest request) {
        if (StringUtils.isEmpty(request.getActionConfig()) || "[]".equalsIgnoreCase(request.getActionConfig())) {
            //动作执行结果
            return;
        }
        List<LinkageVideoDTO> linkageVideoDTOS = this.buildVideo(request.getActionConfig());
        Assert.isParameterError(CollectionUtils.isEmpty(linkageVideoDTOS), "摄像机抓拍配置不能为空");
        linkageVideoDTOS.forEach(linkageVideoDTO -> Assert.isParameterError(
                Objects.isNull(linkageVideoDTO.getId()),
                "摄像机抓拍配置不能为空"));
    }

    /**
     * 手动控制
     *
     * @param param
     *
     * @return
     */
    public Long videoCtrl(VideoCtrlCommandParam param) {
        try {
            LocalDateTime executeDate = DateUtils.now();
            EventVideo eventVideo = this.eventVideoService.getById(param.getEventVideoId());
            Assert.isNotFound(eventVideo, "不存在该摄像机");
            EventService eventService = ApplicationContextHolder.getBean(EventService.class);
            Event event = eventService.getById(eventVideo.getEventId());
            EventVideoRecord eventVideoRecord = ObjectUtils.copying(eventVideo, EventVideoRecord.class);
            eventVideoRecord.setId(IdWorker.getId());
            eventVideoRecord.setExecuteStatus(ExecuteStatusEnum.IN_EXECUTION);
            eventVideoRecord.setExecuteTime(executeDate);
            eventVideoRecord.setOperatorBy(SecurityContextHolder.getPrincipal().getAccount());
            eventVideoRecord.setOperatorName(SecurityContextHolder.getPrincipal().getName());
            eventVideoRecord.setOperatorTime(executeDate);
            this.eventVideoRecordService.save(eventVideoRecord);
            Integer tenant = SessionContextHolder.getRequester().getTenant();
            //执行
            ExecutorContextHolder.run(() -> {
                //传递租户信息
                SessionContextHolder.requestTenant(tenant);
                this.executeVideoAction(eventVideoRecord, event);
            });
            return eventVideoRecord.getId();
        } catch (Exception e) {
            log.error("执行摄像机动作失败", e);
            throw new ServiceException("执行失败");
        }
    }

    public void executeVideoAction(EventVideoRecord eventVideoRecord, Event event) {
        if (VideoActionTypeEnum.PHOTOGRAPH == eventVideoRecord.getType()) {
            photograph(eventVideoRecord, event);
        } else if (VideoActionTypeEnum.VIDEOTAPE == eventVideoRecord.getType()) {
            videotape(eventVideoRecord, event);
        }
    }

    /**
     * 摄像机抓拍动作
     *
     * @param eventVideoRecord
     * @param event
     *
     * @return
     */
    public void photograph(EventVideoRecord eventVideoRecord, Event event) {
        eventVideoRecord.setExecuteStatus(ExecuteStatusEnum.SUCCESSFUL_EXECUTION);
        int successNum = 0;
        VideoSnapCaptureCommand videoSnapCaptureCommand = new VideoSnapCaptureCommand();
        videoSnapCaptureCommand.setLocallyStored(true);
        videoSnapCaptureCommand.setSceneCode(String.format(ALARM_SNAP_CAPTURE_PRE_FIX, event.getApplicationCode()));
        for (int i = 1; i <= eventVideoRecord.getDurationTime(); i++) {
            try {
                long videoFilePathId = IdWorker.getId();
                videoSnapCaptureCommand.setId(videoFilePathId);
                //执行抓拍动作
                VideoSnapCaptureDTO videoSnapCaptureDTO = this.videoClient.videoSnapCapture(eventVideoRecord.getDeviceId()
                                                                                                    .toString(), videoSnapCaptureCommand);
                if (StringUtils.isNotEmpty(videoSnapCaptureDTO.getPreviewUrl())) {
                    EventVideoFilePath eventVideoFilePath = ObjectUtils.copying(eventVideoRecord, EventVideoFilePath.class);
                    eventVideoFilePath.setId(videoFilePathId);
                    eventVideoFilePath.setIntranetFilePath(videoSnapCaptureDTO.getPreviewUrl());
                    eventVideoFilePath.setExtranetFilePath(videoSnapCaptureDTO.getPreviewUrl());
                    eventVideoFilePath.setSource(VideoFileSourceEnum.VIDEO_GW_FILE);
                    eventVideoFilePath.setExecuteTime(DateUtils.now());
                    this.eventVideoFilePathService.save(eventVideoFilePath);
                }
                successNum++;
            } catch (Exception e) {
                log.error("摄像机" + eventVideoRecord.getDeviceName() + "抓拍失败", e);
            }
            //休眠一定时间再进行抓拍
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                // 捕获中断异常
                log.error("video snap shot Thread was interrupted during sleep.");
                Thread.currentThread().interrupt();
            }

        }
        if (successNum == 0) {
            eventVideoRecord.setExecuteStatus(ExecuteStatusEnum.FAILED_EXECUTION);
        }
        this.eventVideoRecordService.updateById(eventVideoRecord);
    }

    /**
     * 摄像机录像动作
     *
     * @param eventVideoRecord
     * @param event
     *
     * @return
     */
    public EventVideoRecord videotape(EventVideoRecord eventVideoRecord, Event event) {
        LocalDateTime executeTime = DateUtils.now();
        ExecuteStatusEnum executeStatus = ExecuteStatusEnum.SUCCESSFUL_EXECUTION;
        try {
            long id = IdWorker.getId();
            VideoCaptureCommand videoCaptureCommand = new VideoCaptureCommand();
            videoCaptureCommand.setId(id);
            videoCaptureCommand.setLocallyStored(Boolean.TRUE);
            videoCaptureCommand.setStartTime(executeTime);
            videoCaptureCommand.setSceneCode(String.format(ALARM_VIDEOTAPE_PRE_FIX, event.getApplicationCode()));
            videoCaptureCommand.setLength(eventVideoRecord.getTime());// 默认时长30s
            videoCaptureCommand.setDelayTime(eventVideoRecord.getTime() + 10);// 默认延时40s
            //执行抓拍动作
            VideoCaptureDTO videoCaptureDTO = this.videoClient.videoCapture(eventVideoRecord.getDeviceId().toString(), videoCaptureCommand);
            if (StringUtils.isNotEmpty(videoCaptureDTO.getCaptureUrl())) {
                EventVideoFilePath eventVideoFilePath = ObjectUtils.copying(eventVideoRecord, EventVideoFilePath.class);
                eventVideoFilePath.setId(id);
                eventVideoFilePath.setIntranetFilePath(videoCaptureDTO.getCaptureUrl());
                eventVideoFilePath.setExtranetFilePath(videoCaptureDTO.getCaptureUrl());
                eventVideoFilePath.setSource(VideoFileSourceEnum.VIDEO_GW_FILE);
                eventVideoFilePath.setExecuteTime(DateUtils.now());
                this.eventVideoFilePathService.save(eventVideoFilePath);
            }
        } catch (Exception e) {
            executeStatus = ExecuteStatusEnum.FAILED_EXECUTION;
            log.error("摄像机{}录像失败", eventVideoRecord.getDeviceName(), e);
        }
        eventVideoRecord.setExecuteStatus(executeStatus);
        this.eventVideoRecordService.updateById(eventVideoRecord);
        return eventVideoRecord;
    }
}
