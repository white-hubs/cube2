package com.guhecloud.cube.onecard.server.consumption.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.onecard.server.consumption.entity.CanteenOrder;
import com.guhecloud.cube.onecard.server.consumption.param.StatisticsQueryParam;
import com.guhecloud.cube.onecard.server.consumption.vo.statistics.AmountTrendStatVO;
import com.guhecloud.cube.onecard.server.consumption.vo.statistics.ConsumptionAccountStatVO;
import com.guhecloud.cube.onecard.server.consumption.vo.statistics.PersonTrendStatVO;
import com.guhecloud.cube.onecard.server.consumption.vo.statistics.WindowAmountStatVO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface CanteenOrderMapper extends BaseMapper<CanteenOrder> {

    /**
     * 计算食堂订单金额
     *
     * @param period 时间范围
     *
     * @return 食堂订单金额
     */
    BigDecimal calculateCanteenConsumptionAmount(@Param("period") Period period);

    /**
     * 计算食堂消费人数
     *
     * @param period 时间范围
     *
     * @return 食堂订单人数
     */
    Integer calculateCanteenConsumptionPersonCount(@Param("period") Period period);

    /**
     * 查询食堂消费金额趋势
     *
     * @param param 趋势统计参数
     *
     * @return 食堂订单金额趋势
     */
    List<AmountTrendStatVO> selectCanteenConsumptionAmountTrend(@Param("param") StatisticsQueryParam param);

    /**
     * 查询用户食堂消费金额统计
     *
     * @param param 趋势统计参数
     *
     * @return 用户食堂消费金额统计
     */
    Page<ConsumptionAccountStatVO> calculateUserConsumptionAmountStatistics(Page<?> page, @Param("param") StatisticsQueryParam param);

    /**
     * 获取窗口金额统计
     *
     * @param param
     *
     * @return
     */
    List<WindowAmountStatVO> calculateWindowConsumptionAmountStatistics(@Param("param") StatisticsQueryParam param);

    /**
     * 获取消费用户数趋势统计
     *
     * @param param
     *
     * @return
     */
    List<PersonTrendStatVO> calculateConsumptionUserNumberTrendStatistics(@Param("param") StatisticsQueryParam param);
}
