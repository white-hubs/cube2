package com.guhecloud.cube.biz.meattrade.server.wallet.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.meattrade.server.notify.MeattradeMsgNotifyUtils;
import com.guhecloud.cube.biz.meattrade.server.wallet.dao.WithdrawMapper;
import com.guhecloud.cube.biz.meattrade.server.wallet.dto.SupplierWithdrawDTO;
import com.guhecloud.cube.biz.meattrade.server.wallet.entity.Withdraw;
import com.guhecloud.cube.biz.meattrade.server.wallet.enumeration.WithdrawStatus;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.MineWithdrawQueryParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.WithdrawParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.WithdrawQueryParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.service.WithdrawService;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.wallet.client.BankClient;
import com.guhecloud.cube.biz.wallet.client.WithdrawClient;
import com.guhecloud.cube.biz.wallet.client.dto.BankCardDTO;
import com.guhecloud.cube.biz.wallet.client.message.WithdrawMessage;
import com.guhecloud.cube.biz.wallet.client.param.AccountWithdrawParam;
import com.guhecloud.cube.biz.wallet.client.param.WithdrawRollbackParam;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.RegexUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.redis.util.RedisUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import jakarta.validation.constraints.NotNull;
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
import java.util.Map;
import java.util.Objects;

/**
 * 账户提现业务操作实现
 *
 * @author Woody
 * @date 2021/8/23
 */
@Slf4j
@Service("meattrade_WithdrawService")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WithdrawServiceImpl extends ServiceImpl<WithdrawMapper, Withdraw> implements WithdrawService {
    private final BankClient bankClient;
    private final UserClient userClient;
    private final WithdrawClient withdrawClient;
    private final SupplierClient supplierClient;
    private final MeattradeMsgNotifyUtils meattradeMsgNotifyUtils;

    private final static String MERCHANT_RECHARGE_SUCCESS = "MERCHANT_WITHDRAWAL_SUCCESS";
    private final static String MERCHANT_RECHARGE_FINISH = "MERCHANT_WITHDRAWAL_FINISH";
    private final static String MERCHANT_RECHARGE_FAIL = "MERCHANT_WITHDRAWAL_FAIL";

    /**
     * 初始化账户提现
     *
     * @param id       提现记录主键
     * @param supplier 商户信息
     * @param bankCard 银行卡信息
     * @param amount   提现金额
     * @param datetime 提现时间
     */
    private void initialize(Long id, SupplierDTO supplier, Withdraw.BankCard bankCard,
                            BigDecimal amount, LocalDateTime datetime, Integer tenant
                           ) {
        Principal principal = SecurityContextHolder.getPrincipal();
        int shortId = Integer.parseInt(StringUtils.substring(String.valueOf(id), -6));
        Withdraw withdraw = Withdraw.builder().id(id).shortId(shortId).userId(supplier.getAdminUserId())
                .userName(supplier.getAdminUserName()).userPhone(supplier.getAdminPhone())
                .supplierId(supplier.getId()).supplierName(supplier.getName()).supplierPhone(supplier.getPhone())
                .bankCardJson(JsonUtils.serialize(bankCard))
                .amount(amount).owner(principal.getName()).status(WithdrawStatus.AUDITING)
                .tenant(tenant).createBy(principal.getAccount()).createTime(datetime)
                .updateBy(principal.getAccount()).updateTime(datetime).build();
        this.baseMapper.initializeWithdraw(withdraw);
    }

    private void initializeV2(Long id, Long userId, Long supplierId, Withdraw.BankCard bankCard,
                              BigDecimal amount, LocalDateTime datetime, Integer tenant
                             ) {
        Principal principal = SecurityContextHolder.getPrincipal();
        int shortId = Integer.parseInt(StringUtils.substring(String.valueOf(id), -6));
        Withdraw withdraw = Withdraw.builder().id(id).shortId(shortId).userId(userId)
                .supplierId(supplierId).bankCardJson(JsonUtils.serialize(bankCard))
                .amount(amount).owner(principal.getName()).status(WithdrawStatus.AUDITING)
                .tenant(tenant).createBy(principal.getAccount()).createTime(datetime)
                .updateBy(principal.getAccount()).updateTime(datetime).build();
        if (userId != null) {
            UserDTO user = userClient.getUser(userId);
            withdraw.setUserName(ObjectUtils.ifNull(user, u -> u.getName()));
            withdraw.setUserPhone(ObjectUtils.ifNull(user, u -> u.getMobile()));
        }
        if (supplierId != null) {
            SupplierDTO supplier = supplierClient.getSupplierById(supplierId);
            withdraw.setSupplierName(ObjectUtils.ifNull(supplier, s -> s.getName()));
            withdraw.setSupplierPhone(ObjectUtils.ifNull(supplier, s -> s.getPhone()));
        }
        this.baseMapper.initializeWithdraw(withdraw);
    }

    @Override
    public String getOperator() {
        UserDTO user = this.userClient.getUser(SecurityContextHolder.getPrincipal().getId());
        String operator = ObjectUtils.ifNull(user, UserDTO::getName);
        Assert.isNotFound(operator, "操作员不存在");
        return operator;
    }

    @Override
    public Long withdraw(@NotNull Long userId, @NotNull WithdrawParam param) {
        // 验证商户有效性
        //该tenant 不同的端 传进来的值不一样，采购商和批发商不同
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        SupplierDTO supplier = this.supplierClient.getBySupplierTenant(tenant);

        Integer marketTenant = supplier == null ? tenant : supplier.getTenant();

        // 验证提现银行卡有效性
        BankCardDTO card = this.bankClient.getCard(param.getBankCardId(), marketTenant);
        Assert.isNotAcceptable(card == null || !Objects.equals(card.getUserId(), userId), "银行卡无效");
        Withdraw.BankCard bankCard = ObjectUtils.copying(card, Withdraw.BankCard.class);

        // 调用钱包账户提现接口
        Long id = RedisUtils.generateIdentity();
        this.withdrawClient.submit(userId, AccountWithdrawParam.builder().id(id).bankCardId(param.getBankCardId())
                .amount(param.getAmount()).password(param.getPassword())
                .supplierId(ObjectUtils.ifNull(supplier, s -> s.getId()))
                .tenant(marketTenant).build());

        // 保存商户提现记录
        try {
            this.initializeV2(id, userId, ObjectUtils.ifNull(supplier, s -> s.getId()), bankCard, param.getAmount(), LocalDateTime.now(), marketTenant);
        } catch (Exception e) {
            log.error("Saving withdraw failed", e);
        }
        // 发送提现完成消息
        Withdraw withdraw = Withdraw.builder().id(id).amount(param.getAmount()).tenant(marketTenant).build();
        this.sendMsg(withdraw, supplier, MERCHANT_RECHARGE_FINISH);
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void pass(@NonNull Withdraw withdraw, @NonNull String operator) {
        Assert.isNotAcceptable(withdraw.getStatus() != WithdrawStatus.AUDITING, "账户提现状态异常");
        Assert.isNotAcceptable(!this.baseMapper.pass(withdraw.getId(), operator, LocalDateTime.now()), "账户提现通过失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reject(@NonNull Withdraw withdraw, @NonNull String remark, @NonNull String operator) {
        withdraw.setAuditTime(LocalDateTime.now());//审核时间

        Assert.isNotAcceptable(withdraw.getStatus() != WithdrawStatus.AUDITING, "账户提现状态异常");
        Assert.isNotAcceptable(!this.baseMapper.reject(withdraw.getId(), remark, operator, withdraw.getAuditTime()),
                               "账户提现拒绝失败");
        WithdrawRollbackParam param = WithdrawRollbackParam.builder().remark(remark).tenant(withdraw.getTenant()).build();
        this.withdrawClient.reject(withdraw.getUserId(), withdraw.getId(), param);

        // 获取批发商信息
        SupplierDTO supplier = (withdraw.getSupplierId() == null ? null : this.supplierClient.getSupplierById(withdraw.getSupplierId()));

        // 发送消息
        this.sendMsg(withdraw, supplier, MERCHANT_RECHARGE_FAIL);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancel(@NonNull Withdraw withdraw, @NonNull String remark, @NonNull Integer tenant) {
        Assert.isNotAcceptable(withdraw.getStatus() != WithdrawStatus.AUDITING, "账户提现状态异常");
        Assert.isNotAcceptable(!this.baseMapper.cancel(withdraw.getId(), remark, LocalDateTime.now(), tenant), "账户提现取消失败");
        WithdrawRollbackParam param = WithdrawRollbackParam.builder().remark(remark).tenant(tenant).build();
        this.withdrawClient.cancel(withdraw.getUserId(), withdraw.getId(), param);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void complete(@NonNull Withdraw withdraw, @NonNull String operator, @NonNull Integer tenant) {
        Assert.isNotAcceptable(withdraw.getStatus() != WithdrawStatus.PASSED, "账户提现状态异常");
        Assert.isNotAcceptable(!this.baseMapper.complete(withdraw.getId(), operator, LocalDateTime.now(), tenant), "账户提现完成失败");
        this.withdrawClient.complete(withdraw.getUserId(), withdraw.getId(), tenant);
        //划账成功，发送体现成功消息
        SupplierDTO supplier = withdraw.getSupplierId() == null ? null : this.supplierClient.getSupplierById(withdraw.getSupplierId());
        // 发送消息
        this.sendMsg(withdraw, supplier, MERCHANT_RECHARGE_SUCCESS);
    }

    @Override
    public void consumeMessage(@NonNull WithdrawMessage message) {
        LocalDateTime datetime = DateUtils.parse(message.getTimestamp());
        if (message.getStatus() == com.guhecloud.cube.biz.wallet.client.enumeration.WithdrawStatus.FROZEN) {
            Withdraw.BankCard bankCard = ObjectUtils.copying(message.getBankCard(), Withdraw.BankCard.class);
            this.initializeV2(message.getId(), message.getUserId(), message.getSupplierId(), bankCard, message.getAmount(), datetime, message.getTenant());
        } else if (message.getStatus() == com.guhecloud.cube.biz.wallet.client.enumeration.WithdrawStatus.CANCELED) {
            this.baseMapper.cancel(message.getId(), message.getRemark(), datetime, message.getTenant());
        } else if (message.getStatus() == com.guhecloud.cube.biz.wallet.client.enumeration.WithdrawStatus.REJECTED) {
            this.baseMapper.reject(message.getId(), message.getRemark(), this.getOperator(), datetime);
        } else if (message.getStatus() == com.guhecloud.cube.biz.wallet.client.enumeration.WithdrawStatus.COMPLETED) {
            this.baseMapper.complete(message.getId(), this.getOperator(), datetime, message.getTenant());
        }
    }

    @Override
    public SupplierWithdrawDTO getSupplierWithdraw(@NonNull Long id) {
        return this.baseMapper.getMerchantWithdraw(id);
    }

    @Override
    public Page<SupplierWithdrawDTO> listSupplierWithdraws(@NonNull WithdrawQueryParam param, @NonNull Collection<Long> supplierIds) {
        Long id = RegexUtils.isInteger(param.getKeyword()) ? Long.parseLong(param.getKeyword()) : null;
        return this.baseMapper.listSupplierWithdraws(PagingUtils.convert(param), id, param, supplierIds);
    }

    @Override
    public Page<Withdraw> listUserWithdraws(@NonNull Long userId, @NonNull MineWithdrawQueryParam param) {
        LambdaQueryWrapper<Withdraw> query = Wrappers.<Withdraw>lambdaQuery().eq(Withdraw::getUserId, userId)
                .eq(Withdraw::getTenant, param.getTenant())
                .eq(Withdraw::getStatus, param.getStatus()).orderByDesc(Withdraw::getCreateTime);
        return this.baseMapper.selectPage(PagingUtils.convert(param), query);
    }

    private void sendMsg(Withdraw withdraw, SupplierDTO supplier, String code) {
        if (supplier == null) {
            return;//商户不存在直接返回
        }
        SessionContextHolder.requestTenant(supplier.getTenant());
        try {
            Map<String, String> sendParam = new HashMap<>();
            if (MERCHANT_RECHARGE_SUCCESS.equals(code)) {
                sendParam.put("supplierName", supplier.getName());
                sendParam.put("money", String.valueOf(withdraw.getAmount().setScale(2, RoundingMode.DOWN)));
            } else if (MERCHANT_RECHARGE_FINISH.equals(code)) {
                sendParam.put("supplierName", supplier.getName());
            } else if (MERCHANT_RECHARGE_FAIL.equals(code)) {
                sendParam.put("supplierName", supplier.getName());
                sendParam.put("money", String.valueOf(withdraw.getAmount().setScale(2, RoundingMode.DOWN)));
                sendParam.put("date", DateUtils.format(withdraw.getAuditTime(), "yyyy年MM月dd日HH时mm分"));
            }
            // 发送消息
            this.meattradeMsgNotifyUtils.supplierNotify(code,
                                                        withdraw.getId().toString(),
                                                        supplier,
                                                        sendParam);
        } catch (Exception e) {
            log.error("消息通知失败", e);
        }
    }
}
