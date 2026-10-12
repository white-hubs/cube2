package com.guhecloud.cube.biz.door.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.door.server.dao.TypeManageMapper;
import com.guhecloud.cube.biz.door.server.entity.EventRecord;
import com.guhecloud.cube.biz.door.server.entity.TypeManage;
import com.guhecloud.cube.biz.door.server.param.EventRecordParam;
import com.guhecloud.cube.biz.door.server.queryparam.EventRecordQueryParam;
import com.guhecloud.cube.biz.door.server.service.EventRecordService;
import com.guhecloud.cube.biz.door.server.vo.EventRecordVo;
import com.guhecloud.cube.biz.door.server.vo.TimeTrendVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Description 门禁事件
 * @Author lrj
 * @Param
 * @Return
 * @Exception
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DoorEventsController {
    private final EventRecordService eventRecordService;

    private final TypeManageMapper typeManageMapper;

    /**
     * @Description 门禁事件记录分页
     * @Author lrj
     */
    @GetMapping("/getDoorEventRecord")
    public Page<EventRecordVo> getDoorEventRecord(EventRecordQueryParam eventRecordQueryParam) {
        return eventRecordService.getDoorEventRecord(eventRecordQueryParam);
    }

    /**
     * @Description 当日趋势图
     * @Author lrj
     */
    @GetMapping("/getToDayEventTrend")
    public List<TimeTrendVo> getToDayEventTrend() {
        return eventRecordService.getToDayEventTrend();
    }

    /**
     * @Description 异常事件分页列表
     * @Author lrj
     */
    @GetMapping("/getAbnormalEvent")
    public Page<EventRecordVo> getAbnormalEvent(EventRecordQueryParam eventRecordQueryParam) {
        return eventRecordService.getAbnormalEvent(eventRecordQueryParam);
    }

    /**
     * @Description 临时接口
     * @Author lrj
     */
    @PostMapping("/public/saveEventRecord")
    public void saveEventRecord(@RequestBody EventRecordParam record) {
        List<TypeManage> typeManages = typeManageMapper.selectList(null);
        Map<String, TypeManage> collect = typeManages.stream()
                .collect(Collectors.toMap(typeManage -> typeManage.getTypeCode().toString(), typeManage -> typeManage, (o, n) -> n));
        TypeManage typeManage = collect.get(record.getEventType());
        String result = typeManage.getTypeResult();
        EventRecord build = EventRecord.builder()
                .eventId(record.getEventId())
                .doorIndexCode(record.getDoorIndexCode())
                .doorName(record.getDoorName())
                .eventType(typeManage.getName())
                .personId(record.getPersonId())
                .personName(record.getPersonName())
                .inAndOutType(String.valueOf(record.getInAndOutType()))
                .eventName(record.getEventName())
                .eventTime(record.getEventTime())
                .eventWay(record.getEventWay())
                .eventResult(result)
                .pictureFileId(record.getPictureFileId())
                .build();
        eventRecordService.save(build);
    }
}
