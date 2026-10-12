package com.guhecloud.cube.property.management.client.property.client.property;

import com.guhecloud.cube.property.management.client.property.param.consumable.ConsumableReduceStockParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/2/4 15:11
 * @Description: 耗材管理client
 */
@FeignClient(name = "propertyManagement")
public interface ConsumableManageClient {

    /**
     * 耗材出库提交-client接口
     */
    @PostMapping("/consumable/outbound/submit/client")
    Long outboundSubmitClient(@RequestBody ConsumableReduceStockParam param);
}
