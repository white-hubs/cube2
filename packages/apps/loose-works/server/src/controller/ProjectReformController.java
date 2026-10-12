package com.guhecloud.cube.srv.gen.loose.works.controller;

import com.guhecloud.cube.srv.gen.loose.works.dto.ProjectReformQueryParam;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.BaseQuery;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectReformAnalysisVO;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectReformVO;
import com.guhecloud.cube.srv.gen.loose.works.service.ProjectReformService;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 工程整改
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/3/12 11:46
 * @Description:
 */

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProjectReformController {

    private final ProjectReformService projectReformService;

    /**
     * 小散工程整改列表
     *
     * @param processInstancePageQuery
     *
     * @return
     */
    @GetMapping("/project/reform/list")
    public Pagination<ProjectReformVO> projectReformList(@Valid ProjectReformQueryParam processInstancePageQuery) {
        return projectReformService.projectReformList(processInstancePageQuery);
    }

    /**
     * 小散工程-工程整改数
     *
     * @param query
     *
     * @return
     */
    @GetMapping("/project/reform/analysis")
    public ProjectReformAnalysisVO getProjectReformAnalysis(BaseQuery query) {
        return projectReformService.getProjectReformAnalysis(query);
    }

    /**
     * 批量删除工程整改
     *
     * @param processInstanceIds 流程实例id
     */
    @PostMapping("/project/reform/delete")
    public Boolean deleteProjectReformBatch(@RequestBody List<String> processInstanceIds) {
        return projectReformService.deleteProjectReformBatch(processInstanceIds);
    }
}
