package com.guhecloud.cube.biz.meattrade.server.wallet.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.finance.client.enumeration.BusinessCommand;
import com.guhecloud.cube.biz.finance.client.enumeration.BusinessType;
import com.guhecloud.cube.biz.finance.client.event.TransactionEvent;
import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.meattrade.server.notify.MeattradeMsgNotifyUtils;
import com.guhecloud.cube.biz.meattrade.server.wallet.dao.DepositMapper;
import com.guhecloud.cube.biz.meattrade.server.wallet.dto.SupplierDepositDTO;
import com.guhecloud.cube.biz.meattrade.server.wallet.entity.Deposit;
import com.guhecloud.cube.biz.meattrade.server.wallet.enumeration.RechargeStatus;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.DepositMerchantQueryParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.DepositQueryParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.RechargeParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.service.DepositService;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.pay.client.enumeration.PayType;
import com.guhecloud.cube.biz.wallet.client.AccountClient;
import com.guhecloud.cube.biz.wallet.client.DepositClient;
import com.guhecloud.cube.biz.wallet.client.dto.AccountAssetsDTO;
import com.guhecloud.cube.biz.wallet.client.enumeration.RechargeMode;
import com.guhecloud.cube.biz.wallet.client.message.RechargeMessage;
import com.guhecloud.cube.biz.wallet.client.param.AccountRechargeParam;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.RegexUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.redis.util.RedisUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账户充值业务操作实现
 *
 * @author Woody
 * @date 2021/9/7
 */
@Slf4j
@Service("meattrade_DepositService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DepositServiceImpl extends ServiceImpl<DepositMapper, Deposit> implements DepositService {
    private final DepositClient depositClient;
    private final SupplierClient supplierClient;
    private final UserClient userClient;
    private final AccountClient accountClient;
    private final MeattradeMsgNotifyUtils meattradeMsgNotifyUtils;

    private final static String MERCHANT_RECHARGE_SUCCESS = "MERCHANT_RECHARGE_SUCCESS";

    /**
     * 初始化充值记录
     *
     * @param id       充值记录主键
     * @param supplier 充值商户
     * @param mode     充值方式
     * @param amount   充值金额
     * @param datetime 充值时间
     */
    private void initialize(Long id, SupplierDTO supplier, RechargeMode mode,
                            BigDecimal amount, LocalDateTime datetime, Integer tenant
                           ) {
        Principal principal = SecurityContextHolder.getPrincipal();
        int shortId = Integer.parseInt(StringUtils.substring(String.valueOf(id), -6));
        Deposit deposit = Deposit.builder().id(id).shortId(shortId).userId(supplier.getAdminUserId())
                .supplierId(supplier.getId()).mode(mode).amount(amount).status(RechargeStatus.PROCESSING)
                .completeTime(datetime).tenant(tenant).createBy(principal.getAccount())
                .createTime(datetime).updateBy(principal.getAccount()).updateTime(datetime)
                .userName(supplier.getAdminUserName()).userPhone(supplier.getAdminPhone())
                .supplierPhone(supplier.getPhone())
                .supplierName(supplier.getName()).build();
        this.baseMapper.initializeDeposit(deposit);
    }

    private void initializeV2(Long id, Long userId, Long supplierId, RechargeMode mode,
                              BigDecimal amount, LocalDateTime datetime, Integer tenant
                             ) {
        Principal principal = SecurityContextHolder.getPrincipal();
        int shortId = Integer.parseInt(StringUtils.substring(String.valueOf(id), -6));
        Deposit deposit = Deposit.builder().id(id).shortId(shortId).userId(userId)
                .supplierId(supplierId).mode(mode).amount(amount).status(RechargeStatus.PROCESSING)
                .completeTime(datetime).tenant(tenant).createBy(principal.getAccount())
                .createTime(datetime).updateBy(principal.getAccount()).updateTime(datetime).build();
        if (userId != null) {
            UserDTO user = userClient.getUser(userId);
            deposit.setUserName(ObjectUtils.ifNull(user, u -> u.getName()));
            deposit.setUserPhone(ObjectUtils.ifNull(user, u -> u.getMobile()));
        }
        if (supplierId != null) {
            SupplierDTO supplier = supplierClient.getSupplierById(supplierId);
            deposit.setSupplierName(ObjectUtils.ifNull(supplier, s -> s.getName()));
            deposit.setSupplierPhone(ObjectUtils.ifNull(supplier, s -> s.getPhone()));
        }
        this.baseMapper.initializeDeposit(deposit);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long recharge(@NonNull Long supplierId, @NonNull RechargeParam param) {
        // 调用钱包账户充值接口
        Long id = RedisUtils.generateIdentity();
        SupplierDTO supplier = this.supplierClient.getSupplierById(supplierId);
        // 保存账户充值记录
        this.initialize(id, supplier, param.getMode(), param.getAmount(), LocalDateTime.now(), param.getTenant());
        // 账户变更
        this.depositClient.recharge(supplier.getAdminUserId(), AccountRechargeParam.builder().id(id).mode(param.getMode())
                .tenant(param.getTenant()).amount(param.getAmount()).supplierId(supplier.getId()).build());
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long rechargeV2(Long userId, RechargeParam param) {
        // 调用钱包账户充值接口
        Long id = RedisUtils.generateIdentity();
        // 保存账户充值记录
        this.initializeV2(id, userId, param.getSupplierId(), param.getMode(), param.getAmount(), LocalDateTime.now(), param.getTenant());

        this.depositClient.recharge(userId, AccountRechargeParam.builder().id(id).mode(param.getMode())
                .tenant(param.getTenant()).amount(param.getAmount()).supplierId(param.getSupplierId()).build());
        return id;
    }

    @Override
    public void consumeMessage(@NonNull RechargeMessage message) {
        // 初始化账户充值记录
        LocalDateTime datetime = DateUtils.parse(message.getTimestamp());
        this.initializeV2(message.getId(), message.getUserId(), message.getSupplierId(), message.getMode(), message.getAmount(), datetime, message.getTenant());
        // 完成账户充值记录（更新记录状态）
        this.baseMapper.complete(message.getId(), datetime);
        Deposit deposit = this.getById(message.getId());

        // 获取批发商信息
        SupplierDTO supplier = message.getSupplierId() == null ? null : this.supplierClient.getSupplierById(message.getSupplierId());
        if (supplier != null) {//目前商家充值 发送消息；采购商充值暂不发送消息
            // 发送充值消息
            this.sendMsg(message.getId(), message.getAmount(), supplier);
        }
        this.sendMessageToFinance(deposit, supplier);
    }

    private void sendMessageToFinance(Deposit deposit, SupplierDTO supplierDTO) {
        String payee = null;//付款人
        String payer = null;//收款人
        if (supplierDTO == null) {
            UserDTO user = userClient.getUser(deposit.getUserId());
            payee = ObjectUtils.ifNull(user, u -> u.getName());
            payer = ObjectUtils.ifNull(user, u -> u.getName());
        } else {
            payee = supplierDTO.getName();
            payer = supplierDTO.getName();
        }
        //向财务模块发送商家充值成功消息
        TransactionEvent transactionEvent = TransactionEvent.builder()
                .businessInfo(JsonUtils.serialize(deposit))
                .businessKey(deposit.getId().toString())
                .businessType(BusinessType.ENTER_CHARGE)
                .command(BusinessCommand.RECHARGE_AND_WITHDRAW)
                .payAmount(deposit.getAmount())
                .payChannel(RechargeMode.CASH == deposit.getMode() ? PayType.CASH_PAY
                                                                   : RechargeMode.WEIXIN_PAY == deposit.getMode() ? PayType.WEIXIN_PAY
                                                                                                                  : PayType.POS_PAY)
                .payScene(false)
                .payee(payee)
                .receivingAccount(null)
                .payeeAccountId(null)
                .payer(payer)//付款人
                .paymentAccount(null)//付款账户
                .marketIsIncome(true)
                .build();
        RocketMQContextHolder.send(TransactionEvent.TOPIC, transactionEvent);
    }

    @Override
    public Page<SupplierDepositDTO> listDepositSuppliers(@NonNull DepositMerchantQueryParam param, @NonNull Collection<Long> supplierIds) {
        return this.baseMapper.listDepositSuppliers(PagingUtils.convert(param), param, supplierIds);
    }

    @Override
    public Page<SupplierDepositDTO> listSupplierDeposits(@NonNull DepositQueryParam param, @NonNull List<Long> supplierIds) {
        Long id = RegexUtils.isInteger(param.getKeyword()) ? Long.parseLong(param.getKeyword()) : null;
        return this.baseMapper.listSupplierDeposits(PagingUtils.convert(param), id, param, supplierIds);
    }

    private void sendMsg(Long businessId, BigDecimal amount, SupplierDTO supplier) {
        try {
            // 获取账户余额信息
            AccountAssetsDTO userAccountAssets = this.accountClient.getUserAccountAssets(supplier.getAdminUserId(), supplier.getTenant());
            Map<String, String> map = new HashMap<>();
            map.put("supplierName", supplier.getName());
            map.put("date", DateUtils.format(DateUtils.now(), "yyyy年MM月dd日HH时mm分"));
            map.put("money", String.valueOf(amount));
            map.put("balance", String.valueOf(userAccountAssets.getAssets().setScale(2, RoundingMode.DOWN)));
            this.meattradeMsgNotifyUtils.supplierNotify(MERCHANT_RECHARGE_SUCCESS,
                                                        businessId.toString(),
                                                        supplier,
                                                        map);
        } catch (Exception e) {
            log.error("充值消息发送失败,失败原因:{}", e);
        }
    }
}
