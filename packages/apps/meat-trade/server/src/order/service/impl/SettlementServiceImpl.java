package com.guhecloud.cube.biz.meattrade.server.order.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.meattrade.server.notify.MeattradeMsgNotifyUtils;
import com.guhecloud.cube.biz.meattrade.server.order.dao.SettlementMapper;
import com.guhecloud.cube.biz.meattrade.server.order.entity.Order;
import com.guhecloud.cube.biz.meattrade.server.order.entity.OrderDetail;
import com.guhecloud.cube.biz.meattrade.server.order.entity.Settlement;
import com.guhecloud.cube.biz.meattrade.server.order.param.SettlementQueryParam;
import com.guhecloud.cube.biz.meattrade.server.order.service.SettlementService;
import com.guhecloud.cube.biz.meattrade.server.wallet.enumeration.BusinessType;
import com.guhecloud.cube.biz.wallet.client.VirementClient;
import com.guhecloud.cube.biz.wallet.client.param.AccountTransferParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.cloud.logger.constant.OperationType;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.redis.util.RedisUtils;
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service("meattrade-settlementService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SettlementServiceImpl extends ServiceImpl<SettlementMapper, Settlement> implements SettlementService {

    private final VirementClient virementClient;
    private final MeattradeMsgNotifyUtils meattradeMsgNotifyUtils;

    private final static String MEATTRADE_ORDER_SETTLEMENT_SUCCESS = "MEATTRADE_ORDER_SETTLEMENT_SUCCESS";

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveByOrderAndDetails(Order order, List<OrderDetail> orderDetails) {
        Settlement settlement = this.getByOrderCode(order.getCode());
        if (Objects.isNull(settlement)) {
            settlement = new Settlement();
            settlement.setSupplierId(order.getSupplierId());
            settlement.setSettlementCode(String.valueOf(RedisUtils.generateIdentity()));
            settlement.setOrderCode(order.getCode());
            settlement.setCommission(orderDetails.stream().map(OrderDetail::getCommission).reduce(BigDecimal.ZERO, BigDecimal::add));
            settlement.setAmount(orderDetails.stream().map(OrderDetail::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add));
            settlement.setInitiateTime(LocalDateTime.now());
            settlement.setStatus(false);
            settlement.setTenant(order.getTenant());
            this.baseMapper.insert(settlement);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(success = "结算确认", fail = "结算确认失败，失败原因：「{{#_errorMsg}}」",
               type = "MEATTRADE", typeName = "肉类滑轨",
               subType = "SETTLEMENT_MANAGE", subTypeName = "结算账单",
               operationType = OperationType.UPDATE,
               bizNo = "{{#settlement.id}}"
    )
    public boolean complete(@NonNull Settlement settlement, @NonNull SupplierDTO supplier, @NonNull String password) {
        // 更新订单结算记录状态
        LambdaUpdateWrapper<Settlement> lambdaUpdateWrapper = Wrappers.<Settlement>lambdaUpdate()
                .set(Settlement::getStatus, true)
                .set(Settlement::getSettlementTime, LocalDateTime.now())
                .set(Settlement::getUpdateBy, SecurityContextHolder.getPrincipal().getAccount())
                .set(Settlement::getUpdateTime, LocalDateTime.now())
                .eq(Settlement::getId, settlement.getId()).eq(Settlement::getStatus, false);
        if (!this.update(lambdaUpdateWrapper)) {
            return false;
        }

        // 平台向卖家转账
        BigDecimal amount = settlement.getAmount().subtract(settlement.getCommission());
        AccountTransferParam transfer = AccountTransferParam.builder().id(settlement.getId())
                .business(BusinessType.PORK_TRADE.name()).sourceUserId(null).targetUserId(supplier.getAdminUserId())
                .amount(amount).password(password).tenant(supplier.getTenant()).build();
        this.virementClient.transfer(transfer);

        // 发送消息
        this.sendMsg(Stream.of(settlement).collect(Collectors.toList()), supplier);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchComplete(@NonNull List<Settlement> settlements, @NonNull SupplierDTO supplier, @NonNull String password) {
        // 更新订单结算记录状态
        List<Long> settlementIds = settlements.stream().map(Settlement::getId).collect(Collectors.toList());
        LambdaUpdateWrapper<Settlement> lambdaUpdateWrapper = Wrappers.<Settlement>lambdaUpdate()
                .set(Settlement::getStatus, true)
                .set(Settlement::getSettlementTime, LocalDateTime.now())
                .set(Settlement::getUpdateBy, SecurityContextHolder.getPrincipal().getAccount())
                .set(Settlement::getUpdateTime, LocalDateTime.now())
                .in(Settlement::getId, settlementIds).eq(Settlement::getStatus, false);
        if (!this.update(lambdaUpdateWrapper)) {
            return false;
        }

        // 平台向卖家转账
        BigDecimal amount = settlements.stream().map(settlement -> settlement.getAmount().subtract(settlement.getCommission()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        AccountTransferParam transfer = AccountTransferParam.builder().id(IdWorker.getId())
                .business(BusinessType.PORK_TRADE.name()).targetUserId(supplier.getAdminUserId())
                .amount(amount).password(password).tenant(supplier.getTenant()).build();
        this.virementClient.transfer(transfer);

        // 发送消息
        this.sendMsg(settlements, supplier);
        return true;
    }

    @Override
    public Settlement getByOrderCode(@NonNull String orderCode) {
        return this.baseMapper.selectOne(Wrappers.<Settlement>lambdaQuery().eq(Settlement::getOrderCode, orderCode));
    }

    @Override
    public List<Settlement> listByIds(@NonNull Collection<? extends Serializable> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() :
               this.lambdaQuery().in(Settlement::getId, ids).list();
    }

    @Override
    public Page<Settlement> pageSettlement(@NonNull SettlementQueryParam queryParam) {
        return this.lambdaQuery()
                .eq(Objects.nonNull(queryParam.getStatus()), Settlement::getStatus, queryParam.getStatus())
                .eq(Settlement::getTenant, queryParam.getTenant())
                .eq(Objects.nonNull(queryParam.getSupplierId()), Settlement::getSupplierId, queryParam.getSupplierId())
                .and(StringUtils.isNotEmpty(queryParam.getKeyword()), q -> q
                        .like(Settlement::getOrderCode, queryParam.getKeyword())
                        .or()
                        .like(Settlement::getSettlementCode, queryParam.getKeyword())
                    )
                .ge(Objects.nonNull(queryParam.getPeriod()) && Objects.nonNull(queryParam.getPeriod()
                                                                                       .getBegin()), Settlement::getInitiateTime, ObjectUtils.ifNull(queryParam.getPeriod(), Period::getBegin))
                .lt(Objects.nonNull(queryParam.getPeriod()) && Objects.nonNull(queryParam.getPeriod()
                                                                                       .getEnd()), Settlement::getInitiateTime, ObjectUtils.ifNull(queryParam.getPeriod(), Period::getEnd))
                .orderByDesc(Settlement::getInitiateTime)
                .page(PagingUtils.convert(queryParam));
    }

    private void sendMsg(List<Settlement> settlements, SupplierDTO supplier) {
        SessionContextHolder.requestTenant(supplier.getTenant());
        try {
            settlements.stream().forEach(settlement -> {
                Map<String, String> map = new HashMap<>();
                map.put("supplierName", supplier.getName());
                map.put("orderCode", settlement.getOrderCode());
                map.put("amount", String.valueOf(settlement.getAmount().setScale(2, RoundingMode.DOWN)));
                map.put("commission", String.valueOf(settlement.getCommission().setScale(2, RoundingMode.DOWN)));
                this.meattradeMsgNotifyUtils.supplierNotify(MEATTRADE_ORDER_SETTLEMENT_SUCCESS,
                                                            settlement.getId().toString(),
                                                            supplier,
                                                            map);
            });
        } catch (Exception e) {
            log.error("市场结算完成通知失败,失败原因:{}", e);
        }
    }
}
