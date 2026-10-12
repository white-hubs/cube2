package com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.controller;

import com.guhecloud.cube.base.videogw.client.VideoClient;
import com.guhecloud.cube.base.videogw.client.dto.VideoFileDTO;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.entity.EventVideoFilePath;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.enumeration.VideoFileSourceEnum;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.service.EventVideoFilePathService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.vo.EventVideoFilePathVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 事件摄像机执行附件管理接口
 *
 * @author lanchun
 * @date 2022/7/5 16:10
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventVideoFilePathController {

    private final EventVideoFilePathService eventVideoFilePathService;
    private final VideoClient videoClient;

    /**
     * 根据事件获取摄像机动作执行附件列表
     *
     * @param eventId
     *
     * @return
     */
    @GetMapping("/event-video-file-paths/{eventId}")
    public List<EventVideoFilePathVO> listByEventId(@PathVariable("eventId") Long eventId) {
        List<EventVideoFilePath> eventVideoFilePaths = this.eventVideoFilePathService.listByEventId(eventId);
        if (CollectionUtils.isEmpty(eventVideoFilePaths)) {
            return Collections.emptyList();
        }
        List<EventVideoFilePathVO> eventVideoFilePathVOS = ObjectUtils.copying(eventVideoFilePaths, EventVideoFilePathVO.class);
        //获取从videogw中操作的文件
        List<EventVideoFilePath> videoGateWayVideoFiles = eventVideoFilePaths.stream()
                .filter(eventVideoFilePath -> VideoFileSourceEnum.VIDEO_GW_FILE == eventVideoFilePath.getSource())
                .collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(videoGateWayVideoFiles)) {
            Set<Long> videoGatewayFileIds = videoGateWayVideoFiles.stream().map(EventVideoFilePath::getId)
                    .collect(Collectors.toSet());
            Map<Long, VideoFileDTO> videoFileDTOMap = this.videoClient.getVideoFilesByIds(videoGatewayFileIds).stream()
                    .collect(Collectors.toMap(VideoFileDTO::getId, videoFileDTO -> videoFileDTO, (o, n) -> n));
            Iterator<EventVideoFilePathVO> iterator = eventVideoFilePathVOS.iterator();
            while (iterator.hasNext()) {
                EventVideoFilePathVO eventVideoFilePathVO = iterator.next();
                if (Objects.nonNull(eventVideoFilePathVO.getId()) &&
                    videoFileDTOMap.containsKey(eventVideoFilePathVO.getId())) {
                    VideoFileDTO videoFileDTO = videoFileDTOMap.get(eventVideoFilePathVO.getId());
                    boolean success = "LOCAL_SUCCESS_STORAGE".equals(videoFileDTO.getLocallyStoredStatus());
                    if (success) {
                        eventVideoFilePathVO.setIntranetFilePath(videoFileDTO.getPath());
                        eventVideoFilePathVO.setExtranetFilePath(videoFileDTO.getPath());
                    } else {
                        iterator.remove();
                    }
                }
            }
        }
        return eventVideoFilePathVOS;

    }
}
