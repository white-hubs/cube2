package com.guhecloud.cube.biz.meattrade.server.wallet.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.meattrade.server.wallet.dto.SupplierWithdrawDTO;
import com.guhecloud.cube.biz.meattrade.server.wallet.entity.Withdraw;
import com.guhecloud.cube.biz.meattrade.server.wallet.enumeration.WithdrawStatus;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.MineWithdrawQueryParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.WithdrawAuditParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.WithdrawCancelParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.WithdrawParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.WithdrawQueryParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.service.WithdrawService;
import com.guhecloud.cube.biz.meattrade.server.wallet.vo.SupplierWithdrawVO;
import com.guhecloud.cube.biz.meattrade.server.wallet.vo.WithdrawVO;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ResourcePreconditionFailedException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.annotation.Signable;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 账户提现接口
 *
 * @author Woody
 * @date 2021/9/9
 */
@Validated
@RestController("meattrade_WithdrawController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WithdrawController {
    private final WithdrawService withdrawService;
    private final SupplierClient supplierClient;
    private final UserClient userClient;

    /**
     * 将用户提现DTO转换成VO
     *
     * @param dto 用户提现DTO
     *
     * @return 用户提现VO
     */
    private WithdrawVO convert(Withdraw dto) {
        WithdrawVO vo = ObjectUtils.copying(dto, WithdrawVO.class);
        if (!StringUtils.isEmpty(dto.getBankCardJson())) {
            Withdraw.BankCard card = JsonUtils.deserialize(dto.getBankCardJson(), Withdraw.BankCard.class);
            String shortCode = StringUtils.substring(card.getCode(), -4);
            WithdrawVO.BankCard bankCard = new WithdrawVO.BankCard();
            bankCard.setOwner(card.getOwner());
            bankCard.setName(String.format("%s(%s)", card.getBank().getDescription(), shortCode));
            vo.setBankCard(bankCard);
        }
        return vo;
    }

    /**
     * 商户提现（签名验证）
     *
     * @param param 提现参数
     *
     * @return 提现记录主键
     */
    @Signable
    @PostMapping("/mine/account/withdraw")
    public Long withdraw(@RequestBody @Valid @Signable(exclude = "password") WithdrawParam param) {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        return this.withdrawService.withdraw(userId, param);
    }

    /**
     * 获取我的提现信息
     *
     * @param id 提现记录主键
     *
     * @return 提现记录信息
     */
    @GetMapping("/mine/account/withdraw/{id}")
    public WithdrawVO getMineWithdraw(@PathVariable("id") @Min(value = 1, message = "提现主键不能小于1") Long id) {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        Withdraw withdraw = this.withdrawService.getById(id);
        Assert.isNotFound(withdraw, "提现记录不存在");
        Assert.isNotAcceptable(!Objects.equals(withdraw.getUserId(), userId), "提现记录无效");
        return this.convert(withdraw);
    }

    /**
     * 查询我的提现记录列表
     *
     * @param param 查询参数
     *
     * @return 提现记录列表分页
     */
    @GetMapping("/mine/account/withdraws")
    public Pagination<WithdrawVO> listMineWithdraws(@Valid MineWithdrawQueryParam param) {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        Page<Withdraw> page = this.withdrawService.listUserWithdraws(userId, param);
        List<Withdraw> records = page.getRecords();
        if (CollectionUtils.isEmpty(records)) {
            return Pagination.empty();
        }
        List<WithdrawVO> withdraws = records.stream().map(this::convert).collect(Collectors.toList());
        return PagingUtils.convert(page, withdraws);
    }

    /**
     * 取消我的账户提现
     *
     * @param id    提现记录主键
     * @param param 取消参数
     */
    @PutMapping("/mine/account/withdraw/{id}/cancel")
    public void cancel(@PathVariable("id") @Min(value = 1, message = "提现主键不能小于1") Long id,
                       @RequestBody @Valid WithdrawCancelParam param
                      ) {
        // 获取提现记录
        Long userId = SecurityContextHolder.getPrincipal().getId();
        Withdraw withdraw = this.withdrawService.getById(id);
        Assert.isNotFound(withdraw, "提现记录不存在");
        Assert.isNotAcceptable(!Objects.equals(withdraw.getUserId(), userId), "提现记录无效");

        // 取消提现
        this.withdrawService.cancel(withdraw, param.getRemark(), param.getTenant());
    }

    /**
     * 提现审核
     *
     * @param id    提现主键
     * @param param 审核参数
     */
    @PutMapping("/supplier/account/withdraw/{id}/audit")
    public void audit(@PathVariable("id") @Min(value = 1, message = "提现主键不能小于1") Long id,
                      @RequestBody @Valid WithdrawAuditParam param
                     ) {
        if (param.getStatus() == WithdrawStatus.REJECTED && StringUtils.isEmpty(param.getRemark())) {
            throw new ResourcePreconditionFailedException("remark", "拒绝原因不能为空");
        }

        // 获取提现记录
        Withdraw withdraw = this.withdrawService.getById(id);
        Assert.isNotFound(withdraw, "提现记录不存在");
        param.setTenant(withdraw.getTenant());

        // 提现审核
        String operator = SecurityContextHolder.getPrincipal().getName();//this.withdrawService.getOperator();
        if (param.getStatus() == WithdrawStatus.PASSED) {
            this.withdrawService.pass(withdraw, operator);
            return;
        } else if (param.getStatus() == WithdrawStatus.REJECTED) {
            this.withdrawService.reject(withdraw, param.getRemark(), operator);
            return;
        } else if (param.getStatus() == WithdrawStatus.COMPLETED) {
            this.withdrawService.complete(withdraw, operator, param.getTenant());
            return;
        }
        throw new ResourcePreconditionFailedException("status", "审核状态无效");
    }

    @GetMapping("/supplier/account/withdraws")
    public Pagination<SupplierWithdrawVO> listSupplierWithdraws(@Valid WithdrawQueryParam param) {
        Period period = param.getPeriod();
        Page<Withdraw> page = this.withdrawService.lambdaQuery().and(!StringUtils.isEmpty(param.getKeyword()),
                                                                     q -> q.like(Withdraw::getUserName, param.getKeyword())
                                                                             .or().like(Withdraw::getSupplierName, param.getKeyword())
                                                                             .or().like(Withdraw::getUserPhone, param.getKeyword())
                                                                             .or().like(Withdraw::getSupplierPhone, param.getKeyword())
                                                                             .or().eq(Withdraw::getId, param.getKeyword()))
                .eq(param.getStatus() != null, Withdraw::getStatus, param.getStatus())
                .eq(Withdraw::getTenant, param.getTenant())
                .between(period !=
                         null, Withdraw::getUpdateTime, ObjectUtils.ifNull(period, p -> p.getBegin()), ObjectUtils.ifNull(period, p -> p.getEnd()))
                .orderByDesc(Withdraw::getUpdateTime)
                .page(PagingUtils.convert(param));
        List<SupplierWithdrawVO> collect = Optional.ofNullable(page.getRecords()).orElse(Collections.emptyList()).stream().map(r -> {
            SupplierWithdrawVO wv = ObjectUtils.copying(r, SupplierWithdrawVO.class);
            wv.setSupplier(SupplierDTO.builder()
                                   .adminUserName(r.getUserName())
                                   .adminPhone(r.getUserPhone())
                                   .liaison(!StringUtils.isEmpty(r.getSupplierName()) ? r.getSupplierName() : r.getUserName())
                                   .phone(!StringUtils.isEmpty(r.getSupplierPhone()) ? r.getSupplierPhone() : r.getUserPhone())
                                   .name(r.getSupplierName()).build());
            return wv;
        }).collect(Collectors.toList());
        return PagingUtils.convert(page, collect);
    }

    /**
     * 获取商户提现信息
     *
     * @param id 提现记录主键
     *
     * @return 商户提现信息
     */

    @GetMapping("/supplier/account/withdraw/{id}")
    public SupplierWithdrawVO getSupplierWithdraw(@PathVariable("id") @Min(value = 1, message = "提现主键不能小于1") Long id) {
        SupplierWithdrawDTO withdraw = this.withdrawService.getSupplierWithdraw(id);
        Assert.isNotFound(withdraw, "提现记录不存在");
        SupplierWithdrawVO vo = ObjectUtils.copying(withdraw, SupplierWithdrawVO.class);
        if (withdraw.getSupplierId() != null) {
            SupplierDTO supplier = this.supplierClient.getSupplierById(withdraw.getSupplierId());
            vo.setSupplier(supplier);
        } else {//批发商ID为空 说明就是采购商
            UserDTO user = userClient.getUser(withdraw.getUserId());
            if (user != null) {
                vo.setSupplier(SupplierDTO.builder().liaison(user.getName()).phone(user.getMobile()).build());
            }
        }
        vo.setBankCard(JsonUtils.deserialize(withdraw.getBankCardJson(), Withdraw.BankCard.class));
        return vo;
    }
}
