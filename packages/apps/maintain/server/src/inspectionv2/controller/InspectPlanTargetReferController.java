package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.controller;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectPlanTargetReferQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectPlanTargetReferVO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 巡检计划 前端控制器
 * </p>
 *
 * @author zwb
 * @since 2022-04-26
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectPlanTargetReferController {

    private final InspectPlanTargetReferQueryService queryService;

    /**
     * ID查询
     */
    @GetMapping("/inspect-plan/{planId}/target-refer/{targetId}")
    public InspectPlanTargetReferVO getById(@PathVariable("planId") Long planId,
                                            @PathVariable("targetId") Long targetId,
                                            @RequestParam(value = "taskId", defaultValue = "") Long taskId
                                           ) {
        return queryService.getVOByPlanIdAndTargetId(planId, targetId, taskId);
    }

}

