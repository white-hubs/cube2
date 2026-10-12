package com.guhecloud.cube.biz.door.server.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.door.server.entity.EventRecord;
import com.guhecloud.cube.biz.door.server.param.StatisticsParam;
import com.guhecloud.cube.biz.door.server.queryparam.AccessEventQueryParam;
import com.guhecloud.cube.biz.door.server.queryparam.EventRecordQueryParam;
import com.guhecloud.cube.biz.door.server.vo.CommonStatVO;
import com.guhecloud.cube.biz.door.server.vo.DoorEventsVo;
import com.guhecloud.cube.biz.door.server.vo.DoorStatisticsVo;
import com.guhecloud.cube.biz.door.server.vo.EventRecordVo;
import com.guhecloud.cube.biz.door.server.vo.RankingVo;
import com.guhecloud.cube.biz.door.server.vo.TimeTrendVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface EventRecordMapper extends BaseMapper<EventRecord> {
    Integer getEventRecordNum(@Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);

    List<RankingVo> getDoorRanking(@Param("beginTime") LocalDateTime beginTime, @Param("endTime") LocalDateTime endTime);

    List<DoorEventsVo> getDoorEvents();

    Page<EventRecordVo> getDoorEventRecord(Page page, @Param("param") EventRecordQueryParam eventRecordQueryParam);

    Page<EventRecordVo> getVisualizationDoorEventRecord(Page page, @Param("param") EventRecordQueryParam eventRecordQueryParam);

    Page<EventRecordVo> getAbnormalEvent(Page page, @Param("param") EventRecordQueryParam eventRecordQueryParam);

    DoorStatisticsVo getEventInOrOut(@Param("param") StatisticsParam param);

    List<TimeTrendVo> getInAndOutTrend(@Param("param") StatisticsParam param, @Param("timeFlag") String timeFlag);

    Integer getInAndOutTotal(@Param("param") StatisticsParam param);

    @InterceptorIgnore(illegalSql = "true", tenantLine = "true")
    void insertOrUpdateDoorEvent(List<EventRecord> dataList);

    List<CommonStatVO> getEventStatByName(@Param("param") AccessEventQueryParam param);
}
