package com.guhecloud.cube.base.flow.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vo.FormFieldVO;
import vo.FormFlowGroupTreeVO;

import java.util.List;

@FeignClient(name = ServiceRoute.TAG)
public interface FlowGroupClient {
    /**
     * 表单流程分组-列表(最末级)
     *
     * @return
     */
    @GetMapping("/form-flow-group/list")
    List<FormFieldVO> getConferenceTypeVos(@RequestParam(required = false) String keyword);

    /**
     * 表单流程分组-树
     *
     * @return
     */
    @GetMapping("/form-flow-group/tree")
    List<FormFlowGroupTreeVO> tree(@RequestParam(required = false) String keyword);
}
