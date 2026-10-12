package com.guhecloud.cube.biz.door.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.door.server.entity.EventRecord;
import com.guhecloud.cube.biz.door.server.param.PassedNumParam;
import com.guhecloud.cube.biz.door.server.param.StatisticsParam;
import com.guhecloud.cube.biz.door.server.queryparam.EventRecordQueryParam;
import com.guhecloud.cube.biz.door.server.vo.DoorEventsVo;
import com.guhecloud.cube.biz.door.server.vo.DoorStatisticsVo;
import com.guhecloud.cube.biz.door.server.vo.EventRecordVo;
import com.guhecloud.cube.biz.door.server.vo.RankingVo;
import com.guhecloud.cube.biz.door.server.vo.TimeTrendVo;

import java.util.List;
import java.util.Map;

public interface EventRecordService extends IService<EventRecord> {
    long passedTodayNum();

    List<TimeTrendVo> getDoorTend(String timeFlag);

    List<RankingVo> getDoorRanking();

    List<DoorEventsVo> getDoorEvents();

    Page<EventRecordVo> getDoorEventRecord(EventRecordQueryParam eventRecordQueryParam);

    Page<EventRecordVo> getvisualizationDoorEventRecord(EventRecordQueryParam eventRecordQueryParam);

    List<TimeTrendVo> getToDayEventTrend();

    Page<EventRecordVo> getAbnormalEvent(EventRecordQueryParam eventRecordQueryParam);

    List<String> getEventIds(List<String> ids);

    DoorStatisticsVo getEventInOrOut(StatisticsParam param);

    Map<String, Object> getInAndOutTrend(StatisticsParam param);

    void insertOrUpdate(EventRecord event);

    List<EventRecord> getPatrolRecord(StatisticsParam param);

    long passedNumByDay(PassedNumParam param);
}
