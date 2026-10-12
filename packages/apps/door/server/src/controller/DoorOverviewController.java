package com.guhecloud.cube.biz.door.server.controller;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.door.server.entity.DoorDevice;
import com.guhecloud.cube.biz.door.server.param.PassedNumParam;
import com.guhecloud.cube.biz.door.server.service.DoorDeviceService;
import com.guhecloud.cube.biz.door.server.service.EventRecordService;
import com.guhecloud.cube.biz.door.server.vo.DoorEventsVo;
import com.guhecloud.cube.biz.door.server.vo.RankingVo;
import com.guhecloud.cube.biz.door.server.vo.TimeTrendVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 门禁概览
 * @Author lrj
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DoorOverviewController {

    private final EventRecordService eventRecordService;

    private final DoorDeviceService doorDeviceService;

    /**
     * @Description 今日通行数
     * @Author lrj
     */
    @GetMapping("/passedTodayNum")
    public Long passedTodayNum() {
        return eventRecordService.passedTodayNum();
    }

    /**
     * @Description 通行数
     */
    @GetMapping("/passedNumByDay")
    public Long passedNumByDay(PassedNumParam param) {
        return eventRecordService.passedNumByDay(param);
    }

    /**
     * @Description 概览趋势图
     * @Author lrj
     */
    @GetMapping("/getDoorTend/{timeFlag}")
    public List<TimeTrendVo> getDoorTend(@PathVariable String timeFlag) {
        return eventRecordService.getDoorTend(timeFlag);
    }

    /**
     * @Description 门禁近30天排名
     * @Author lrj
     */
    @GetMapping("/getDoorRanking")
    public List<RankingVo> getDoorRanking() {
        return eventRecordService.getDoorRanking();
    }

    /**
     * @Description 最新20条门禁通行事件
     * @Author lrj
     */
    @GetMapping("/getDoorEvents")
    public List<DoorEventsVo> getDoorEvents() {
        return eventRecordService.getDoorEvents();
    }

    /**
     * @Description app首页统计
     * @Author lrj
     */
    @GetMapping("/getAppCountNum")
    public Map<String, Object> getAppCountNum() {
        HashMap<String, Object> remap = new HashMap<>();
        long count = doorDeviceService.count();
        long count1 = doorDeviceService.count(Wrappers.<DoorDevice>lambdaQuery().eq(DoorDevice::getStatus, "1"));
        long count2 = doorDeviceService.count(Wrappers.<DoorDevice>lambdaQuery().eq(DoorDevice::getStatus, "2"));
        long count3 = doorDeviceService.count(Wrappers.<DoorDevice>lambdaQuery().eq(DoorDevice::getStatus, "3"));
        remap.put("total", count);
        remap.put("onTotal", count1);
        remap.put("offTotal", count2);
        remap.put("offLineTotal", count3);
        return remap;
    }

}
