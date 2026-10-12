package com.guhecloud.cube.base.flow.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import vo.FormFlowDetailVO;

@FeignClient(name = ServiceRoute.TAG)
public interface FormClient {

    @GetMapping("/form/instance/{id}/definition")
    Long getDefinitionId(@PathVariable("id") Long id);

    @GetMapping("/form-flow/{id}")
    FormFlowDetailVO get(@PathVariable("id") Long id);

}
