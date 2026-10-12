package com.guhecloud.cube.onecard.server.consumption.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.onecard.server.consumption.dao.CanteenOrderMapper;
import com.guhecloud.cube.onecard.server.consumption.entity.CanteenOrder;
import com.guhecloud.cube.onecard.server.consumption.param.OneCardJobParam;
import com.guhecloud.cube.onecard.server.consumption.param.StatisticsQueryParam;
import com.guhecloud.cube.onecard.server.consumption.service.CanteenOrderService;
import com.guhecloud.cube.onecard.server.consumption.utils.DateUnitUtils;
import com.guhecloud.cube.onecard.server.consumption.vo.statistics.AmountTrendStatVO;
import com.guhecloud.cube.onecard.server.consumption.vo.statistics.ConsumptionAccountStatVO;
import com.guhecloud.cube.onecard.server.consumption.vo.statistics.PersonTrendStatVO;
import com.guhecloud.cube.onecard.server.consumption.vo.statistics.WindowAmountStatVO;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.subsystem.requester.sdk.client.OneCardClient;
import com.guhecloud.subsystem.requester.sdk.param.onecard.AmountChangeParamQuery;
import com.guhecloud.subsystem.requester.sdk.vo.onecard.CanteenOrderListVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class CanteenOrderServiceImpl extends ServiceImpl<CanteenOrderMapper, CanteenOrder> implements CanteenOrderService {

    private final OneCardClient oneCardClient;

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String CANTEEN_ORDER_KEY = "CANTEEN_ORDER_LAST_TIME";

    public CanteenOrderServiceImpl(OneCardClient oneCardClient, RedisTemplate<String, Object> redisTemplate) {
        this.oneCardClient = oneCardClient;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void syncCanteenOrder(OneCardJobParam oneCardJobParam) {
        //定义一个变量，用于存储上次同步时间
        String lastSyncTime = null;
        //从redis中获取上次同步时间
        var o = this.redisTemplate.opsForHash()
                .get(CANTEEN_ORDER_KEY, oneCardJobParam.getTenant().toString());
        if (Objects.nonNull(o)) {
            //如果获取到上次同步时间，则格式化
            var dateTime = DateUtils.offset(DateUtils.parseDate(o.toString()), ChronoUnit.SECONDS, 1);
            lastSyncTime = DateUtils.format(dateTime);
        } else {
            var canteenOrder = this.lambdaQuery()
                    .orderByDesc(CanteenOrder::getOrderCreateTime)
                    .last("limit 1").one();
            lastSyncTime = Objects.nonNull(canteenOrder)
                           ? DateUtils.format(DateUtils.offset(canteenOrder.getOrderCreateTime(), ChronoUnit.SECONDS, 1))
                           : null;
        }
        var amountChangeParamQuery = new AmountChangeParamQuery();
        amountChangeParamQuery.setPageNumber(1);
        amountChangeParamQuery.setPageSize(1000);
        amountChangeParamQuery.setBeginDate(lastSyncTime);
        // 初始化分页信息
        int currentPage = 1;
        int totalPages = 1;
        int totalRecords;
        do {
            // 设置当前页码
            amountChangeParamQuery.setPageNumber(currentPage);
            // 查询当前页数据
            var canteenOrderList = oneCardClient.getCanteenOrderList(amountChangeParamQuery);

            if (Objects.isNull(canteenOrderList) || Objects.isNull(canteenOrderList.getRecords()) ||
                canteenOrderList.getRecords().isEmpty()) {
                log.info("第{}页数据为空，停止同步", currentPage);
                break;
            }

            // 如果是第一页，获取总页数信息
            if (currentPage == 1) {
                totalRecords = canteenOrderList.getTotal() != null ? canteenOrderList.getTotal().intValue() : 0;
                int pageSize = 1000;
                totalPages = (int) Math.ceil((double) totalRecords / pageSize);
                log.info("开始同步食堂订单数据，总记录数：{}，每页大小：{}，总页数：{}", totalRecords, pageSize, totalPages);
            }
            // 转换数据并入库
            var canteenOrders = canteenOrderList.getRecords().stream()
                    .map(this::convertToCanteenOrder)
                    .filter(Objects::nonNull)
                    .toList();
            if (!canteenOrders.isEmpty()) {
                // 批量保存数据
                this.saveBatch(canteenOrders);
                log.info("第{}页数据同步完成，成功入库{}条记录", currentPage, canteenOrders.size());
            } else {
                log.warn("第{}页数据转换后为空，跳过入库", currentPage);
            }
            // 更新当前页码
            currentPage++;
            // 添加短暂延迟，避免请求过快
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("同步过程被中断");
                break;
            }
        } while (currentPage <= totalPages);
        // 更新最后同步时间到Redis
        this.redisTemplate.opsForHash()
                .put(CANTEEN_ORDER_KEY,
                     oneCardJobParam.getTenant().toString(), DateUtils.format(LocalDateTime.now()));
        log.info("食堂订单数据同步完成，共处理{}页数据", currentPage - 1);
    }

    @Override
    public BigDecimal calculateCanteenConsumptionAmount(Period period) {
        return this.baseMapper.calculateCanteenConsumptionAmount(period);
    }

    @Override
    public Integer calculateCanteenConsumptionPersonCount(Period period) {
        return this.baseMapper.calculateCanteenConsumptionPersonCount(period);
    }

    @Override
    public List<AmountTrendStatVO> calculateCanteenConsumptionAmountTrend(StatisticsQueryParam param) {
        var dateField = DateUnitUtils.handleDateParam(param);
        List<AmountTrendStatVO> trendStatisticsVOS = this.baseMapper.selectCanteenConsumptionAmountTrend(param);
        LocalDateTime startDate = ObjectUtils.ifNull(param.getPeriod(), Period::getBegin);
        LocalDateTime endDate = ObjectUtils.ifNull(param.getPeriod(), Period::getEnd);
        if (startDate != null && endDate != null) {
            endDate = DateUtils.endOfDay(endDate);
            List<AmountTrendStatVO> finalData = new ArrayList<>();
            for (LocalDateTime start = startDate;
                 start.isBefore(endDate) || start.isEqual(endDate); start = DateUtils.offset(start, dateField, 1)) {
                String date = DateUtils.format(start, param.getDateString());
                String viewDate = DateUtils.format(start, param.getViewDateString());
                AmountTrendStatVO trendStatVO = trendStatisticsVOS.stream().filter(t -> t.getPeriod().equals(date))
                        .findAny().orElse(null);
                finalData.add(trendStatVO == null ? new AmountTrendStatVO(BigDecimal.ZERO, viewDate)
                                                  : new AmountTrendStatVO(trendStatVO.getAmount(), viewDate));
            }
            return finalData;
        } else {
            return trendStatisticsVOS;
        }
    }

    @Override
    public List<ConsumptionAccountStatVO> calculateUserConsumptionAmountStatistics(StatisticsQueryParam param) {
        var alarmTrendStatisticsVOS = this.baseMapper.calculateUserConsumptionAmountStatistics(PagingUtils.convert(param), param);
        return alarmTrendStatisticsVOS.getRecords();
    }

    @Override
    public List<WindowAmountStatVO> calculateWindowConsumptionAmountStatistics(StatisticsQueryParam param) {
        return this.baseMapper.calculateWindowConsumptionAmountStatistics(param);
    }

    @Override
    public List<PersonTrendStatVO> calculateConsumptionUserNumberTrendStatistics(StatisticsQueryParam param) {
        var dateField = DateUnitUtils.handleDateParam(param);
        List<PersonTrendStatVO> trendStatisticsVOS = this.baseMapper.calculateConsumptionUserNumberTrendStatistics(param);
        LocalDateTime startDate = ObjectUtils.ifNull(param.getPeriod(), Period::getBegin);
        LocalDateTime endDate = ObjectUtils.ifNull(param.getPeriod(), Period::getEnd);
        if (startDate != null && endDate != null) {
            endDate = DateUtils.endOfDay(endDate);
            List<PersonTrendStatVO> finalData = new ArrayList<>();
            for (LocalDateTime start = startDate;
                 start.isBefore(endDate) || start.isEqual(endDate); start = DateUtils.offset(start, dateField, 1)) {
                String date = DateUtils.format(start, param.getDateString());
                String viewDate = DateUtils.format(start, param.getViewDateString());
                PersonTrendStatVO trendStatVO = trendStatisticsVOS.stream().filter(t -> t.getPeriod().equals(date))
                        .findAny().orElse(null);
                finalData.add(trendStatVO == null ? new PersonTrendStatVO(0, viewDate)
                                                  : new PersonTrendStatVO(trendStatVO.getPersonCount(), viewDate));
            }
            return finalData;
        } else {
            return trendStatisticsVOS;
        }
    }

    /**
     * 将 CanteenOrderListVO 转换为 CanteenOrder 实体
     */
    private CanteenOrder convertToCanteenOrder(CanteenOrderListVO source) {
        if (Objects.isNull(source)) {
            return null;
        }

        var target = new CanteenOrder();
        // 直接映射相同字段
        target.setOrderId(source.getOrderId());
        target.setCardId(source.getCardId());
        target.setCardNo(source.getCardNo());
        target.setDepId(source.getDepId());
        target.setUserId(source.getUserId());
        target.setName(source.getName());
        target.setDepName(source.getDepName());
        target.setCanteenName(source.getCanteenName());
        target.setWindowName(source.getWindowName());
        target.setOrderStatusStr(source.getOrderStatusStr());
        target.setPayTypeStr(source.getPayTypeStr());
        target.setPayType(source.getPayType());
        target.setPrice(source.getPrice());
        target.setPersonalPrice(source.getPersonalPrice());
        target.setCardPrice(source.getCardPrice());
        target.setCashBalance(source.getCashBalance());
        target.setCanteenId(source.getCanteenId());
        target.setWindowId(source.getWindowId());
        target.setOrderStatus(source.getOrderStatus());
        // 时间字段转换
        if (Objects.nonNull(source.getCreateTime())) {
            try {
                target.setOrderCreateTime(DateUtils.parse(source.getCreateTime()));
            } catch (Exception e) {
                log.warn("时间格式转换失败：{}", source.getCreateTime());
                target.setOrderCreateTime(LocalDateTime.now());
            }
        }
        target.setPayTime(source.getPayTime());
        target.setCostPrice(source.getCostPrice());
        target.setDiscounts(source.getDiscounts());
        target.setTime(source.getTime());
        target.setRefundAmount(source.getRefundAmount());
        target.setAbnormal(source.getAbnormal());
        target.setAbnormalDesc(source.getAbnormalDesc());
        target.setReceivedPrice(source.getReceivedPrice());
        return target;
    }
}
