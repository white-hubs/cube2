package com.guhecloud.cube.biz.subsystem.broadcast.server.statistics.controller;

import com.guhecloud.cube.biz.subsystem.broadcast.server.archive.interfaces.vo.BroadcastPlayRecordPageVO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.param.StatisticsParam;
import com.guhecloud.cube.biz.subsystem.broadcast.server.statistics.service.BroadcastPlayRecordStatisticsService;
import com.guhecloud.cube.biz.subsystem.broadcast.server.statistics.vo.BroadcastTemplatePlayStatisticsVO;
import com.guhecloud.cube.biz.subsystem.broadcast.server.statistics.vo.RecordNumVo;
import com.guhecloud.cube.biz.subsystem.broadcast.server.statistics.vo.StatisticsPlayRecordVo;
import com.guhecloud.cube.biz.subsystem.broadcast.server.statistics.vo.TrendVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BroadcastStatisticsController {
    private final BroadcastPlayRecordStatisticsService statisticsService;

    /**
     * @Description 播放记录top5
     * @Author lrj
     */
    @GetMapping("/statistics-play-record-top")
    public List<StatisticsPlayRecordVo> getStatisticsPlayRecordVoTop(StatisticsParam param) {
        return statisticsService.getStatisticsPlayRecordVoTop(param);
    }

    /**
     * @Description 播放记录总数
     * @Author lrj
     */
    @GetMapping("/record-total")
    public RecordNumVo getRecordNum(StatisticsParam param) {
        return statisticsService.getRecordNum(param);
    }

    /**
     * @Description 广播记录趋势
     * @Author lrj
     */
    @GetMapping("/record-trend")
    public List<TrendVo> getRecordTrendNum(StatisticsParam param) {
        return statisticsService.getRecordTrendNum(param);
    }

    /**
     * @Description 最新播放记录
     * @Author lrj
     */
    @GetMapping("/statistics-record")
    public List<BroadcastPlayRecordPageVO> getTop10(StatisticsParam param) {
        return statisticsService.getTop10(param);
    }

    /**
     * 广播模板播放统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/broadcast-template/play/statistics")
    public List<BroadcastTemplatePlayStatisticsVO> getTemplatePlayRecordStatistics(@Valid StatisticsParam param) {
        return statisticsService.getTemplatePlayRecordStatistics(param);
    }

}
