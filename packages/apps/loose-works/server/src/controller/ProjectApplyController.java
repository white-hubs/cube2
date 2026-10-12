package com.guhecloud.cube.srv.gen.loose.works.controller;

import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.BaseQuery;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.ProjectStatisticsQuery;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectApplyListVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectDetailVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectNodeInfo;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectStatisticsVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.projectNameListVO;
import com.guhecloud.cube.srv.gen.loose.works.service.ProjectApplyService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 小散工程申请
 *
 * @author wql
 * @describe 工程申请
 * @date 2023/9/14 18:48
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectApplyController {
    private final ProjectApplyService projectApplyService;

    /**
     * 小散工程统计面板
     *
     * @Author wql
     * @Description 小散工程统计面板
     * @Date 19:00 2023/9/14
     **/
    @GetMapping("project/statistics")
    public ProjectStatisticsVO projectStatistics(ProjectStatisticsQuery query) {
        return projectApplyService.projectStatistics(query);
    }

    /**
     * 小散工程详情
     *
     * @Author wql
     * @Description 小散工程详情
     * @Date 19:20 2023/9/14
     **/
    @GetMapping("project/detail/{id}")
    public ProjectDetailVO projectDetail(@NotNull @PathVariable("id") Long id) {
        Assert.isParameterError(id == null, "流程实例id不能为空!");
        return projectApplyService.projectDetail(id);
    }

    /**
     * 小散工程申请单列表
     *
     * @Author wql
     * @Description 小散工程申请单列表
     * @Date 16:02 2023/9/15
     **/
    @GetMapping("project/apply/list")
    public Pagination<ProjectApplyListVO> projectApplyList(@Valid ProcessInstancePageQuery processInstancePageQuery) {
        return projectApplyService.projectApplyList(processInstancePageQuery);
    }

    /**
     * 小散工程节点列表
     *
     * @Author wql
     * @Description 小散工程节点列表
     * @Date 15:34 2023/9/22
     **/
    @GetMapping("project/apply/nodeList")
    public Pagination<ProjectNodeInfo> projectNodeInfoList(BaseQuery query) {
        return projectApplyService.projectNodeInfoList(query);
    }

    /**
     * 小散工程名称列表
     *
     * @Author wql
     * @Description 小散工程名称列表
     * @Date 9:20 2023/9/18
     **/
    @GetMapping("project/apply/nameList")
    public List<projectNameListVO> projectNameList() {
        return projectApplyService.projectNameList();
    }

    /**
     * 批量删除工程申请
     *
     * @param processInstanceIds 流程实例id
     */
    @PostMapping("project/apply/deleted")
    public void deleteProjectApply(@RequestBody List<String> processInstanceIds) {
        projectApplyService.deleteProjectApplyBatch(processInstanceIds);
    }

}
