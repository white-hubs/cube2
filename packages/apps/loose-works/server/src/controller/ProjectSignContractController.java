package com.guhecloud.cube.srv.gen.loose.works.controller;

import com.guhecloud.cube.base.flow.client.param.query.ProcessInstancePageQuery;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectApplyListVO;
import com.guhecloud.cube.srv.gen.loose.works.service.ProjectSignContractService;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import java.util.List;

/**
 * 项目签约申请
 *
 * @author wql
 * @describe 项目签约
 * @date 2023/9/14 18:50
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectSignContractController {
    private final ProjectSignContractService projectSignContractService;

    /**
     * 小散工程签约列表
     *
     * @Author wql
     * @Description 小散工程签约列表
     * @Date 16:02 2023/9/15
     **/
    @GetMapping("project/signContract/list")
    public Pagination<ProjectApplyListVO> projectSignContractList(@Valid ProcessInstancePageQuery processInstancePageQuery) {
        return projectSignContractService.projectSignContractList(processInstancePageQuery);
    }

    /**
     * 批量删除工单签约
     */
    @PostMapping("project/signContract/delete")
    public void deleteProjectSignContractBatch(@RequestBody List<String> processInstanceIds) {
        projectSignContractService.deleteProjectSignContractBatch(processInstanceIds);
    }
}
