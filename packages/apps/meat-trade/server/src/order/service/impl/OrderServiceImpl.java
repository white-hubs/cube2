package com.guhecloud.cube.biz.meattrade.server.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.meattrade.server.base.entity.Config;
import com.guhecloud.cube.biz.meattrade.server.base.entity.WeightLoss;
import com.guhecloud.cube.biz.meattrade.server.base.enumeration.ConfigEnum;
import com.guhecloud.cube.biz.meattrade.server.base.service.ConfigService;
import com.guhecloud.cube.biz.meattrade.server.base.service.WeightLossService;
import com.guhecloud.cube.biz.meattrade.server.notify.MeattradeMsgNotifyUtils;
import com.guhecloud.cube.biz.meattrade.server.order.dao.OrderMapper;
import com.guhecloud.cube.biz.meattrade.server.order.dto.OrderCommissionDTO;
import com.guhecloud.cube.biz.meattrade.server.order.dto.OrderDetailDTO;
import com.guhecloud.cube.biz.meattrade.server.order.entity.Order;
import com.guhecloud.cube.biz.meattrade.server.order.entity.OrderDetail;
import com.guhecloud.cube.biz.meattrade.server.order.entity.SettlementConfig;
import com.guhecloud.cube.biz.meattrade.server.order.enumeration.OrderEnum;
import com.guhecloud.cube.biz.meattrade.server.order.enumeration.SettlementConfigEnum;
import com.guhecloud.cube.biz.meattrade.server.order.param.OrderCommissionModifyParam;
import com.guhecloud.cube.biz.meattrade.server.order.param.OrderDetailModifyParam;
import com.guhecloud.cube.biz.meattrade.server.order.param.OrderModifyParam;
import com.guhecloud.cube.biz.meattrade.server.order.param.OrderQueryParam;
import com.guhecloud.cube.biz.meattrade.server.order.rocketmq.message.OrderDelayMessage;
import com.guhecloud.cube.biz.meattrade.server.order.service.OrderDetailService;
import com.guhecloud.cube.biz.meattrade.server.order.service.OrderService;
import com.guhecloud.cube.biz.meattrade.server.order.service.SettlementConfigService;
import com.guhecloud.cube.biz.meattrade.server.order.vo.OrderDateWeightVO;
import com.guhecloud.cube.biz.meattrade.server.order.vo.OrderTradeStatisticsVO;
import com.guhecloud.cube.biz.meattrade.server.order.vo.SupplierOrderTradeStatisticsVO;
import com.guhecloud.cube.biz.meattrade.server.pork.dao.PorkArrangeAuditMapper;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeAudit;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.PorkArrangeDetail;
import com.guhecloud.cube.biz.meattrade.server.pork.entity.SpotCheckResource;
import com.guhecloud.cube.biz.meattrade.server.pork.enumeration.PorkArrangeAuditEnum;
import com.guhecloud.cube.biz.meattrade.server.pork.param.StatQueryParam;
import com.guhecloud.cube.biz.meattrade.server.pork.service.PorkArrangeDetailService;
import com.guhecloud.cube.biz.meattrade.server.pork.service.SpotCheckResourceService;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.wallet.client.AccountClient;
import com.guhecloud.cube.biz.wallet.client.dto.AccountAssetsDTO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.cloud.logger.constant.OperationType;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.entity.BasicEntity;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.redis.util.RedisUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service("meattrade-orderService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    private final OrderDetailService orderDetailService;
    private final ConfigService configService;
    private final PorkArrangeDetailService porkArrangeDetailService;
    private final SettlementConfigService settlementConfigService;
    private final SpotCheckResourceService spotCheckResourceService;
    private final WeightLossService weightLossService;
    private final PorkArrangeAuditMapper porkArrangeAuditMapper;

    private final UserClient userClient;
    private final SupplierClient supplierClient;
    private final AccountClient accountClient;
    private final MeattradeMsgNotifyUtils meattradeMsgNotifyUtils;

    private final static String MEATTRADE_ORDER_REMIND = "MEATTRADE_ORDER_REMIND";
    private final static String MEATTRADE_ORDER_PAYMENT_SUCCESS = "MEATTRADE_ORDER_PAYMENT_SUCCESS";
    private final static String MEATTRADE_CUSTOMER_ORDER_CANCEL_CUSTOMER_NOTIFY = "MEATTRADE_CUSTOMER_ORDER_CANCEL_CUSTOMER_NOTIFY";
    private final static String MEATTRADE_CUSTOMER_ORDER_CANCEL_SUPPLIER_NOTIFY = "MEATTRADE_CUSTOMER_ORDER_CANCEL_SUPPLIER_NOTIFY";
    private final static String MEATTRADE_SUPPLIER_ORDER_CANCEL_CUSTOMER_NOTIFY = "MEATTRADE_SUPPLIER_ORDER_CANCEL_CUSTOMER_NOTIFY";
    private final static String MEATTRADE_SUPPLIER_ORDER_CANCEL_SUPPLIER_NOTIFY = "MEATTRADE_SUPPLIER_ORDER_CANCEL_SUPPLIER_NOTIFY";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long addOrder(@NonNull OrderModifyParam param) {
        // 获取批发商信息
        Integer supplierTenant = SessionContextHolder.getRequester().getTenant();
        SupplierDTO supplier = this.supplierClient.getBySupplierTenant(supplierTenant);
        Assert.isNotFound(supplier, "不存在批发商信息!");
        SessionContextHolder.requestTenant(supplier.getTenant());
        // 判断当前开单的片数是否已出售
        Set<Long> porkArrangeDetailIds = param.getDetails().stream().map(OrderDetailModifyParam::getPorkArrangeDetailId)
                .collect(Collectors.toSet());
        List<OrderDetail> activeOrderDetails = this.orderDetailService.getActiveOrderDetail(porkArrangeDetailIds, supplier.getTenant());
        Assert.isConflict(CollectionUtils.isNotEmpty(activeOrderDetails), "存在已出售的猪肉信息!");

        // 新增订单主表信息
        Order order = ObjectUtils.copying(param, Order.class);
        Long orderId = IdWorker.getId();
        order.setId(orderId);
        order.setSupplierId(supplier.getId());
        order.setStatus(OrderEnum.WAITED);
        order.setOrderTime(LocalDateTime.now());
        order.setCode(String.valueOf(RedisUtils.generateIdentity()));
        order.setTenant(supplier.getTenant());
        List<OrderDetail> orderDetails = ObjectUtils.copying(param.getDetails(), OrderDetail.class);

        //计算损耗等
        this.calculateOrderDetail(orderDetails, order);
        //
        // 计算佣金、佣金率，新增订单
        this.calculateCommissionRatio(orderDetails, supplier.getTenant());
        order.setCommission(orderDetails.stream().map(OrderDetail::getCommission).reduce(BigDecimal.ZERO, BigDecimal::add));
        this.baseMapper.insert(order);

        // 订单详情新增
        this.orderDetailService.saveBatch(orderDetails);

        // 延迟发送消息，订单创建后一个小时后发送消息通知商户
        RocketMQContextHolder.asyncSend(OrderDelayMessage.TOPIC, OrderDelayMessage.builder().orderId(orderId).build(), 17);
        return orderId;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(success = "删除订单", fail = "删除订单失败，失败原因：「{{#_errorMsg}}」",
               type = "MEATTRADE", typeName = "肉类滑轨",
               subType = "MEATTRADE_ORDER", subTypeName = "订单管理",
               operationType = OperationType.DELETE,
               bizNo = "{{#id}}"
    )
    public void removeOrder(@NonNull Long id) {
        Order order = this.baseMapper.selectById(id);
        Assert.isNotFound(order, "不存在订单信息!");
        Assert.isNotAcceptable(order.getStatus() != OrderEnum.CANCELED, "当前订单状态不是已取消!");
        DatabaseContextHolder.softDelete(this, id);

        // 获取批发商信息
        SupplierDTO supplier = this.supplierClient.getSupplierById(order.getSupplierId());
        Assert.isNotFound(supplier, "批发商信息不能为空!");

        // 取消相关订单详情信息
        List<OrderDetail> orderDetails = this.orderDetailService.listByOrderId(id, supplier.getTenant());
        DatabaseContextHolder.softDelete(orderDetailService, orderDetails);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(@NonNull Long id) {
        Order order = this.baseMapper.selectById(id);
        Assert.isNotFound(order, "不存在订单信息!");
        Assert.isNotAcceptable(order.getStatus() != OrderEnum.WAITED, "当前订单状态不是待付款!");

        order.setStatus(OrderEnum.CANCELED);
        this.baseMapper.updateById(order);

        // 修改取消的猪肉为未出售
        List<OrderDetail> orderDetails = this.orderDetailService.listByOrderId(id, order.getTenant());
        Set<Long> porkArrangeDetailIds = orderDetails.stream().map(OrderDetail::getPorkArrangeDetailId).collect(Collectors.toSet());
        this.porkArrangeDetailService.lambdaUpdate().set(PorkArrangeDetail::getPrice, null)
                .in(PorkArrangeDetail::getId, porkArrangeDetailIds).update();

        this.orderDetailService.lambdaUpdate().in(OrderDetail::getOrderId, id).set(OrderDetail::getChecked, 0).update();

        // 发送消息
        SupplierDTO supplier = this.supplierClient.getSupplierById(order.getSupplierId());
        Assert.isNotFound(supplier, "不存在批发商信息!");
        this.sendMsg(order, supplier);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendOrderMsg(Order order) {
        SupplierDTO supplier = this.supplierClient.getSupplierById(order.getSupplierId());
        Assert.isNotFound(supplier, "不存在批发商信息!");
        this.sendMsg(order, supplier);
    }

    @Override
    public Page<Order> pageMpSupplierOrder(@NonNull OrderQueryParam queryParam) {
        return this.lambdaQuery()
                .eq(Objects.nonNull(queryParam.getSupplierId()), Order::getSupplierId, queryParam.getSupplierId())
                .eq(Objects.nonNull(queryParam.getStatus()), Order::getStatus, queryParam.getStatus())
                .eq(Order::getTenant, queryParam.getTenant())
                .orderByDesc(Order::getOrderTime)
                .page(PagingUtils.convert(queryParam));
    }

    @Override
    public List<OrderDateWeightVO> orderDateGroupWeights(StatQueryParam queryParam) {
        return this.baseMapper.selectOrderDateGroupWeights(queryParam);
    }

    @Override
    public boolean complete(@NonNull Long id) {
        LambdaUpdateWrapper<Order> lambdaUpdateWrapper = Wrappers.<Order>lambdaUpdate()
                .set(Order::getStatus, OrderEnum.FINISHED)
                .set(BasicEntity::getUpdateBy, SecurityContextHolder.getPrincipal().getAccount())
                .set(BasicEntity::getUpdateTime, LocalDateTime.now())
                .eq(Order::getId, id).eq(Order::getStatus, OrderEnum.WAITED);
        return this.update(lambdaUpdateWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<OrderCommissionDTO> calculateCommissionRules(@NonNull OrderCommissionModifyParam queryParam) {
        // 获取理货片数信息
        Set<Long> porkArrangeDetailIds =
                queryParam.getOrderDetails().stream().map(OrderCommissionModifyParam.OrderDetail::getPorkArrangeDetailId)
                        .collect(Collectors.toSet());
        List<PorkArrangeDetail> porkArrangeDetails = this.porkArrangeDetailService.listByIds(porkArrangeDetailIds);
        //订单详情根据 理货ID分组，以便取得订单的单价 计算佣金
        Map<Long, OrderCommissionModifyParam.OrderDetail> orderMap = queryParam.getOrderDetails().stream()
                .collect(Collectors.toMap(OrderCommissionModifyParam.OrderDetail::getPorkArrangeDetailId, d -> d));
        //根据供应商ID 分组
        Map<Long, List<PorkArrangeDetail>> porkArrangeDetailMap =
                porkArrangeDetails.stream().collect(Collectors.groupingBy(PorkArrangeDetail::getSupplierId));

        List<OrderCommissionDTO> commissionDTOS = new ArrayList<>();

        porkArrangeDetailMap.forEach((supplierId, records) -> {
            // 获取损耗信息
            List<OrderDetailDTO> orderDetailDTOS = records.stream().map(porkArrangeDetail -> {
                OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
                long time = (System.currentTimeMillis() - DateUtils.toTimestamp(porkArrangeDetail.getPorkArrangeTime())) / (1000 * 60 * 60);
                WeightLoss weightLoss = this.weightLossService.getByTime(time);
                orderDetailDTO.setLoss(Objects.isNull(weightLoss) ? BigDecimal.ZERO : weightLoss.getLoss());
                orderDetailDTO.setPorkArrangeDetailId(porkArrangeDetail.getId());
                orderDetailDTO.setWeight(porkArrangeDetail.getWeight());
                orderDetailDTO.setPrice(orderMap.get(porkArrangeDetail.getId()).getPrice());
                return orderDetailDTO;
            }).collect(Collectors.toList());
            // 计算分佣
            List<OrderDetail> orderDetails = ObjectUtils.copying(orderDetailDTOS, OrderDetail.class);
            this.calculateCommissionRatio(orderDetails, queryParam.getTenant());
            OrderCommissionDTO commissionDTO = new OrderCommissionDTO();
            commissionDTO.setSupplierId(supplierId);
            commissionDTO.setCommission(orderDetails.stream().map(OrderDetail::getCommission).reduce(BigDecimal.ZERO, BigDecimal::add));
            commissionDTO.setOrderDetails(orderDetailDTOS);
            commissionDTO.setWeightLoss(orderDetailDTOS.stream().map(OrderDetailDTO::getLoss).reduce(BigDecimal.ZERO, BigDecimal::add));
            commissionDTOS.add(commissionDTO);
        });
        return commissionDTOS;
    }

    private void calculateOrderDetail(List<OrderDetail> orderDetails, Order order) {
        // 获取商户最低价格
        Config config = this.configService.getByType(ConfigEnum.SELLING_PRICE);

        BigDecimal value = Objects.isNull(config) ? BigDecimal.ZERO : BigDecimal.valueOf(config.getValue());
        BigDecimal totalWeight = BigDecimal.valueOf(0);
        BigDecimal totalLoss = BigDecimal.valueOf(0);
        BigDecimal totalPrice = BigDecimal.valueOf(0);

        // 获取开单的猪肉信息
        Set<Long> porkArrangeDetailIds = orderDetails.stream().map(OrderDetail::getPorkArrangeDetailId).collect(Collectors.toSet());
        List<PorkArrangeDetail> porkArrangeDetails = this.porkArrangeDetailService.listByIds(porkArrangeDetailIds);
        Assert.isConflict(CollectionUtils.isEmpty(porkArrangeDetails), "订单存在重复肉!");
        Map<Long, PorkArrangeDetail> porkArrangeDetailMap =
                porkArrangeDetails.stream().collect(Collectors.toMap(PorkArrangeDetail::getId, d -> d, (o, n) -> n));
        //查询理货审批时间 以便计算猪肉的损耗
        Set<Long> reportIds = porkArrangeDetails.stream().map(PorkArrangeDetail::getPorkReportId).collect(Collectors.toSet());
        List<PorkArrangeAudit> porkArrangeAudits = porkArrangeAuditMapper.selectList(Wrappers.<PorkArrangeAudit>lambdaQuery()
                                                                                             .in(PorkArrangeAudit::getPorkReportId, reportIds)
                                                                                             .eq(PorkArrangeAudit::getAuditStatus, PorkArrangeAuditEnum.PASSED));
        Assert.isNotAcceptable(
                porkArrangeAudits == null || porkArrangeAudits.size() != reportIds.size(), "订单中存在未经审批的肉");
        Map<Long, LocalDateTime> auditMap = porkArrangeAudits.stream()
                .collect((Collectors.toMap(PorkArrangeAudit::getPorkReportId, PorkArrangeAudit::getAuditTime, (o, n) -> n)));

        List<PorkArrangeDetail> details = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails) {
            if (porkArrangeDetailMap.containsKey(orderDetail.getPorkArrangeDetailId())) {
                PorkArrangeDetail porkArrangeDetail = porkArrangeDetailMap.get(orderDetail.getPorkArrangeDetailId());
                long time = (System.currentTimeMillis() - DateUtils.toTimestamp(auditMap.get(porkArrangeDetail.getPorkReportId()))) /
                            (1000 * 60 * 60);
                WeightLoss weightLoss = this.weightLossService.getByTime(time);
                totalLoss = Objects.isNull(weightLoss) ? totalLoss.add(BigDecimal.ZERO) : totalLoss.add(weightLoss.getLoss());
                orderDetail.setLoss(Objects.isNull(weightLoss) ? BigDecimal.ZERO : weightLoss.getLoss());
                totalWeight = totalWeight.add(porkArrangeDetail.getWeight());
                orderDetail.setWeight(porkArrangeDetail.getWeight());
                totalPrice = totalPrice.add(orderDetail.getUnitPrice().multiply((orderDetail.getWeight().subtract(orderDetail.getLoss())))
                                                    .setScale(2, RoundingMode.UP));
                orderDetail.setPrice(orderDetail.getUnitPrice().multiply(orderDetail.getWeight().subtract(orderDetail.getLoss())));
                // 判断是否存在最低价格
                Assert.isNotAcceptable(orderDetail.getUnitPrice().compareTo(value) < 0, "存在最低价格商品,最低价格为" + value);
                // 更新理货详情价格
                porkArrangeDetail.setPrice(orderDetail.getUnitPrice());
                orderDetail.setGoodsCategoryId(porkArrangeDetail.getGoodsCategoryId());
                orderDetail.setGoodsCategoryName(porkArrangeDetail.getGoodsCategoryName());
                orderDetail.setFreshLabel(porkArrangeDetail.getFreshLabel());
                orderDetail.setOrderId(order.getId());
                orderDetail.setTenant(order.getTenant());
                orderDetail.setChecked("1");
                details.add(porkArrangeDetail);
            }
        }
        BigDecimal actualWeight = totalWeight.subtract(totalLoss);
        order.setWeight(totalWeight);
        order.setActualWeight(actualWeight);
        order.setAmount(totalPrice);
        order.setLoss(totalLoss);
        Assert.isNotAcceptable(order.getAmount().compareTo(BigDecimal.valueOf(100000000)) >= 0, "订单金额过大!");
        this.porkArrangeDetailService.updateBatchById(details);
    }

    public void saveSpotCheckResource(List<OrderDetail> details, Order order) {
        Set<Long> porkArrangeDetailIds =
                details.stream().map(OrderDetail::getPorkArrangeDetailId).collect(Collectors.toSet());
        List<PorkArrangeDetail> porkArrangeDetails = this.porkArrangeDetailService.listByIds(porkArrangeDetailIds);
        Map<Long, List<PorkArrangeDetail>> porkArrangeDetailMap =
                porkArrangeDetails.stream().collect(Collectors.groupingBy(PorkArrangeDetail::getId));
        // 构建信息
        List<SpotCheckResource> spotCheckResources = details.stream().map(orderDetail -> {
            if (porkArrangeDetailMap.containsKey(orderDetail.getPorkArrangeDetailId())) {
                PorkArrangeDetail porkArrangeDetail = porkArrangeDetailMap.get(orderDetail.getPorkArrangeDetailId()).get(0);
                return SpotCheckResource.builder()
                        .supplierId(order.getSupplierId())
                        .orderId(order.getId())
                        .porkArrangeDetailId(porkArrangeDetail.getId())
                        .porkReportCode(porkArrangeDetail.getPorkReportCode())
                        .tenant(orderDetail.getTenant())
                        .build();
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        this.spotCheckResourceService.saveBatch(spotCheckResources);
    }

    public void calculateCommissionRatio(List<OrderDetail> orderDetails, Integer tenant) {
        // 获取分佣模式
        SettlementConfigEnum way = SettlementConfigEnum.TRADE;
        List<SettlementConfig> configs =
                this.settlementConfigService.listActiveSettlementConfig(tenant);
        Assert.isNotAcceptable(CollectionUtils.isEmpty(configs), "未设置分佣模式，无法开单!");
        if (!configs.isEmpty()) {
            way = configs.getFirst().getWay();//佣金结算方式，按交易金额时，可能是多条，但是分佣模式一样的；其他按重量和数量，配置都只有一条数据
        }
        // 计算佣金
        for (OrderDetail orderDetail : orderDetails) {
            if (SettlementConfigEnum.TRADE.equals(way)) {
                //没有匹配到分佣规则 则不收佣金
                SettlementConfig settlementConfig = configs.stream()
                        .filter(c -> c.getStartWeight().compareTo(orderDetail.getWeight()) <= 0 &&
                                     c.getEndWeight().compareTo(orderDetail.getWeight()) > 0).findAny().orElse(null);
                if (settlementConfig != null) {
                    BigDecimal value = settlementConfig.getValue();//金额的比率
                    orderDetail.setCommissionRatio(value);
                    orderDetail.setCommission(orderDetail.getPrice().multiply(value).setScale(2, BigDecimal.ROUND_DOWN));
                } else {
                    orderDetail.setCommissionRatio(BigDecimal.ZERO);
                    orderDetail.setCommission(BigDecimal.ZERO);
                }
            } else if (SettlementConfigEnum.WEIGHT.equals(way)) {
                BigDecimal value = configs.get(0).getValue();
                orderDetail.setCommissionRatio(value);
                orderDetail.setCommission(orderDetail.getWeight().multiply(value));
            } else if (SettlementConfigEnum.NUM.equals(way)) {
                BigDecimal value = configs.get(0).getValue();
                orderDetail.setCommissionRatio(value);
                orderDetail.setCommission(value);
            }
        }
    }

    @Override
    public Order getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public List<Order> getDealOrder(@NonNull StatQueryParam queryParam) {
        return this.lambdaQuery()
                .eq(Order::getStatus, OrderEnum.FINISHED)
                .eq(Order::getTenant, queryParam.getTenant())
                .ge(Objects.nonNull(queryParam.getPeriod()) && Objects.nonNull(queryParam.getPeriod()
                                                                                       .getBegin()), Order::getOrderTime, ObjectUtils.ifNull(queryParam.getPeriod(), Period::getBegin))
                .lt(Objects.nonNull(queryParam.getPeriod()) && Objects.nonNull(queryParam.getPeriod()
                                                                                       .getEnd()), Order::getOrderTime, ObjectUtils.ifNull(queryParam.getPeriod(), Period::getEnd))
                .list();
    }

    @Override
    public OrderTradeStatisticsVO getOrderTradeStatistics(StatQueryParam queryParam) {
        return this.baseMapper.selectOrderTradeStatistics(queryParam);
    }

    @Override
    public List<Order> getDealOrderPage(StatQueryParam queryParam) {
        return this.lambdaQuery()
                .eq(Order::getStatus, OrderEnum.FINISHED)
                .eq(Order::getTenant, queryParam.getTenant())
                .ge(Objects.nonNull(queryParam.getPeriod()) && Objects.nonNull(queryParam.getPeriod()
                                                                                       .getBegin()), Order::getOrderTime, ObjectUtils.ifNull(queryParam.getPeriod(), Period::getBegin))
                .lt(Objects.nonNull(queryParam.getPeriod()) && Objects.nonNull(queryParam.getPeriod()
                                                                                       .getEnd()), Order::getOrderTime, ObjectUtils.ifNull(queryParam.getPeriod(), Period::getEnd))
                .orderByDesc(Order::getAmount)
                .list();
    }

    @Override
    public List<SupplierOrderTradeStatisticsVO> getSupplierOrderTradeStatistics(StatQueryParam queryParam) {
        return this.baseMapper.selectSupplierOrderTradeStatistics(PagingUtils.convert(queryParam), queryParam);
    }

    @Override
    public List<Order> listOrder(@NonNull OrderQueryParam queryParam) {
        return this.lambdaQuery()
                .eq(Objects.nonNull(queryParam.getStatus()), Order::getStatus, queryParam.getStatus())
                .eq(Order::getTenant, queryParam.getTenant())
                .list();
    }

    @Override
    public Page<Order> pageOrder(@NonNull OrderQueryParam queryParam) {
        return this.lambdaQuery()
                .and(StringUtils.isNotEmpty(queryParam.getKeyword()), q -> q
                        .like(Order::getCode, queryParam.getKeyword())
                        .or()
                        .in(!CollectionUtils.isEmpty(queryParam.getCustomerIds()), Order::getCustomerId, queryParam.getCustomerIds())
                    )
                .eq(Objects.nonNull(queryParam.getCustomerId()), Order::getCustomerId, queryParam.getCustomerId())
                .eq(Objects.nonNull(queryParam.getStatus()), Order::getStatus, queryParam.getStatus())
                .eq(Order::getTenant, queryParam.getTenant())
                .ge(Objects.nonNull(queryParam.getPeriod()) && Objects.nonNull(queryParam.getPeriod()
                                                                                       .getBegin()), Order::getCreateTime, ObjectUtils.ifNull(queryParam.getPeriod(), Period::getBegin))
                .lt(Objects.nonNull(queryParam.getPeriod()) && Objects.nonNull(queryParam.getPeriod()
                                                                                       .getEnd()), Order::getCreateTime, ObjectUtils.ifNull(queryParam.getPeriod(), Period::getEnd))
                .orderByDesc(Order::getOrderTime)
                .page(PagingUtils.convert(queryParam));
    }

    private void sendMsg(Order order, SupplierDTO supplier) {
        String terminal = SessionContextHolder.getRequester().getTerminal();
        SessionContextHolder.requestTenant(supplier.getTenant());
        try {
            String date = DateUtils.format(DateUtils.now(), "yyyy年MM月dd日HH时mm分");
            if (order.getStatus() == OrderEnum.CANCELED) {
                String supplierSceneCode = "";
                String customerSceneCode = "";
                if ("asc-supplier-mp".equals(terminal)) {
                    supplierSceneCode = MEATTRADE_SUPPLIER_ORDER_CANCEL_SUPPLIER_NOTIFY;
                    customerSceneCode = MEATTRADE_SUPPLIER_ORDER_CANCEL_CUSTOMER_NOTIFY;
                } else if ("asc-meat-purchaser-mp".equals(terminal)) {
                    supplierSceneCode = MEATTRADE_CUSTOMER_ORDER_CANCEL_SUPPLIER_NOTIFY;
                    customerSceneCode = MEATTRADE_CUSTOMER_ORDER_CANCEL_CUSTOMER_NOTIFY;
                }
                //卖家通知
                Map<String, String> supplierMap = new HashMap<>();
                supplierMap.put("supplierName", supplier.getName());
                supplierMap.put("date", date);
                supplierMap.put("orderCode", String.valueOf(order.getCode()));
                this.meattradeMsgNotifyUtils.supplierNotify(supplierSceneCode,
                                                            order.getId().toString(),
                                                            supplier,
                                                            supplierMap);
                //买家通知
                //获取买家信息
                UserDTO customer = this.userClient.getUser(order.getCustomerId());
                Map<String, String> customerMap = new HashMap<>();
                customerMap.put("customerName", customer.getName());
                customerMap.put("date", date);
                customerMap.put("orderCode", String.valueOf(order.getCode()));
                this.meattradeMsgNotifyUtils.commonNotify(customerSceneCode,
                                                          order.getId().toString(),
                                                          Collections.singleton(order.getCustomerId()),
                                                          supplier.getTenant(),
                                                          customerMap);
            } else if (order.getStatus() == OrderEnum.FINISHED) {
                // 获取买家信息
                UserDTO customer = this.userClient.getUser(order.getCustomerId());
                // 获取买家用户余额信息
                AccountAssetsDTO accountAssets = this.accountClient.getUserAccountAssets(customer.getId(), supplier.getTenant());
                Map<String, String> map = new HashMap<>();
                map.put("customerName", customer.getName());
                map.put("amount", String.valueOf(order.getAmount().setScale(2, RoundingMode.DOWN)));
                map.put("date", date);
                map.put("balance", String.valueOf(accountAssets.getBalance()));
                this.meattradeMsgNotifyUtils.commonNotify(MEATTRADE_ORDER_PAYMENT_SUCCESS,
                                                          order.getId().toString(),
                                                          Collections.singleton(order.getCustomerId()),
                                                          supplier.getTenant(),
                                                          map);
            } else if (OrderEnum.WAITED == order.getStatus()) {
                String sceneCode = MEATTRADE_ORDER_REMIND;
                Map<String, String> map = new HashMap<>();
                map.put("supplierName", supplier.getName());
                map.put("orderCode", String.valueOf(order.getCode()));
                this.meattradeMsgNotifyUtils.supplierNotify(sceneCode, order.getId().toString(), supplier, map);
            }
        } catch (Exception e) {
            log.error("订单消息发送失败,失败原因:{}", e);
        }
    }
}
