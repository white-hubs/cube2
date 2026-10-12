package com.guhecloud.cube.srv.gen.maintain.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(ServiceRoute.TAG)
public interface InspectPlanClient {

    @GetMapping("/inspect/plan/getName/{id}")
    String getPlanName(@PathVariable("id") Long id);
}
