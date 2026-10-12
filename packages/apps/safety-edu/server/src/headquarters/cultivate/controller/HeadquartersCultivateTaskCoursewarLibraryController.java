package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.controller;

import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.param.HeadquartersCultivateCoursewarParam;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.param.HeadquartersCultivateTaskCoursewarLibraryQueryParam;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.service.HeadquartersCultivateTaskCoursewarLibraryService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.vo.HeadquartersCultivateCoursewarVO;
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
 * 总部培训任务关联课件管理
 *
 * @author lanchun
 * @date 2024/4/10 15:08
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HeadquartersCultivateTaskCoursewarLibraryController {

    private final HeadquartersCultivateTaskCoursewarLibraryService headquartersCultivateTaskCoursewarLibraryService;

    /**
     * 功能描述： 培训课件管理添加
     */
    @PostMapping(value = "/headquarters/cultivate-task-coursewar-library")
    public Long add(@Valid @RequestBody HeadquartersCultivateCoursewarParam param) {
        return headquartersCultivateTaskCoursewarLibraryService.add(param);
    }

    /**
     * 功能描述： 培训课件管理修改
     */
    @PutMapping(value = "/headquarters/cultivate-task-coursewar-library/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody HeadquartersCultivateCoursewarParam param) {
        headquartersCultivateTaskCoursewarLibraryService.update(id, param);
    }

    /**
     * 功能描述： 培训课件管理删除
     */
    @DeleteMapping(value = "/headquarters/cultivate-task-coursewar-library/{id}")
    public void delete(@PathVariable Long id) {
        headquartersCultivateTaskCoursewarLibraryService.delete(id);
    }

    /**
     * 功能描述： 培训课件管理批量删除
     */
    @DeleteMapping(value = "/headquarters/cultivate-task-coursewar-library/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        headquartersCultivateTaskCoursewarLibraryService.batchDelete(ids);
    }

    /**
     * 功能描述： 培训课件管理分页
     */
    @GetMapping(value = "/headquarters/cultivate-task-coursewar-library/page")
    public Pagination<HeadquartersCultivateCoursewarVO> getPage(HeadquartersCultivateTaskCoursewarLibraryQueryParam param) {
        return headquartersCultivateTaskCoursewarLibraryService.getPage(param);
    }

    /**
     * 功能描述： 培训课件管理详情
     */
    @GetMapping(value = "/headquarters/cultivate-task-coursewar-library/{id}")
    public HeadquartersCultivateCoursewarVO getById(@PathVariable Long id) {
        return headquartersCultivateTaskCoursewarLibraryService.getById(id);
    }

}