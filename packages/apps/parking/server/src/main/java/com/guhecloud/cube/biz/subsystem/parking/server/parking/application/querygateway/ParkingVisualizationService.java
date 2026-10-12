package com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.guhecloud.cube.biz.subsystem.parking.client.param.StatisticalChartParam;
import com.guhecloud.cube.biz.subsystem.parking.client.util.DateLocalUtil;
import com.guhecloud.cube.biz.subsystem.parking.client.util.MathUtils;
import com.guhecloud.cube.biz.subsystem.parking.client.util.VisualizationGraphicsUtil;
import com.guhecloud.cube.biz.subsystem.parking.client.vo.StatisticalChartDataVO;
import com.guhecloud.cube.biz.subsystem.parking.client.vo.StatisticalChartVO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.query.ParkingStatisticsQueryParam;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.enumeration.ParkingAccessTypeEnum;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.domain.po.ParkingPayRecordPO;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.dao.ParkingPayRecordMapper;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.infrastructure.repository.dao.ParkingVisualizationMapper;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkStatisticsVo;
import com.guhecloud.cube.biz.subsystem.parking.server.parking.interfaces.vo.ParkingOverviewVO;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.MoneyOverviewVO;
import com.guhecloud.cube.biz.subsystem.parking.server.vehicle.vo.MoneyTypeVO;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SuppressWarnings("DoubleBraceInitialization")
public class ParkingVisualizationService {
    /** 滞留分布仅统计近 N 天进场记录，避免全表反连接 */
    private static final int RETENTION_LOOKBACK_DAYS = 15;

    private final ParkingVisualizationMapper visualizationMapper;
    private final ParkingPayRecordMapper payRecordMapper;

    public ParkingOverviewVO getParkingOverview(String source) {
        return visualizationMapper.getParkingOverview(source);
    }

    public MoneyOverviewVO getMoneyOverview(StatisticalChartParam param) {
        var slotMany = DateLocalUtil.dimensionTime(param.getDimension());
        var ratio = new BigDecimal(0);
        var moneyOverview = visualizationMapper.getMoneyOverviewCurrent(slotMany.getCurrentDate());
        if (ObjectUtils.isEmpty(moneyOverview)) {
            moneyOverview = new MoneyOverviewVO(ratio, ratio, ratio);
        }
        var previous = visualizationMapper.getMoneyOverviewCurrent(slotMany.getPreviousDate());
        if (ObjectUtils.isEmpty(previous)) {
            previous = new MoneyOverviewVO(ratio, ratio, ratio);
        }
        moneyOverview.setDiscountAmountRingRatio(MathUtils.d2dRatio(moneyOverview.getDiscountAmount(), previous.getDiscountAmount()));
        moneyOverview.setFreeAmountRingRatio(MathUtils.d2dRatio(moneyOverview.getFreeAmount(), previous.getFreeAmount()));
        moneyOverview.setReceivableAmountRingRatio(MathUtils.d2dRatio(moneyOverview.getReceivableAmount(), previous.getReceivableAmount()));
        return moneyOverview;
    }

    public StatisticalChartVO getTrafficTrend(StatisticalChartParam param) {
        // 生成基础数据
        var table = new ArrayList<String>();
        table.add("使入车辆");
        table.add("使出车辆");
        var chart = VisualizationGraphicsUtil.getGraphics(param.getDimension(), table);
        // 处理数据
        var slotMany = DateLocalUtil.dimensionTime(param.getDimension());
        var in = visualizationMapper.getTrafficTrend(slotMany.getCurrentDate(), ParkingAccessTypeEnum.ACCESS_IN.name(), param.getDimension());
        var out = visualizationMapper.getTrafficTrend(slotMany.getCurrentDate(), ParkingAccessTypeEnum.ACCESS_OUT.name(), param.getDimension());
        var data = new ArrayList<List<StatisticalChartDataVO>>() {{
            add(in);
            add(out);
        }};
        return VisualizationGraphicsUtil.processingData(param.getDimension(), chart, data);
    }

    /**
     * 近 15 天进场的滞留车辆分布：有进无出，且滞留完整天数 >= 1，按 1/2/3/4/5+ 天分桶并计算百分比。
     */
    public StatisticalChartVO getRetentionTrafficTrend() {
        var tableName = new ArrayList<String>() {{
            add("滞留车辆分布");
            add("滞留车辆分布百分比");
        }};
        var retention = VisualizationGraphicsUtil.getGraphics(5, tableName);
        var now = LocalDateTime.now();
        // 仅统计近 15 天进场；strandedBefore=now-1天，等价 TIMESTAMPDIFF(DAY)>=1
        var lookbackBegin = DateUtils.offsetDay(now, -RETENTION_LOOKBACK_DAYS);
        var strandedBefore = DateUtils.offsetDay(now, -1);
        var buckets = visualizationMapper.getRetentionTrafficTrendBuckets(now, lookbackBegin, strandedBefore);
        if (ObjectUtils.isEmpty(buckets)) {
            return retention;
        }
        var totalAll = 0;
        for (var bucket : buckets) {
            if (bucket.getValus() != null) {
                totalAll += bucket.getValus();
            }
        }
        if (totalAll <= 0) {
            return retention;
        }
        var totalAllDecimal = new BigDecimal(totalAll);
        var countSeries = retention.getSeries().getFirst();
        var percentSeries = retention.getSeries().get(1);
        for (var bucket : buckets) {
            if (bucket.getName() == null || bucket.getValus() == null || bucket.getValus() <= 0) {
                continue;
            }
            var i = Integer.parseInt(bucket.getName().trim());
            if (i < 0 || i >= countSeries.getValue().size()) {
                continue;
            }
            var value = new BigDecimal(bucket.getValus());
            countSeries.getValue().set(i, value);
            countSeries.setTotal(countSeries.getTotal().add(value));
            percentSeries.getValue().set(i, MathUtils.percentage(value, totalAllDecimal));
        }
        return retention;
    }

    /**
     * 停车时长分布：库内按 identify_id 配对进出并分桶，禁止再拉全量行（含 image_base）。
     * 分桶与旧逻辑一致：ChronoUnit.DAYS → 0-3 / 4-6 / 7-9 / 9以上。
     */
    public StatisticalChartVO getParkingDuration(StatisticalChartParam param) {
        var retention = VisualizationGraphicsUtil.getGraphics(4, "停车时长分布");
        var xdata = new ArrayList<String>() {{
            add("0-3");
            add("4-6");
            add("7-9");
            add("9以上");
        }};
        retention.setXdata(xdata);
        var slotMany = DateLocalUtil.dimensionTime(param.getDimension());
        var buckets = visualizationMapper.getParkingDurationBuckets(slotMany.getCurrentDate());
        if (ObjectUtils.isEmpty(buckets)) {
            return retention;
        }
        var countSeries = retention.getSeries().getFirst();
        for (var bucket : buckets) {
            if (bucket.getName() == null || bucket.getValus() == null || bucket.getValus() <= 0) {
                continue;
            }
            var i = Integer.parseInt(bucket.getName().trim());
            if (i < 0 || i >= countSeries.getValue().size()) {
                continue;
            }
            countSeries.getValue().set(i, new BigDecimal(bucket.getValus()));
        }
        return retention;
    }

    public List<MoneyTypeVO> getPayType(StatisticalChartParam param) {
        var slotMany = DateLocalUtil.dimensionTime(param.getDimension());
        return visualizationMapper.getPayType(slotMany.getCurrentDate());
    }

    public StatisticalChartVO getPayTrend(StatisticalChartParam param) {
        // 初始化
        var retention = VisualizationGraphicsUtil.getGraphics(param.getDimension(), "收费趋势");
        // 通过时间维度获取开始、结束时间
        var slotMany = DateLocalUtil.dimensionTime(param.getDimension());
        // 查询数据
        var data = visualizationMapper.getPayTrend(param.getDimension(), slotMany.getCurrentDate());
        var dataList = new ArrayList<List<StatisticalChartDataVO>>();
        dataList.add(data);
        return VisualizationGraphicsUtil.processingData(param.getDimension(), retention, dataList);
    }

    public StatisticalChartVO getPayIntervalTrend(StatisticalChartParam param) {
        var retention = VisualizationGraphicsUtil.getGraphics(4, "收费区间分布");
        // 根据区间展示，需要重新定义x轴
        var xdata = new ArrayList<String>() {{
            add("0-3");
            add("4-6");
            add("7-9");
            add("9以上");
        }};
        retention.setXdata(xdata);
        var slotMany = DateLocalUtil.dimensionTime(param.getDimension());
        var date = slotMany.getCurrentDate();
        // 0-3元
        var number = payRecordMapper.selectCount(new LambdaQueryWrapper<ParkingPayRecordPO>()
                                                         .lt(ParkingPayRecordPO::getFreeAmount, 3)
                                                         .between(ParkingPayRecordPO::getPayTime, date.getStartTime(), date.getEndTime()));
        retention.getSeries().getFirst().getValue().set(0, new BigDecimal(number));
        // 4-6元
        number = payRecordMapper.selectCount(new LambdaQueryWrapper<ParkingPayRecordPO>()
                                                     .between(ParkingPayRecordPO::getPayTime, date.getStartTime(), date.getEndTime())
                                                     .gt(ParkingPayRecordPO::getFreeAmount, 3)
                                                     .lt(ParkingPayRecordPO::getFreeAmount, 7));
        retention.getSeries().getFirst().getValue().set(1, new BigDecimal(number));
        // 7-9元
        number = payRecordMapper.selectCount(new LambdaQueryWrapper<ParkingPayRecordPO>()
                                                     .between(ParkingPayRecordPO::getPayTime, date.getStartTime(), date.getEndTime())
                                                     .gt(ParkingPayRecordPO::getFreeAmount, 6)
                                                     .lt(ParkingPayRecordPO::getFreeAmount, 10));
        retention.getSeries().getFirst().getValue().set(2, new BigDecimal(number));
        // 大于九元
        number = payRecordMapper.selectCount(new LambdaQueryWrapper<ParkingPayRecordPO>()
                                                     .between(ParkingPayRecordPO::getPayTime, date.getStartTime(), date.getEndTime())
                                                     .gt(ParkingPayRecordPO::getFreeAmount, 9));
        retention.getSeries().getFirst().getValue().set(3, new BigDecimal(number));
        return retention;
    }

    public ParkStatisticsVo getParkEventInOrOut(ParkingStatisticsQueryParam param) {
        return visualizationMapper.getParkEventInOrOut(param);
    }

}
