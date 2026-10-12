package com.guhecloud.cube.srv.gen.reportmanagement.server.controller;

import com.guhecloud.cube.srv.gen.reportmanagement.server.param.WorkPlanParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.WorkPlanQueryParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.service.WorkPlanService;
import com.guhecloud.cube.srv.gen.reportmanagement.server.vo.WorkPlanStatVO;
import com.guhecloud.cube.srv.gen.reportmanagement.server.vo.WorkPlanVO;
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
 * @Description 工作安排
 * @date 2024-01-04 10:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WorkPlanController {
    private final WorkPlanService workPlanService;

    /**
     * 功能描述： 工作安排添加
     */
    @PostMapping(value = "/work-plan")
    public Long add(@Valid @RequestBody WorkPlanParam param) {
        return workPlanService.add(param);
    }

    /**
     * 功能描述： 工作安排修改
     */
    @PutMapping(value = "/work-plan/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody WorkPlanParam param) {
        workPlanService.update(id, param);
    }

    /**
     * 功能描述： 工作安排删除
     */
    @DeleteMapping(value = "/work-plan/{id}")
    public void delete(@PathVariable Long id) {
        workPlanService.delete(id);
    }

    /**
     * 功能描述： 工作安排批量删除
     */
    @DeleteMapping(value = "/work-plan/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        workPlanService.batchDelete(ids);
    }

    /**
     * 功能描述： 工作安排分页
     */
    @GetMapping(value = "/work-plan/page")
    public Pagination<WorkPlanVO> getPage(WorkPlanQueryParam param) {
        return workPlanService.getPage(param);
    }

    /**
     * 功能描述： 工作安排最近12小时 列表
     */
    @GetMapping(value = "/work-plan/list")
    public List<WorkPlanVO> getList(WorkPlanQueryParam param) {
        return workPlanService.getList(param);
    }

    /**
     * 功能描述： 工作安排详情
     */
    @GetMapping(value = "/work-plan/{id}")
    public WorkPlanVO getById(@PathVariable Long id) {
        return workPlanService.getById(id);
    }

    /**
     * 根据发布人统计发布数量
     *
     * @param param
     *
     * @return
     */
    public List<WorkPlanStatVO> getWorkPlanStatByPerson(WorkPlanQueryParam param) {
        return this.workPlanService.getWorkPlanStatByPerson(param);
    }

    /**
     * 根据部门统计发布数量
     *
     * @param param
     *
     * @return
     */
    public List<WorkPlanStatVO> getWorkPlanStatByDept(WorkPlanQueryParam param) {
        return this.workPlanService.getWorkPlanStatByDept(param);
    }
}