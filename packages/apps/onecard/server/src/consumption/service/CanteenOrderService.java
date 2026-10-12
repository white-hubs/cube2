package com.guhecloud.cube.onecard.server.consumption.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.onecard.server.consumption.entity.CanteenOrder;
import com.guhecloud.cube.onecard.server.consumption.param.OneCardJobParam;
import com.guhecloud.cube.onecard.server.consumption.param.StatisticsQueryParam;
import com.guhecloud.cube.onecard.server.consumption.vo.statistics.AmountTrendStatVO;
import com.guhecloud.cube.onecard.server.consumption.vo.statistics.ConsumptionAccountStatVO;
import com.guhecloud.cube.onecard.server.consumption.vo.statistics.PersonTrendStatVO;
import com.guhecloud.cube.onecard.server.consumption.vo.statistics.WindowAmountStatVO;
import com.guhecloud.platform.base.model.Period;

import java.math.BigDecimal;
import java.util.List;

public interface CanteenOrderService extends IService<CanteenOrder> {

    /**
     * 同步食堂订单
     *
     * @param oneCardJobParam
     */
    void syncCanteenOrder(OneCardJobParam oneCardJobParam);

    /**
     * 获取消费金额统计
     *
     * @param period
     *
     * @return
     */
    BigDecimal calculateCanteenConsumptionAmount(Period period);

    /**
     * 获取消费人数统计
     *
     * @param period
     *
     * @return
     */
    Integer calculateCanteenConsumptionPersonCount(Period period);

    /**
     * 获取消费金额趋势统计
     *
     * @param param
     *
     * @return
     */
    List<AmountTrendStatVO> calculateCanteenConsumptionAmountTrend(StatisticsQueryParam param);

    /**
     * 获取账户消费金额统计
     *
     * @param param
     *
     * @return
     */
    List<ConsumptionAccountStatVO> calculateUserConsumptionAmountStatistics(StatisticsQueryParam param);

    /**
     * 获取窗口金额统计
     *
     * @param param
     *
     * @return
     */
    List<WindowAmountStatVO> calculateWindowConsumptionAmountStatistics(StatisticsQueryParam param);

    /**
     * 获取消费用户数趋势统计
     *
     * @param param
     *
     * @return
     */
    List<PersonTrendStatVO> calculateConsumptionUserNumberTrendStatistics(StatisticsQueryParam param);
}
