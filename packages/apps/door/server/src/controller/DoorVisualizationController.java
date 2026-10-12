package com.guhecloud.cube.biz.door.server.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.door.server.enumeration.EventResultEnum;
import com.guhecloud.cube.biz.door.server.param.DeviceStatusParam;
import com.guhecloud.cube.biz.door.server.param.StatisticalChartParam;
import com.guhecloud.cube.biz.door.server.param.StatisticsParam;
import com.guhecloud.cube.biz.door.server.queryparam.AccessEventQueryParam;
import com.guhecloud.cube.biz.door.server.queryparam.CommonStatQueryParam;
import com.guhecloud.cube.biz.door.server.queryparam.EventRecordQueryParam;
import com.guhecloud.cube.biz.door.server.service.DoorCardService;
import com.guhecloud.cube.biz.door.server.service.DoorVisualizationService;
import com.guhecloud.cube.biz.door.server.service.EventRecordService;
import com.guhecloud.cube.biz.door.server.vo.CardStatByStatusVO;
import com.guhecloud.cube.biz.door.server.vo.CommonStatVO;
import com.guhecloud.cube.biz.door.server.vo.DoorStatisticsVo;
import com.guhecloud.cube.biz.door.server.vo.EventRecordVo;
import com.guhecloud.cube.biz.door.server.vo.PieChartVO;
import com.guhecloud.cube.biz.door.server.vo.PieChartValueVO;
import com.guhecloud.cube.biz.door.server.vo.StatisticalChartVO;
import com.guhecloud.platform.base.util.MathUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 门禁管理可视化
 *
 * @author : lijie
 * @date : 2023-3-21
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DoorVisualizationController {
    private final DoorVisualizationService doorVisualizationService;
    private final DoorCardService doorCardService;
    private final EventRecordService eventRecordService;

    /**
     * 人流通行趋势
     */
    @GetMapping("/visualization/getEventRecord")
    public StatisticalChartVO getEventRecord(StatisticalChartParam param) {
        return doorVisualizationService.getEventRecord(param);
    }

    /**
     * 新版人流通行趋势
     */
    @GetMapping("/visualization/record-trend")
    public Map<String, Object> getNewEventRecordTrend(StatisticalChartParam param) {
        Map<String, Object> remap = new HashMap<>();
        //通过
        param.setEventResult(EventResultEnum.DO_PASS.name());
        StatisticalChartVO eventResultRecord = doorVisualizationService.getEventResultRecord(param);
        //异常
        param.setEventResult(EventResultEnum.DO_REFUSE.name());
        StatisticalChartVO eventRefuseResultRecord = doorVisualizationService.getEventResultRecord(param);
        //计算
        BigDecimal allTotal = eventResultRecord.getTotal().add(eventRefuseResultRecord.getTotal());
        BigDecimal previousTotal = eventResultRecord.getTotal().add(eventRefuseResultRecord.getTotal());
        BigDecimal bigDecimal = MathUtils.calculateRingRatio(allTotal, previousTotal);
        remap.put("total", allTotal);
        remap.put("ringRatio", bigDecimal);
        remap.put("eventPass", eventResultRecord);
        remap.put("eventRefuse", eventRefuseResultRecord);
        return remap;
    }

    /**
     * 通行类型占比
     */
    @GetMapping("/visualization/getDoorThroughType")
    public PieChartVO getDoorThroughType(StatisticalChartParam param) {
        return doorVisualizationService.getDoorThroughType(param);
    }

    /**
     * 通行汇总
     */
    @GetMapping("/visualization/getDoorAccessSummary")
    public PieChartVO getDoorAccessSummary(StatisticalChartParam param) {
        return doorVisualizationService.getDoorAccessSummary(param);
    }

    /**
     * 设备实时状态
     */
    @GetMapping("/visualization/getDeviceStatus")
    public List<PieChartValueVO> getDeviceStatus(DeviceStatusParam param) {
        return doorVisualizationService.getDeviceStatus(param);
    }

    /**
     * 异常事件top5
     */
    @GetMapping("/visualization/getDoorAnomalousTop")
    public List<PieChartValueVO> getDoorAnomalousTop(StatisticalChartParam param) {
        return doorVisualizationService.getDoorAnomalousTop(param);
    }

    /**
     * @Description 门禁事件记录分页
     * @Author lrj
     */
    @GetMapping("/visualization-event")
    public Page<EventRecordVo> getDoorVisualizationEventRecord(EventRecordQueryParam eventRecordQueryParam) {
        return eventRecordService.getvisualizationDoorEventRecord(eventRecordQueryParam);
    }

    /**
     * @Description 进出统计
     * @Author lrj
     */
    @GetMapping("/door-in-out")
    public DoorStatisticsVo getEventInOrOut(StatisticsParam param) {
        return eventRecordService.getEventInOrOut(param);
    }

    /**
     * @Description 人员出入情况统计
     * @Author lrj
     */
    @GetMapping("/in-out-trend")
    public Map<String, Object> getInAndOutTrend(StatisticsParam param) {
        return eventRecordService.getInAndOutTrend(param);
    }

    /**
     * 按使用状态统计卡的数量
     *
     * @return
     */
    @GetMapping("/door/card/useStatus/stat")
    public CardStatByStatusVO getCardStatByStatus() {
        return doorCardService.getCardStatByStatus();
    }

    /**
     * 访客趋势统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/door/visiting/trend/stat")
    public List<CommonStatVO> getVisitingRecordStatByTime(CommonStatQueryParam param) {
        return doorCardService.getVisitingRecordStatByTime(param);
    }

    /**
     * 门禁事件按名称统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/door/event/name/stat")
    public List<CommonStatVO> getDoorEventStatByName(AccessEventQueryParam param) {
        return doorCardService.getDoorEventStatByName(param);
    }

    /**
     * 访问事件按名称统计
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/door/access/name/stat")
    public List<CommonStatVO> getAccessEventStatByName(AccessEventQueryParam param) {
        return doorCardService.getAccessEventStatByName(param);
    }

}
