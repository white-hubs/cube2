package com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.controller;

import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.entity.EventVideoRecord;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.service.EventVideoRecordService;
import com.guhecloud.cube.biz.linkage.server.linkage.strategy.video.vo.EventVideoRecordVO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

/**
 * 事件摄像机执行记录管理接口
 *
 * @author lanchun
 * @date 2022/7/5 16:10
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EventVideoRecordController {

    private final EventVideoRecordService eventVideoRecordService;

    /**
     * 根据事件获取摄像机动作执行记录列表
     *
     * @param eventId
     *
     * @return
     */
    @GetMapping("/event-video-records/{eventId}")
    public List<EventVideoRecordVO> listByEventId(@PathVariable("eventId") Long eventId) {
        List<EventVideoRecord> eventVideoRecords = this.eventVideoRecordService.listByEventId(eventId);
        if (CollectionUtils.isEmpty(eventVideoRecords)) {
            return Collections.emptyList();
        }
        return ObjectUtils.copying(eventVideoRecords, EventVideoRecordVO.class);
    }
}
