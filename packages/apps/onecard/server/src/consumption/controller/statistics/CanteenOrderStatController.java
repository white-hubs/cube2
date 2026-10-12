package com.guhecloud.cube.onecard.server.consumption.controller.statistics;

import com.guhecloud.cube.onecard.server.consumption.param.StatisticsQueryParam;
import com.guhecloud.cube.onecard.server.consumption.service.CanteenOrderService;
import com.guhecloud.cube.onecard.server.consumption.vo.statistics.AmountTrendStatVO;
import com.guhecloud.cube.onecard.server.consumption.vo.statistics.ConsumptionAccountStatVO;
import com.guhecloud.cube.onecard.server.consumption.vo.statistics.PersonTrendStatVO;
import com.guhecloud.cube.onecard.server.consumption.vo.statistics.WindowAmountStatVO;
import com.guhecloud.platform.base.model.Period;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * 食堂统计
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CanteenOrderStatController {

    private final CanteenOrderService canteenOrderService;

    /**
     * 获取食堂消费金额统计
     *
     * @param period 时间范围
     *
     * @return 食堂消费金额统计
     */
    @GetMapping("/canteen-order/consumption/amount")
    public BigDecimal calculateCanteenConsumptionAmount(Period period) {
        return canteenOrderService.calculateCanteenConsumptionAmount(period);
    }

    /**
     * 获取食堂消费人数统计
     *
     * @param period 时间范围
     *
     * @return 食堂消费人数统计
     */
    @GetMapping("/canteen-order/consumption/person-count")
    public Integer calculateCanteenConsumptionPersonCount(Period period) {
        return canteenOrderService.calculateCanteenConsumptionPersonCount(period);
    }

    /**
     * 获取食堂消费金额趋势统计
     *
     * @param param 时间范围
     *
     * @return 食堂消费金额趋势统计
     */
    @GetMapping("/canteen-order/consumption/amount-trend")
    public List<AmountTrendStatVO> calculateCanteenConsumptionAmountTrend(StatisticsQueryParam param) {
        return canteenOrderService.calculateCanteenConsumptionAmountTrend(param);
    }

    /**
     * 获取用户食堂消费金额统计
     *
     * @param param 时间范围
     *
     * @return 用户食堂消费金额统计
     */
    @GetMapping("/canteen-order/consumption/account-stat")
    public List<ConsumptionAccountStatVO> calculateUserConsumptionAmountStatistics(StatisticsQueryParam param) {
        return canteenOrderService.calculateUserConsumptionAmountStatistics(param);
    }

    /**
     * 获取食堂消费金额窗口统计
     *
     * @param param 时间范围
     *
     * @return 食堂消费金额窗口统计
     */
    @GetMapping("/canteen-order/consumption/window-stat")
    public List<WindowAmountStatVO> calculateWindowConsumptionAmountStatistics(StatisticsQueryParam param) {
        return canteenOrderService.calculateWindowConsumptionAmountStatistics(param);
    }

    /**
     * 获取消费用户数趋势统计
     *
     * @param param 时间范围
     *
     * @return 消费用户数趋势统计
     */
    @GetMapping("/canteen-order/consumption/user-number-trend")
    public List<PersonTrendStatVO> calculateConsumptionUserNumberTrendStatistics(StatisticsQueryParam param) {
        return canteenOrderService.calculateConsumptionUserNumberTrendStatistics(param);
    }
}
