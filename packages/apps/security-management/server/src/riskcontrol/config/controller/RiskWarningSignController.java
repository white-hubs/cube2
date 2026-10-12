package com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.config.controller;

import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskWarningSignPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.api.param.RiskWarningSignParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskOptionVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.common.vo.RiskWarningSignVO;
import com.guhecloud.cube.srv.gen.securitymanagement.server.riskcontrol.workflow.RiskConfigWorkflowService;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 风险警示标志接口。
 */
@RestController("securityManagementRiskWarningSignController")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RiskWarningSignController {
    private final RiskConfigWorkflowService configWorkflowService;

    /**
     * 查询警示标志分页列表。
     *
     * @param param 查询条件，keyword 按标志名称模糊匹配；signType 按标志类型筛选；enabled 按启用状态筛选
     * @return 警示标志分页结果，仅包含 id、code、name、signType、iconFileId、iconFile 和 enabled
     */
    @GetMapping("/risk-control/warning-signs")
    public Pagination<RiskWarningSignVO> warningSigns(RiskWarningSignPageParam param) {
        return configWorkflowService.warningSignPage(param);
    }

    /**
     * 查询警示标志下拉选项。
     *
     * @param param 查询条件，keyword 按标志名称模糊匹配；signType 按标志类型筛选
     * @return 警示标志选项列表，包含 id、code、name、iconFileId 和 iconFile
     */
    @GetMapping("/risk-control/warning-sign-options")
    public List<RiskOptionVO> warningSignOptions(RiskWarningSignPageParam param) {
        return configWorkflowService.warningSignOptions(param);
    }

    /**
     * 查询警示标志详情。
     *
     * @param id 警示标志 ID
     * @return 警示标志详情，包含编号、名称、类型、图标文件 ID、图标文件对象和启用状态
     */
    @GetMapping("/risk-control/warning-signs/{id}")
    public RiskWarningSignVO warningSignDetail(@PathVariable Long id) {
        return configWorkflowService.warningSignDetail(id);
    }

    /**
     * 新增警示标志。
     *
     * @param param 警示标志入参，code 可选，未传由后端生成；包含 name、signType、iconFileId、enabled；iconFileId 为文件服务使用场景码 RISK::CONTROL_WARNING_SIGN_ICON 上传得到的文件 ID
     * @return 新增后的警示标志详情
     */
    @PostMapping("/risk-control/warning-signs")
    public RiskWarningSignVO createWarningSign(@RequestBody RiskWarningSignParam param) {
        return configWorkflowService.createWarningSign(param);
    }

    /**
     * 修改警示标志。
     *
     * @param id 警示标志 ID
     * @param param 警示标志更新入参，code 可选，未传保持原编号；包含 name、signType、iconFileId、enabled；iconFileId 为文件服务使用场景码 RISK::CONTROL_WARNING_SIGN_ICON 上传得到的文件 ID
     * @return 更新后的警示标志详情
     */
    @PutMapping("/risk-control/warning-signs/{id}")
    public RiskWarningSignVO updateWarningSign(@PathVariable Long id, @RequestBody RiskWarningSignParam param) {
        return configWorkflowService.updateWarningSign(id, param);
    }

    /**
     * 删除警示标志。
     *
     * @param id 警示标志 ID
     */
    @DeleteMapping("/risk-control/warning-signs/{id}")
    public void deleteWarningSign(@PathVariable Long id) {
        configWorkflowService.deleteWarningSign(id);
    }
}
