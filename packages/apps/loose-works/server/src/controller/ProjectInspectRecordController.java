package com.guhecloud.cube.srv.gen.loose.works.controller;

import com.guhecloud.cube.srv.gen.loose.works.dto.request.BaseQuery;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.param.ProjectInspectRecordParam;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectInspectRecordVO;
import com.guhecloud.cube.srv.gen.loose.works.service.ProjectInspectRecordService;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 工程临时巡查
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/7/11 17:20
 * @Description:
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/project/inspect/record")
public class ProjectInspectRecordController {
    private final ProjectInspectRecordService projectInspectRecordService;

    /**
     * 新增临时巡查记录
     */
    @PostMapping("/submit")
    public Long submitProjectInspectRecord(@RequestBody ProjectInspectRecordParam param) {
        return projectInspectRecordService.submitProjectInspectRecord(param);
    }

    /**
     * 临时巡查记录
     */
    @GetMapping("/page")
    public Pagination<ProjectInspectRecordVO> getPageProjectInspectRecord(BaseQuery baseQuery) {
        return projectInspectRecordService.getPageProjectInspectRecord(baseQuery);
    }

    /**
     * 临时巡查记录详情
     */
    @GetMapping("/detail")
    public ProjectInspectRecordVO getProjectInspectRecordDetail(@RequestParam("id") Long id) {
        return projectInspectRecordService.getProjectInspectRecordDetail(id);
    }

    /**
     * 是否存在异常
     */
    @GetMapping("/abnormal")
    public Boolean isExitAbnormal(@RequestParam("id") Long id) {
        return projectInspectRecordService.isExitAbnormal(id);
    }
}
