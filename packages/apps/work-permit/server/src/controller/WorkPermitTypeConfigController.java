package com.guhecloud.cube.srv.gen.work.permit.server.controller;

import com.guhecloud.cube.srv.gen.work.permit.server.param.WorkPermitAssociatedItemQueryParam;
import com.guhecloud.cube.srv.gen.work.permit.server.param.WorkPermitTypeConfigParam;
import com.guhecloud.cube.srv.gen.work.permit.server.param.WorkPermitTypeConfigQueryParam;
import com.guhecloud.cube.srv.gen.work.permit.server.service.WorkPermitTypeConfigService;
import com.guhecloud.cube.srv.gen.work.permit.server.vo.WorkPermitAssociatedItemPageVO;
import com.guhecloud.cube.srv.gen.work.permit.server.vo.WorkPermitTypeConfigDetailVO;
import com.guhecloud.cube.srv.gen.work.permit.server.vo.WorkPermitTypeConfigNameVO;
import com.guhecloud.cube.srv.gen.work.permit.server.vo.WorkPermitTypeConfigPageVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
 * 作业类型配置接口。
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkPermitTypeConfigController {

    private final WorkPermitTypeConfigService workPermitTypeConfigService;

    /**
     * 新增作业类型配置。
     */
    @PostMapping("/work-permit-types")
    public Long add(@Valid @RequestBody WorkPermitTypeConfigParam param) {
        return workPermitTypeConfigService.add(param);
    }

    /**
     * 修改作业类型配置。
     */
    @PutMapping("/work-permit-types/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody WorkPermitTypeConfigParam param) {
        workPermitTypeConfigService.update(id, param);
    }

    /**
     * 删除作业类型配置。
     */
    @DeleteMapping("/work-permit-types/{id}")
    public void delete(@PathVariable Long id) {
        workPermitTypeConfigService.delete(id);
    }

    /**
     * 查询作业类型配置详情。
     */
    @GetMapping("/work-permit-types/{id}")
    public WorkPermitTypeConfigDetailVO getById(@PathVariable Long id) {
        return workPermitTypeConfigService.getById(id);
    }

    /**
     * 查询作业类型配置分页列表。
     */
    @GetMapping("/work-permit-types/page")
    public Pagination<WorkPermitTypeConfigPageVO> getPage(WorkPermitTypeConfigQueryParam param) {
        return workPermitTypeConfigService.getPage(param);
    }

    /**
     * 查询全量作业等级列表。
     */
    @GetMapping("/work-permit-types/levels")
    public List<String> listLevels(WorkPermitTypeConfigQueryParam param) {
        return workPermitTypeConfigService.listLevels(param);
    }

    /**
     * 查询作业名称列表。
     */
    @GetMapping("/work-permit-types/names")
    public List<WorkPermitTypeConfigNameVO> listNames(WorkPermitTypeConfigQueryParam param) {
        return workPermitTypeConfigService.listNames(param);
    }

    /**
     * 查询检查项分页列表。
     */
    @GetMapping("/work-permit-types/risk-check-items/page")
    public Pagination<WorkPermitAssociatedItemPageVO> getRiskCheckItemPage(WorkPermitAssociatedItemQueryParam param) {
        return workPermitTypeConfigService.getRiskCheckItemPage(param);
    }

    /**
     * 查询检查项全量ID列表。
     */
    @GetMapping("/work-permit-types/risk-check-items/ids")
    public List<Long> listRiskCheckItemIds(WorkPermitAssociatedItemQueryParam param) {
        return workPermitTypeConfigService.listRiskCheckItemIds(param);
    }

    /**
     * 查询验收项分页列表。
     */
    @GetMapping("/work-permit-types/acceptance-items/page")
    public Pagination<WorkPermitAssociatedItemPageVO> getAcceptanceItemPage(WorkPermitAssociatedItemQueryParam param) {
        return workPermitTypeConfigService.getAcceptanceItemPage(param);
    }

    /**
     * 查询验收项全量ID列表。
     */
    @GetMapping("/work-permit-types/acceptance-items/ids")
    public List<Long> listAcceptanceItemIds(WorkPermitAssociatedItemQueryParam param) {
        return workPermitTypeConfigService.listAcceptanceItemIds(param);
    }

    /**
     * 导出作业类型配置。
     */
    @GetMapping("/work-permit-types/export")
    public void export(WorkPermitTypeConfigQueryParam param, HttpServletResponse response) {
        workPermitTypeConfigService.export(param, response);
    }
}
