package com.guhecloud.cube.biz.mdata.server.purchaser.controller;

import com.guhecloud.cube.biz.mdata.server.purchaser.dto.PurchaserDTO;
import com.guhecloud.cube.biz.mdata.server.purchaser.entity.Purchaser;
import com.guhecloud.cube.biz.mdata.server.purchaser.entity.PurchaserAuthentication;
import com.guhecloud.cube.biz.mdata.server.purchaser.param.PurchaserInnerModifyParam;
import com.guhecloud.cube.biz.mdata.server.purchaser.param.PurchaserModifyParam;
import com.guhecloud.cube.biz.mdata.server.purchaser.service.PurchaserAuthenticationService;
import com.guhecloud.cube.biz.mdata.server.purchaser.service.PurchaserService;
import com.guhecloud.cube.biz.mdata.server.purchaser.vo.PurchaserVO;
import com.guhecloud.cube.biz.ouaa.client.TenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.TenantDTO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 采购商接口
 *
 * @author lzy
 * @date 2021/12/24
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PurchaserController {

    private final PurchaserService purchaserService;
    private final PurchaserAuthenticationService purchaserAuthenticationService;

    private final TenantClient tenantClient;

    /**
     * 采购商新增
     */
    @PostMapping("/purchaser")
    public void addPurchaser(@Valid @RequestBody PurchaserModifyParam param) {
        this.purchaserService.addPurchaser(param);
    }

    /**
     * 采购商新增（内部调用）
     *
     * @param param 新增参数
     */
    @PostMapping("/purchaser/inner")
    public Long addInnerPurchaser(@Valid @RequestBody PurchaserInnerModifyParam param) {
        return this.purchaserService.addInnerPurchaser(param);
    }

    /**
     * 获取我的采购商市场
     *
     * @return 采购商市场信息
     */
    @GetMapping("/purchaser/markets")
    public List<PurchaserVO> listMarketPurchaser() {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        List<Purchaser> purchasers = this.purchaserService.listByUserId(userId);
        if (CollectionUtils.isEmpty(purchasers)) {
            return Collections.emptyList();
        }
        Set<Integer> tenants = purchasers.stream().map(Purchaser::getTenant).collect(Collectors.toSet());
        List<TenantDTO> tenantDTOS = this.tenantClient.listByCodes(tenants);
        Map<Integer, List<TenantDTO>> tenantMap = tenantDTOS.stream().collect(Collectors.groupingBy(TenantDTO::getCode));
        List<PurchaserVO> records = purchasers.stream().map(purchaser -> {
            PurchaserVO record = ObjectUtils.copying(purchaser, PurchaserVO.class);
            if (tenantMap.containsKey(purchaser.getTenant())) {
                TenantDTO tenant = tenantMap.get(purchaser.getTenant()).get(0);
                record.setMarketName(tenant.getName());
            }
            return record;
        }).collect(Collectors.toList());
        return records;
    }

    /**
     * 获取认证成功的采购商信息（内部调用）
     *
     * @param tenant 市场租户编号
     *
     * @return 采购商信息
     */
    @GetMapping("/purchaser/inner/active/{tenant}/tenant")
    public List<PurchaserDTO> listActivePurchasers(@PathVariable("tenant") Integer tenant) {
        List<PurchaserAuthentication> purchaserAuthentications = this.purchaserAuthenticationService.listByTenant(tenant);
        if (CollectionUtils.isEmpty(purchaserAuthentications)) {
            return Collections.emptyList();
        }
        Set<Long> purchaserIds = purchaserAuthentications.stream().map(PurchaserAuthentication::getPurchaserId).collect(Collectors.toSet());
        List<Purchaser> purchasers = this.purchaserService.listByIds(purchaserIds);
        return ObjectUtils.copying(purchasers, PurchaserDTO.class);
    }
}
