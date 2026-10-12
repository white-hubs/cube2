package com.guhecloud.cube.srv.gen.reportmanagement.server.controller;

import com.guhecloud.cube.srv.gen.reportmanagement.server.param.WorkPlanTypeParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.WorkPlanTypeQueryParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.service.WorkPlanTypeService;
import com.guhecloud.cube.srv.gen.reportmanagement.server.vo.WorkPlanTypeVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Appet
 * @Description 工作安排类型
 * @date 2024-01-04 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkPlanTypeController {
    private final WorkPlanTypeService workPlanTypeService;

    /**
     * 功能描述： 工作安排类型添加
     */
    @PostMapping(value = "/work-plan-type")
    public Long add(@Valid @RequestBody WorkPlanTypeParam param) {
        return workPlanTypeService.add(param);
    }

    /**
     * 功能描述： 工作安排类型修改
     */
    @PutMapping(value = "/work-plan-type/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody WorkPlanTypeParam param) {
        workPlanTypeService.update(id, param);
    }

    @PutMapping(value = "/work-plan-type/{id}/status/{status}")
    public void updateStatus(@PathVariable Long id, @PathVariable Boolean status) {
        workPlanTypeService.updateStatus(id, status);
    }

    /**
     * 功能描述： 工作安排类型删除
     */
    @DeleteMapping(value = "/work-plan-type/{id}")
    public void delete(@PathVariable Long id) {
        workPlanTypeService.delete(id);
    }

    /**
     * 功能描述： 工作安排类型批量删除
     */
    @DeleteMapping(value = "/work-plan-type/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        workPlanTypeService.batchDelete(ids);
    }

    /**
     * 功能描述： 工作安排类型分页
     */
    @GetMapping(value = "/work-plan-type/page")
    public Pagination<WorkPlanTypeVO> getPage(WorkPlanTypeQueryParam param) {
        return workPlanTypeService.getPage(param);
    }

    /**
     * 功能描述： 工作安排类型列表
     */
    @GetMapping(value = "/work-plan-type/list")
    public List<WorkPlanTypeVO> getList(WorkPlanTypeQueryParam param) {
        return workPlanTypeService.getList(param);
    }

    /**
     * 功能描述： 工作安排类型详情
     */
    @GetMapping(value = "/work-plan-type/{id}")
    public WorkPlanTypeVO getById(@PathVariable Long id) {
        return workPlanTypeService.getById(id);
    }

}