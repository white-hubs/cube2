package com.guhecloud.cube.biz.collection.analysis.gm.controller;

import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.ParkSettleCategoryDayDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.ParkSettleCategoryWeekDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.TransportDayDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.TransportWeekDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.service.GmParkService;
import com.guhecloud.platform.base.model.Period;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 来货运转统计
 *
 * @Author: lijie
 * @CreateTime: 2024-12-16  11:43
 * @Description:
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GmParkController {
    private final GmParkService gmParkService;

    /**
     * 昨日来货统计
     */
    @GetMapping("/get/goods/yesterday")
    public ParkSettleCategoryDayDTO getGoodsYesterday(LocalDateTime deliveryDate) {
        return gmParkService.getGoodsYesterday(deliveryDate);
    }

    /**
     * 近一周来货趋势图
     */
    @GetMapping("/get/goods/week")
    public List<ParkSettleCategoryWeekDTO> getGoodsWeek(Period period) {
        return gmParkService.getGoodsWeek(period);
    }

    /**
     * 昨日转运统计
     */
    @GetMapping("/get/transport/yesterday")
    public TransportDayDTO getTransportYesterday(LocalDateTime deliveryDate) {
        return gmParkService.getTransportYesterday(deliveryDate);
    }

    /**
     * 最近一周转运件数趋势
     */
    @GetMapping("/get/transport/week")
    public List<TransportWeekDTO> getTransportWeek(Period period) {
        return gmParkService.getTransportWeek(period);
    }
}
