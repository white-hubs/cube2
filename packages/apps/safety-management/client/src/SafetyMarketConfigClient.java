package com.guhecloud.cube.base.dataintegrate.client;

import com.guhecloud.cube.base.dataintegrate.client.param.MarketConfigQueryParam;
import com.guhecloud.cube.base.dataintegrate.client.vo.MarketConfigVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/6/12 10:31
 * @Description:
 */
@FeignClient(name = "safetyManagement")
public interface SafetyMarketConfigClient {

    /**
     * 获取市场配置
     */
    @GetMapping("/safety/market-config/list")
    List<MarketConfigVO> getMarketConfigList(@SpringQueryMap MarketConfigQueryParam configQueryParam);
}
