package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.identify.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskIdentificationPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskIdentificationSubmitParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskUnitPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskIdentificationRecordVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskObjectVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.vo.RiskUnitVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.workflow.RiskIdentificationWorkflowService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.workflow.RiskUnitWorkflowService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 风险辨识接口。
 */
@RestController("securityManagementRiskIdentificationController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskIdentificationController {
    private final RiskIdentificationWorkflowService identificationWorkflowService;
    private final RiskUnitWorkflowService unitWorkflowService;

    /**
     * 查询风险辨识管理分页列表。
     *
     * @param param 查询条件，keyword 按单元名称模糊匹配；areaId 按所属区域筛选；
     * status 按辨识状态筛选，支持 IDENTIFYING/PENDING_REVIEW/REVIEWED，也兼容待辨识、辨识中、待审核、已审核
     * @return 风险单元维度的辨识管理分页结果；新增风险单元会以 IDENTIFYING 状态出现在该列表
     */
    @DataPermissionSwitch(dataModel = "riskControl:identification")
    @GetMapping("/risk-control/identification-units")
    public Pagination<RiskUnitVO> identificationUnits(RiskUnitPageParam param) {
        return unitWorkflowService.identificationUnitPage(param);
    }

    /**
     * 提交单元辨识对象集。
     *
     * @param unitId 风险单元 ID
     * @param param 辨识提交入参，objects 为本次显式新增、修改或删除的风险对象集合；新增对象 code 可选，未传由后端生成
     * @return 当前辨识轮次下的风险对象列表
     */
    @PostMapping("/risk-control/units/{unitId}/identifications")
    public List<RiskObjectVO> submitIdentification(@PathVariable Long unitId, @RequestBody RiskIdentificationSubmitParam param) {
        return identificationWorkflowService.submit(unitId, param);
    }

    /**
     * 查询辨识历史分页列表。
     *
     * @param param 查询条件，unitId 按单元筛选，objectId 按风险对象筛选；该接口只查已提交产生的辨识历史记录
     * @return 辨识记录分页结果
     */
    @DataPermissionSwitch(dataModel = "riskControl:identification")
    @GetMapping("/risk-control/identifications")
    public Pagination<RiskIdentificationRecordVO> identifications(RiskIdentificationPageParam param) {
        return identificationWorkflowService.identificationPage(param);
    }

    /**
     * 查询辨识历史详情。
     *
     * @param id 辨识记录 ID
     * @return 辨识记录详情，包含操作类型、提交人、提交时间和快照 JSON
     */
    @DataPermissionSwitch(dataModel = "riskControl:identification")
    @GetMapping("/risk-control/identifications/{id}")
    public RiskIdentificationRecordVO identificationDetail(@PathVariable Long id) {
        return identificationWorkflowService.identificationDetail(id);
    }
}
