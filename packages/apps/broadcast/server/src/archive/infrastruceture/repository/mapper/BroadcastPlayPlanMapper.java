package com.guhecloud.cube.biz.subsystem.broadcast.server.archive.infrastruceture.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.domain.po.BroadcastPlayPlanPO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.interfaces.vo.BroadcastPlayRecordPageVO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.param.StatisticsParam;
import com.guhecloud.cube.biz.subsystem.broadcast.server.statistics.vo.BroadcastTemplatePlayStatisticsVO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.statistics.vo.StatisticsPlayRecordVo;
import com.guhecloud.cube.biz.subsystem.broadcast.server.statistics.vo.TrendVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BroadcastPlayPlanMapper extends BaseMapper<BroadcastPlayPlanPO> {

    List<BroadcastPlayPlanPO> selectPlanByDeviceId(@Param("deviceId") String deviceId);

    List<StatisticsPlayRecordVo> getStatisticsPlayRecordVoTop(@Param("param") StatisticsParam param);

    Integer getStatisticsPlayRecordVoNum(@Param("param") StatisticsParam param);

    List<BroadcastPlayRecordPageVO> getPlayRecordTop10(@Param("param") StatisticsParam param);

    List<BroadcastTemplatePlayStatisticsVO> selectTemplatePlayRecordStatistics(@Param("param") StatisticsParam param);

    List<TrendVo> getPlayRecordTrend(@Param("param") StatisticsParam param);

}
