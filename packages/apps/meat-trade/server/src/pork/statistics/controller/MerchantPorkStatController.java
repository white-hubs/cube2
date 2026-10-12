package com.guhecloud.cube.biz.meattrade.server.pork.statistics.controller;

import com.guhecloud.cube.biz.mdata.client.SupplierClient;
import com.guhecloud.cube.biz.mdata.client.dto.SupplierDTO;
import com.guhecloud.cube.biz.meattrade.server.pork.statistics.service.MerchantPorkStatService;
import com.guhecloud.cube.biz.meattrade.server.pork.statistics.vo.MerchantSpotCheckStatVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 商户统计相关接口
 *
 * @author lanchun
 * @date 2025/1/23 14:34
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MerchantPorkStatController {

    private final MerchantPorkStatService merchantPorkStatService;

    private final SupplierClient supplierClient;

    /**
     * 获取商户食安抽检统计
     *
     * @param supplierId 商户id
     *
     * @return MerchantSpotCheckStatVO
     */
    @GetMapping("/merchant/{supplierId}/spot-check/stat")
    public MerchantSpotCheckStatVO getMerchantSpotCheckStat(@PathVariable("supplierId") Long supplierId) {
        SupplierDTO supplier = this.supplierClient.getSupplierById(supplierId);
        if (Objects.isNull(supplier)) {
            return null;
        }
        return merchantPorkStatService.getMerchantSpotCheckStat(supplier.getCode());
    }
}
