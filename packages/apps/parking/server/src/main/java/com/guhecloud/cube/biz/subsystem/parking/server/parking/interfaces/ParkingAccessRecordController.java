package com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.ParkingAccessRecordService;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.ParkingAccessRecordExportService;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.ParkingInfoQueryService;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.query.ParkingAccessQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.query.ParkingInfoQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.query.ParkingStatisticsQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.aggregates.ParkingInfo;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.ParkingAccessTypeEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingAccessRecordPO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingAccessRecordPageVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingAccessRecordTrendTotalVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingAccessTypeTotalVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingAccessTypeTrendTotalVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingDurationDistributionVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingVehicleFlowDetailVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingVehicleFlowRankVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingVehicleFlowSummaryVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingVehicleFlowTrendVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ParkingAccessRecordController {

    private final ParkingAccessRecordService parkingAccessRecordService;

    private final ParkingAccessRecordExportService parkingAccessRecordExportService;

    private final ParkingInfoQueryService parkingInfoQueryService;

    /**
     * 出入记录分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/parking-access-records")
    public Pagination<ParkingAccessRecordPageVO> getPage(@Valid ParkingAccessQueryParam param) {
        var parkingAccessRecordPage = this.parkingAccessRecordService.getPage(param);
        var parkingAccessRecordPageVOS = ObjectUtils.copying(parkingAccessRecordPage.getRecords(), ParkingAccessRecordPageVO.class);
        if (CollectionUtils.isEmpty(parkingAccessRecordPageVOS)) {
            return Pagination.empty();
        }
        extendsParkingRecord(parkingAccessRecordPageVOS);
        return PagingUtils.convert(parkingAccessRecordPage, parkingAccessRecordPageVOS);
    }

    private void extendsParkingRecord(List<ParkingAccessRecordPageVO> parkingAccessRecordPageVOS) {
        var parkingCodes = parkingAccessRecordPageVOS.stream().map(ParkingAccessRecordPageVO::getParkingInfoCode)
                .filter(StringUtils::isNotBlank).toList();
        var queryParam = new ParkingInfoQueryParam();
        queryParam.setParkingInfoCodes(new LinkedHashSet<>(parkingCodes));
        var parkingMap = parkingInfoQueryService.getParkingInfo(queryParam).stream()
                .filter(info -> StringUtils.isNotEmpty(info.getCode()))
                .collect(Collectors.toMap(ParkingInfo::getCode, info -> info));
        parkingAccessRecordPageVOS.forEach(vo -> {
            if (StringUtils.isNotBlank(vo.getParkingInfoCode()) && parkingMap.containsKey(vo.getParkingInfoCode())) {
                vo.setParkingInfoName(parkingMap.get(vo.getParkingInfoCode()).getName());
            }
        });
    }

    /**
     * 今日车场停车数趋势
     *
     * @return
     */
    @GetMapping("/parking-access-records/today-in-trend")
    public List<ParkingAccessRecordTrendTotalVO> getTodayInRecordTrend(ParkingStatisticsQueryParam param) {
        var beginTime = DateUtils.beginOfDay(LocalDateTime.now());
        var endTime = DateUtils.endOfDay(beginTime);
        Period period = new Period(beginTime, endTime);
        return this.parkingAccessRecordService.getRecordTrend(period, ParkingAccessTypeEnum.ACCESS_IN, param.getParkingInfoCodes());
    }

    /**
     * 滞留车辆分页列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/parking-access-stranded-records")
    public Pagination<ParkingAccessRecordPageVO> getStrandedRecordPage(@Valid ParkingAccessQueryParam param) {
        Page<ParkingAccessRecordPO> parkingAccessRecordPage = this.parkingAccessRecordService.getStrandedRecords(param);
        List<ParkingAccessRecordPageVO> parkingAccessRecordPageVOS = ObjectUtils.copying(parkingAccessRecordPage.getRecords(), ParkingAccessRecordPageVO.class);
        if (CollectionUtils.isEmpty(parkingAccessRecordPageVOS)) {
            return Pagination.empty();
        }
        extendsParkingRecord(parkingAccessRecordPageVOS);
        return PagingUtils.convert(parkingAccessRecordPage, parkingAccessRecordPageVOS);
    }

    /**
     * 今日车辆出入数量统计
     *
     * @return
     */
    @GetMapping("/parking-access-record/today-access-total")
    public ParkingAccessTypeTotalVO getTodayAccessTypeTotal(ParkingStatisticsQueryParam param) {
        var beginTime = DateUtils.beginOfDay(LocalDateTime.now());
        var endTime = DateUtils.endOfDay(beginTime);
        Period period = new Period(beginTime, endTime);
        return this.parkingAccessRecordService.getAccessTypeTotal(period, param.getParkingInfoCodes());
    }

    /**
     * 今日停车出入趋势
     *
     * @return
     */
    @GetMapping("/parking-access-record/today-access-trend")
    public ParkingAccessTypeTrendTotalVO getTodayAccessTypeTrendTotal(ParkingStatisticsQueryParam param) {
        var beginTime = DateUtils.beginOfDay(LocalDateTime.now());
        var endTime = DateUtils.endOfDay(beginTime);
        Period period = new Period(beginTime, endTime);
        return this.parkingAccessRecordService.getAccessTypeTrendTotal(period, param.getParkingInfoCodes());
    }

    /**
     * 车辆出入数量统计
     *
     * @return
     */
    @GetMapping("/parking-access-record/access-total")
    public ParkingAccessTypeTotalVO getAccessTypeTotal(ParkingStatisticsQueryParam param) {
        return this.parkingAccessRecordService.getAccessTypeTotal(param.getPeriod(), param.getParkingInfoCodes());
    }

    /**
     * 停车出入趋势
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/parking-access-record/access-trend")
    public ParkingAccessTypeTrendTotalVO getAccessTypeTrendTotal(ParkingStatisticsQueryParam param) {
        return this.parkingAccessRecordService.getAccessTypeTrendTotal(param);
    }

    /**
     * 停车时长分布
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/parking-access-record/parking-duration-distribution")
    public List<ParkingDurationDistributionVO> getDurationDistribution(ParkingStatisticsQueryParam param) {
        return Collections.emptyList();
    }

    /**
     * 车流统计汇总
     *
     * @param param 查询参数
     *
     * @return 车流统计汇总
     */
    @GetMapping("/parking-access-records/statistics/vehicle-flow/summary")
    public ParkingVehicleFlowSummaryVO getVehicleFlowSummary(ParkingStatisticsQueryParam param) {
        return parkingAccessRecordService.queryVehicleFlowSummary(param);
    }

    /**
     * 车流趋势
     *
     * @param param 查询参数
     *
     * @return 车流趋势
     */
    @GetMapping("/parking-access-records/statistics/vehicle-flow/trend")
    public List<ParkingVehicleFlowTrendVO> getVehicleFlowTrend(ParkingStatisticsQueryParam param) {
        return parkingAccessRecordService.getVehicleFlowTrend(param);
    }

    /**
     * 停车场车辆排行
     *
     * @param param 查询参数
     *
     * @return 停车场车辆排行
     */
    @GetMapping("/parking-access-records/statistics/vehicle-flow/area-rank")
    public Pagination<ParkingVehicleFlowRankVO> getParkingVehicleFlowRank(ParkingStatisticsQueryParam param) {
        return parkingAccessRecordService.getParkingVehicleFlowRank(param);
    }

    /**
     * 停车场车辆排行导出
     */
    @GetMapping("/parking-access-records/statistics/vehicle-flow/area-rank/export")
    public void exportParkingVehicleFlowRank(ParkingStatisticsQueryParam param, HttpServletResponse response) {
        parkingAccessRecordExportService.exportParkingVehicleFlowRank(param, response);
    }

    /**
     * 车辆详情趋势（按 10 分钟统计）
     *
     * @param param 查询参数
     *
     * @return 车辆详情趋势
     */
    @GetMapping("/parking-access-records/statistics/vehicle-flow/detail-trend")
    public List<ParkingVehicleFlowTrendVO> getVehicleFlowDetailTrend(ParkingStatisticsQueryParam param) {
        return parkingAccessRecordService.getVehicleFlowDetailTrend(param);
    }

    /**
     * 车流详情列表
     *
     * @param param 查询参数
     *
     * @return 车流详情列表
     */
    @GetMapping("/parking-access-records/statistics/vehicle-flow/details")
    public Pagination<ParkingVehicleFlowDetailVO> listVehicleFlowDetail(ParkingStatisticsQueryParam param) {
        return parkingAccessRecordService.pageVehicleFlowDetail(param);
    }

    /**
     * 车辆统计明细导出
     */
    @GetMapping("/parking-access-records/statistics/vehicle-flow/details/export")
    public void exportVehicleFlowDetail(ParkingStatisticsQueryParam param, HttpServletResponse response) {
        parkingAccessRecordExportService.exportParkingVehicleFlowDetail(param, response);
    }
}
