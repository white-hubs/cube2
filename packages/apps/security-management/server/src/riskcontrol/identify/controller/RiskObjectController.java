package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskObjectPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskObjectVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.workflow.RiskIdentificationWorkflowService;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * 风险对象接口。
 */
@RestController("securityManagementRiskObjectController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskObjectController {
    private final RiskIdentificationWorkflowService identificationWorkflowService;

    /**
     * 查询风险对象分页列表。
     *
     * @param param 查询条件，unitId 按单元筛选；reviewResult=REJECTED 时查询退回对象
     * @return 风险对象分页结果
     */
    @GetMapping("/risk-control/risk-objects")
    public Pagination<RiskObjectVO> riskObjects(RiskObjectPageParam param) {
        return identificationWorkflowService.objectPage(param);
    }

    /**
     * 查询风险对象详情。
     *
     * @param objectId 风险对象 ID
     * @return 风险对象详情，包含危险源、事故类型、责任人、状态和风险等级
     */
    @GetMapping("/risk-control/risk-objects/{objectId}")
    public RiskObjectVO riskObjectDetail(@PathVariable Long objectId) {
        return identificationWorkflowService.objectDetail(objectId);
    }
}
