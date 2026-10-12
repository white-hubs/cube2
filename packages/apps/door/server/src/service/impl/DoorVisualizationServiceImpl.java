package com.guhecloud.cube.biz.door.server.service.impl;

import com.guhecloud.cube.base.device.center.std.devcenter.DeviceBimSceneStdClient;
import com.guhecloud.cube.biz.door.server.dao.DoorVisualizationMapper;
import com.guhecloud.cube.biz.door.server.enumeration.EventResultEnum;
import com.guhecloud.cube.biz.door.server.param.DeviceStatusParam;
import com.guhecloud.cube.biz.door.server.param.StatisticalChartParam;
import com.guhecloud.cube.biz.door.server.service.DoorVisualizationService;
import com.guhecloud.cube.biz.door.server.util.DateLocalUtil;
import com.guhecloud.cube.biz.door.server.util.VisualizationGraphicsUtil;
import com.guhecloud.cube.biz.door.server.vo.DateTimeSlotManyVO;
import com.guhecloud.cube.biz.door.server.vo.PieChartVO;
import com.guhecloud.cube.biz.door.server.vo.PieChartValueVO;
import com.guhecloud.cube.biz.door.server.vo.StatisticalChartDataVO;
import com.guhecloud.cube.biz.door.server.vo.StatisticalChartVO;
import com.guhecloud.platform.base.util.MathUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("DoubleBraceInitialization")
public class DoorVisualizationServiceImpl implements DoorVisualizationService {
    private final DeviceBimSceneStdClient deviceBimSceneClient;
    private final DoorVisualizationMapper doorVisualizationMapper;

    @Override
    public StatisticalChartVO getEventRecord(StatisticalChartParam param) {
        setDeviceCode(param);
        DateTimeSlotManyVO dimension = DateLocalUtil.dimensionTime(param.getDimension());
        List<StatisticalChartDataVO> currentList = doorVisualizationMapper.getCurrentList(param, dimension.getCurrentDate(), param.getDimension());

        StatisticalChartVO retention = VisualizationGraphicsUtil.getGraphics(param.getDimension(), "人流通行趋势");
        if (ObjectUtils.isEmpty(currentList)) {
            return retention;
        }
        // 获取昨天的通行总数
        Integer previous = doorVisualizationMapper.getPreviousList(param, dimension.getPreviousDate());
        BigDecimal total = new BigDecimal(0);
        for (StatisticalChartDataVO ex : currentList) {
            total = total.add(new BigDecimal(ex.getValus()));
        }
        retention.setRingRatio(MathUtils.calculateRingRatio(total, new BigDecimal(previous)));
        List<List<StatisticalChartDataVO>> data = new ArrayList<>() {{
            add(currentList);
        }};
        return VisualizationGraphicsUtil.processingData(param.getDimension(), retention, data);

    }

    private void setDeviceCode(StatisticalChartParam param) {
        Set<String> codes;
        try {
            codes = this.deviceBimSceneClient.getBimDeviceCodes(param.getSceneType(), param.getAreaId());
        } catch (Exception e) {
            log.error("远程调用device-center获取设备code失败！，错误原因：{}", e.getMessage());
            throw new RuntimeException(e);
        }
        log.info("codes:{}", codes.size());
        if (ObjectUtils.isEmpty(codes)) {
            codes.add("-1");
        }
        for (String ex : codes) {
            log.info("code:{}", ex);
        }

        param.setDeviceCodes(codes);
        param.setThirdPartyIds(new ArrayList<>(codes));
    }

    @Override
    public StatisticalChartVO getEventResultRecord(StatisticalChartParam param) {
        Set<Long> ids = this.deviceBimSceneClient.getBimDeviceIds(param.getSceneType(), param.getAreaId());
        log.info("ids:{}", ids.size());
        if (ObjectUtils.isEmpty(ids)) {
            ids.add(-1L);
        }
        for (Long ex : ids) {
            log.info("business_id:{}", ex);
        }
        param.setBusinessIds(new ArrayList<>(ids));
        DateTimeSlotManyVO dimension = DateLocalUtil.dimensionTime(param.getDimension());
        List<StatisticalChartDataVO> currentList = doorVisualizationMapper.getCurrentList(param, dimension.getCurrentDate(), param.getDimension());

        StatisticalChartVO retention = VisualizationGraphicsUtil.getGraphics(param.getDimension(), "人流通行趋势");
        if (ObjectUtils.isEmpty(currentList)) {
            return retention;
        }
        // 获取昨天的通行总数
        Integer previous = doorVisualizationMapper.getPreviousList(param, dimension.getPreviousDate());
        BigDecimal total = new BigDecimal(0);
        for (StatisticalChartDataVO ex : currentList) {
            total = total.add(new BigDecimal(ex.getValus()));
        }
        retention.setTotal(total);
        retention.setPrevious(new BigDecimal(previous));
        retention.setRingRatio(MathUtils.calculateRingRatio(total, new BigDecimal(previous)));
        List<List<StatisticalChartDataVO>> data = new ArrayList<List<StatisticalChartDataVO>>() {{
            add(currentList);
        }};
        return VisualizationGraphicsUtil.processingData(param.getDimension(), retention, data);

    }

    @Override
    public PieChartVO getDoorThroughType(StatisticalChartParam param) {
        DateTimeSlotManyVO dimension = DateLocalUtil.dimensionTime(param.getDimension());
        Set<Long> ids = this.deviceBimSceneClient.getBimDeviceIds(param.getSceneType(), param.getAreaId());
        if (ObjectUtils.isEmpty(ids)) {
            ids.add(-1L);
        }
        param.setBusinessIds(new ArrayList<>(ids));
        List<PieChartValueVO> currentList = doorVisualizationMapper.getDoorThroughTypeCurrent(param, dimension.getCurrentDate());
        Integer previous = doorVisualizationMapper.getDoorThroughTypePervious(param, dimension.getPreviousDate());
        PieChartVO pieChart = new PieChartVO();
        pieChart.setData(currentList);
        BigDecimal total = new BigDecimal(0);
        for (PieChartValueVO ex : currentList) {
            total = total.add(ex.getValue());
        }
        pieChart.setRingRatio(MathUtils.calculateRingRatio(total,
                                                           ObjectUtils.isNotNull(previous) ? new BigDecimal(previous) : new BigDecimal(0)));
        pieChart.setTotal(total);
        return pieChart;
    }

    @Override
    public PieChartVO getDoorAccessSummary(StatisticalChartParam param) {
        DateTimeSlotManyVO dimension = DateLocalUtil.dimensionTime(param.getDimension());
        setDeviceCode(param);
        List<PieChartValueVO> data = doorVisualizationMapper.getDoorAccessSummary(param, dimension.getCurrentDate());
        PieChartVO pieChart = new PieChartVO();
        Map<String, PieChartValueVO> map = new HashMap<>();
        for (PieChartValueVO ex : data) {
            map.put(ex.getName(), ex);
            if (EventResultEnum.DO_REFUSE.name().equals(ex.getName())) {
                ex.setName("异常通行");
            } else if (EventResultEnum.DO_PASS.name().equals(ex.getName())) {
                ex.setName("正常通行");
            }
        }
        if (ObjectUtils.isEmpty(map.get(EventResultEnum.DO_PASS.name()))) {
            PieChartValueVO value = new PieChartValueVO();
            value.setName("正常通行");
            value.setValue(new BigDecimal(0));
            data.add(value);
        }
        if (ObjectUtils.isEmpty(map.get(EventResultEnum.DO_REFUSE.name()))) {
            PieChartValueVO value = new PieChartValueVO();
            value.setName("异常通行");
            value.setValue(new BigDecimal(0));
            data.add(value);
        }
        pieChart.setData(data);
        return pieChart;
    }

    /**
     * 设备实时状态
     */
    @Override
    public List<PieChartValueVO> getDeviceStatus(DeviceStatusParam param) {
        Set<Long> ids = this.deviceBimSceneClient.getBimDeviceIds(param.getSceneType(), param.getAreaId());
        if (ObjectUtils.isEmpty(ids)) {
            ids.add(-1L);
        }
        Long onlineDevice = doorVisualizationMapper.getOnlineDevice(new ArrayList<>(ids));
        Long offlineDevice = doorVisualizationMapper.getOfflineDevice(new ArrayList<>(ids));
        //不返回总数，总数前端是自行计算的，虽然没必要可以直接使用后端计算的
        Long totalDevices = onlineDevice + offlineDevice;

        //在线设备
        BigDecimal onlineRate = calculatePercentage(onlineDevice, totalDevices);
        PieChartValueVO onlinePieChartValueVO = PieChartValueVO.builder().name("在线").value(new BigDecimal(onlineDevice))
                .ringRatio(onlineRate).build();

        //离线设备
        BigDecimal offlineRate = calculatePercentage(offlineDevice, totalDevices);
        PieChartValueVO offlinePieChartValueVO = PieChartValueVO.builder().name("离线").value(new BigDecimal(offlineDevice))
                .ringRatio(offlineRate).build();

        return Arrays.asList(onlinePieChartValueVO, offlinePieChartValueVO);
    }

    /**
     * 异常事件top5
     */
    @Override
    public List<PieChartValueVO> getDoorAnomalousTop(StatisticalChartParam param) {
        DateTimeSlotManyVO dimension = DateLocalUtil.dimensionTime(param.getDimension());
        setDeviceCode(param);
        List<PieChartValueVO> currentList = doorVisualizationMapper.getDoorAnomalousTopCurrent(param, dimension.getCurrentDate());
        List<PieChartValueVO> previousList = doorVisualizationMapper.getDoorAnomalousTopCurrent(param, dimension.getPreviousDate());
        Map<String, BigDecimal> map = new HashMap<>();
        for (PieChartValueVO ex : previousList) {
            map.put(ex.getName(), ex.getValue());
        }
        for (PieChartValueVO ex : currentList) {
            ex.setRingRatio(MathUtils.calculateRingRatio(ex.getValue(), map.get(ex.getName())));
        }
        return currentList;
    }

    public BigDecimal calculatePercentage(Long count, Long total) {
        if (total == 0) {
            return BigDecimal.ZERO; // 防止除以零异常
        }
        BigDecimal countDecimal = new BigDecimal(count);
        BigDecimal totalDecimal = new BigDecimal(total);
        return countDecimal.divide(totalDecimal, 4, RoundingMode.HALF_UP)
                .multiply(new BigDecimal("100")) // 转换为百分比
                .setScale(2, RoundingMode.HALF_UP);
    }
}
