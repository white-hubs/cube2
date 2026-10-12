package com.guhecloud.cube.srv.gen.maintain.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 维保
 */
@FeignClient("maintain")
public interface MaintainPlanClient {

    @GetMapping("/maintain/getPlanName/{id}")
    String getPlanName(@PathVariable Long id);
}
