package com.guhecloud.cube.biz.meattrade.server.wallet.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.mdata.client.param.SupplierQueryParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.dto.SupplierDepositDTO;
import com.guhecloud.cube.biz.meattrade.server.wallet.entity.Deposit;
import com.guhecloud.cube.biz.meattrade.server.wallet.entity.VipCard;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.DepositMerchantQueryParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.DepositQueryParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.RechargeParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.service.DepositService;
import com.guhecloud.cube.biz.meattrade.server.wallet.service.VipCardService;
import com.guhecloud.cube.biz.meattrade.server.wallet.vo.DepositVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.annotation.Signable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 账户充值接口
 *
 * @author Woody
 * @date 2021/8/23
 */
@Validated
@RestController("meattrade_DepositController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DepositController {
    private final VipCardService vipCardService;
    private final DepositService depositService;
    private final SupplierClient supplierClient;

    /**
     * 商户充值（签名验证）
     *
     * @param supplierId 商户主键
     * @param param      充值参数
     *
     * @return 充值记录主键
     */
    //    @Signable
    @PostMapping("/supplier/{supplierId}/account/recharge")
    public Long recharge(@PathVariable("supplierId") @Min(value = 1, message = "商户主键不能小于1") Long supplierId,
                         @RequestBody @Valid @Signable RechargeParam param
                        ) {
        return this.depositService.recharge(supplierId, param);
    }

    //    @Signable
    @PostMapping("/supplier/{userId}/account/recharge/v2")
    public Long rechargeV2(@PathVariable("userId") @Min(value = 1, message = "用户主键不能小于1") Long userId,
                           @RequestBody @Valid @Signable RechargeParam param
                          ) {
        return this.depositService.rechargeV2(userId, param);
    }

    @GetMapping("/supplier/account/deposits")
    public Pagination<DepositVO> pageSupplierDeposits(@Valid DepositQueryParam param) {
        Period period = param.getPeriod();
        Page<Deposit> page = depositService.lambdaQuery().and(!StringUtils.isEmpty(param.getKeyword()),
                                                              q -> q.like(Deposit::getUserName, param.getKeyword())
                                                                      .or().like(Deposit::getSupplierName, param.getKeyword())
                                                                      .or().like(Deposit::getUserPhone, param.getKeyword())
                                                                      .or().like(Deposit::getSupplierPhone, param.getKeyword())
                                                                      .or().eq(Deposit::getId, param.getKeyword()))
                .eq(param.getStatus() != null, Deposit::getStatus, param.getStatus())
                .eq(Deposit::getTenant, param.getTenant())
                .between(period !=
                         null, Deposit::getCompleteTime, ObjectUtils.ifNull(period, p -> p.getBegin()), ObjectUtils.ifNull(period, p -> p.getEnd()))
                .orderByDesc(Deposit::getCompleteTime)
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, DepositVO.class);
    }

    /**
     * 查询充值商户
     *
     * @param param 查询参数
     *
     * @return 商户信息列表分页
     */
    @GetMapping("/deposit-suppliers")
    public Pagination<SupplierDepositDTO> listDepositSuppliers(@Valid DepositMerchantQueryParam param) {
        if (StringUtils.isEmpty(param.getKeyword())) {
            return Pagination.empty();
        } else if (this.vipCardService.isCiphertext(param.getKeyword())) {
            // 如果关键字参数满足会员卡密文模式，则对密文进行解码，然后使用会员卡号查询
            param.setKeyword(this.vipCardService.decode(param.getKeyword()).getCode());
        }
        // 会员卡查询商户
        List<VipCard> vipCards = this.vipCardService.listCards(param.getKeyword(), param.getTenant());
        List<Long> vipCardSupplierIds = vipCards.stream().map(VipCard::getSupplierId).collect(Collectors.toList());

        // 商户查询
        SupplierQueryParam queryParam = new SupplierQueryParam();
        queryParam.setTenant(param.getTenant());

        if (!CollectionUtils.isEmpty(vipCardSupplierIds)) {
            queryParam.setIds(vipCardSupplierIds);
            Pagination<SupplierDTO> page = this.supplierClient.pageSupplier(queryParam);
            List<SupplierDepositDTO> records = page.getRecords().stream().map(supplier -> {
                if (vipCardSupplierIds.contains(supplier.getId())) {
                    SupplierDepositDTO record = new SupplierDepositDTO();
                    record.setId(supplier.getId());
                    record.setName(supplier.getName());
                    record.setLiaison(supplier.getLiaison());
                    record.setPhone(supplier.getPhone());
                    return record;
                }
                return null;
            }).filter(Objects::nonNull).collect(Collectors.toList());
            return PagingUtils.convert(page, records);
        }
        // 条件为查询商户
        queryParam.setKeyword(param.getKeyword());
        Pagination<SupplierDTO> page = this.supplierClient.pageSupplier(queryParam);

        // 构建商户信息
        List<SupplierDepositDTO> records = page.getRecords().stream().map(record -> {
            SupplierDepositDTO dto = new SupplierDepositDTO();
            dto.setId(record.getId());
            dto.setName(record.getName());
            dto.setLiaison(record.getLiaison());
            dto.setPhone(record.getPhone());
            return dto;
        }).collect(Collectors.toList());
        return PagingUtils.convert(page, records);
    }
}
