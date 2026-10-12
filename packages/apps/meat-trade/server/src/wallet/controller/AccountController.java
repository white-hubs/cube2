package com.guhecloud.cube.biz.meattrade.server.wallet.controller;

import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.meattrade.server.wallet.param.AccountQueryParam;
import com.guhecloud.cube.biz.meattrade.server.wallet.vo.AccountAssetsVO;
import com.guhecloud.cube.biz.meattrade.server.wallet.vo.AssetsSummaryVO;
import com.guhecloud.cube.biz.meattrade.server.wallet.vo.SupplierAccountVO;
import com.guhecloud.cube.biz.wallet.client.AccountClient;
import com.guhecloud.cube.biz.wallet.client.dto.AccountAssetsDTO;
import com.guhecloud.cube.biz.wallet.client.dto.WalletAccountDTO;
import com.guhecloud.cube.biz.wallet.client.param.WalletAccountQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 账户接口
 *
 * @author Woody
 * @date 2021/9/14
 */
@Validated
@RestController("meattrade_AccountController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AccountController {
    private final AccountClient accountClient;
    private final SupplierClient supplierClient;

    /**
     * 获取所有资产汇总
     *
     * @return 资产汇总信息
     */
    @GetMapping("/supplier/account/assets")
    public AssetsSummaryVO getAssetsSummary(@RequestParam("tenant") Integer tenant) {
        AccountAssetsDTO total = this.accountClient.getAllAccountAssets(tenant);
        AccountAssetsDTO platform = this.accountClient.getTenantAccountAmount(tenant);
        return AssetsSummaryVO.builder().total(total.getAssets()).platform(platform.getAssets())
                .merchant(total.getAssets().subtract(platform.getAssets())).build();
    }

    /**
     * 获取市场资产信息
     *
     * @return 账户资产信息
     */
    @GetMapping("/platform/account/assets")
    public AccountAssetsVO getPlatformAssets(@RequestParam("tenant") Integer tenant) {
        AccountAssetsDTO assets = this.accountClient.getTenantAccountAmount(tenant);
        return ObjectUtils.copying(assets, AccountAssetsVO.class);
    }

    /**
     * 获取商户资产信息
     *
     * @param supplierId 商户主键
     *
     * @return 账户资产信息
     */
    @GetMapping("/supplier/{supplierId}/account/assets")
    public AccountAssetsVO getMerchantAssets(@PathVariable("supplierId") @Min(value = 1, message = "商户主键不能小于1") Long supplierId,
                                             @RequestParam("tenant") Integer tenant
                                            ) {
        SupplierDTO supplier = this.supplierClient.getSupplierById(supplierId);
        Assert.isNotFound(supplier, "不存在商户信息!");
        AccountAssetsDTO assets = this.accountClient.getUserAccountAssets(supplier.getAdminUserId(), tenant);
        return ObjectUtils.copying(assets, AccountAssetsVO.class);
    }

    /**
     * 查询商户账户信息
     *
     * @param param 查询参数
     *
     * @return 账户分页信息
     */
    @GetMapping("/supplier/accounts")
    public Pagination<SupplierAccountVO> listMerchantAccounts(@Valid AccountQueryParam param) {
        // 根据关键字查找商户
        Set<Long> userIds = null;
        List<SupplierDTO> suppliers = this.supplierClient.listSuppliers(param.getKeyword(), param.getTenant());
        if (!StringUtils.isEmpty(param.getKeyword())) {
            userIds = suppliers.stream().map(SupplierDTO::getAdminUserId).collect(Collectors.toSet());
        }
        if (!StringUtils.isEmpty(param.getKeyword()) && CollectionUtils.isEmpty(suppliers)) {
            return Pagination.empty();
        }

        // 查找商户对应的钱包账户
        WalletAccountQueryParam query = ObjectUtils.copying(param, WalletAccountQueryParam.class);
        query.setUserIds(userIds);
        Pagination<WalletAccountDTO> pagination = this.accountClient.listWalletAccounts(query);
        if (CollectionUtils.isEmpty(pagination.getRecords())) {
            return Pagination.empty();
        } else if (CollectionUtils.isEmpty(suppliers)) {
            // 根据钱包账户用户查询商户列表
            userIds = pagination.getRecords().stream().map(WalletAccountDTO::getUserId).collect(Collectors.toSet());
            suppliers = this.supplierClient.listByUserIds(userIds, param.getTenant());
        }
        Map<Long, SupplierDTO> userSuppliers = CollectionUtils.isEmpty(suppliers) ? Collections.emptyMap() :
                                               suppliers.stream()
                                                       .collect(Collectors.toMap(SupplierDTO::getAdminUserId, m -> m, (o, n) -> n));

        // 构建商户账户分页信息
        List<SupplierAccountVO> accounts = pagination.getRecords().stream().map(record -> {
            WalletAccountDTO.QuickPayment quickPayment = record.getQuickPayment();
            SupplierAccountVO account = ObjectUtils.copying(record, SupplierAccountVO.class);
            SupplierDTO supplier = userSuppliers.get(record.getUserId());
            SupplierDTO supplierDTO = ObjectUtils.copying(supplier, SupplierDTO.class);
            if (supplierDTO != null) {
                supplierDTO.setId(supplier.getId());
            }
            account.setSupplier(supplierDTO);
            account.setPaymentQuickly(ObjectUtils.ifNull(quickPayment, WalletAccountDTO.QuickPayment::getEnabled));
            return account;
        }).collect(Collectors.toList());
        return PagingUtils.convert(pagination, accounts);
    }
}
