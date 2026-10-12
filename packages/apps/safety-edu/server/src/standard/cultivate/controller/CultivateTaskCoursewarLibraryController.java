package com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.controller;

import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateCoursewarParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.param.CultivateTaskCoursewarLibraryQueryParam;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.service.CultivateTaskCoursewarLibraryService;
import com.guhecloud.cube.srv.gen.safety.education.server.standard.cultivate.vo.CultivateCoursewarVO;
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
 * @Description 培训课件管理
 * @date 2023-04-18 15:08:36
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CultivateTaskCoursewarLibraryController {
    private final CultivateTaskCoursewarLibraryService cultivateTaskCoursewarLibraryService;

    /**
     * 功能描述： 培训课件管理添加
     */
    @PostMapping(value = "/cultivate-task-coursewar-library")
    public Long add(@Valid @RequestBody CultivateCoursewarParam param) {
        return cultivateTaskCoursewarLibraryService.add(param);
    }

    /**
     * 功能描述： 培训课件管理修改
     */
    @PutMapping(value = "/cultivate-task-coursewar-library/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody CultivateCoursewarParam param) {
        cultivateTaskCoursewarLibraryService.update(id, param);
    }

    /**
     * 功能描述： 培训课件管理删除
     */
    @DeleteMapping(value = "/cultivate-task-coursewar-library/{id}")
    public void delete(@PathVariable Long id) {
        cultivateTaskCoursewarLibraryService.delete(id);
    }

    /**
     * 功能描述： 培训课件管理批量删除
     */
    @DeleteMapping(value = "/cultivate-task-coursewar-library/batchdelete")
    public void deleteBatch(@RequestParam(value = "ids") @NotBlank(message = "ids不能为空") List<Long> ids) {
        cultivateTaskCoursewarLibraryService.batchDelete(ids);
    }

    /**
     * 功能描述： 培训课件管理分页
     */
    @GetMapping(value = "/cultivate-task-coursewar-library/page")
    public Pagination<CultivateCoursewarVO> getPage(CultivateTaskCoursewarLibraryQueryParam param) {
        return cultivateTaskCoursewarLibraryService.getPage(param);
    }

    /**
     * 功能描述： 培训课件管理详情
     */
    @GetMapping(value = "/cultivate-task-coursewar-library/{id}")
    public CultivateCoursewarVO getById(@PathVariable Long id) {
        return cultivateTaskCoursewarLibraryService.getById(id);
    }

}