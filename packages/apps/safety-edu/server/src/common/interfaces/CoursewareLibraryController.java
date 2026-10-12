package com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.BusinessKeysParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.CoursewareLibraryService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.ThemeService;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.commandgateway.command.ChangeCoursewareLibraryCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.commandgateway.command.IncreaseCoursewareLibraryCommand;
import com.guhecloud.cube.srv.gen.safety.education.server.common.application.querygateway.query.CoursewareLibraryQuery;
import com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po.CoursewareLibraryPO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.domain.po.ThemePO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.enumeration.SafetyEducationFileSceneCodeEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.common.interfaces.vo.CoursewareLibraryVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
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
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 课件库接口管理
 *
 * @author lanchun
 * @date 2023/4/18 11:43
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CoursewareLibraryController {

    private final CoursewareLibraryService coursewareLibraryService;

    private final ThemeService themeService;

    private final FileRefClient fileRefClient;

    private final FileClient fileClient;

    /**
     * 课件新增
     *
     * @param increaseCoursewareLibraryCommand
     *
     * @return
     */
    @PostMapping("/courseware-library")
    public Long save(@Valid @RequestBody IncreaseCoursewareLibraryCommand increaseCoursewareLibraryCommand) {
        FileVO fileVO = this.fileClient.getById(increaseCoursewareLibraryCommand.getFileId());
        Assert.isNotFound(fileVO, "上传的文件不存在");
        CoursewareLibraryPO coursewareLibraryPO = ObjectUtils.copying(increaseCoursewareLibraryCommand, CoursewareLibraryPO.class);
        coursewareLibraryPO.setFileName(fileVO.getName());
        coursewareLibraryPO.setFileSize(fileVO.getSize());
        this.coursewareLibraryService.save(coursewareLibraryPO);
        this.fileRefClient.refInduceSingle(SafetyEducationFileSceneCodeEnum.SAFETY_EDUCATION_COURSEWARE.getCode(), coursewareLibraryPO.getId(), increaseCoursewareLibraryCommand.getFileId(), null);
        return coursewareLibraryPO.getId();
    }

    /**
     * 课件修改
     *
     * @param id
     * @param changeCoursewareLibraryCommand
     */
    @PutMapping("/courseware-library/{id}")
    public void update(@PathVariable("id") Long id, @Valid @RequestBody ChangeCoursewareLibraryCommand changeCoursewareLibraryCommand) {
        CoursewareLibraryPO coursewareLibraryPO = this.coursewareLibraryService.getById(id);
        Assert.isNotFound(changeCoursewareLibraryCommand, "课件不存在");
        FileVO fileVO = this.fileClient.getById(changeCoursewareLibraryCommand.getFileId());
        Assert.isNotFound(fileVO, "上传的文件不存在");
        ObjectUtils.copying(changeCoursewareLibraryCommand, coursewareLibraryPO);
        coursewareLibraryPO.setFileName(fileVO.getName());
        coursewareLibraryPO.setFileSize(fileVO.getSize());
        this.coursewareLibraryService.updateById(coursewareLibraryPO);
        this.fileRefClient.removeByBusinessKey(coursewareLibraryPO.getId());
        this.fileRefClient.refInduceSingle(SafetyEducationFileSceneCodeEnum.SAFETY_EDUCATION_COURSEWARE.getCode(), coursewareLibraryPO.getId(), changeCoursewareLibraryCommand.getFileId(), null);
    }

    /**
     * 课件详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/courseware-library/{id}")
    public CoursewareLibraryVO getCoursewareLibrary(@PathVariable("id") Long id) {
        CoursewareLibraryPO coursewareLibraryPO = this.coursewareLibraryService.getById(id);
        Assert.isNotFound(coursewareLibraryPO, "课件不存在");
        CoursewareLibraryVO coursewareLibraryVO = ObjectUtils.copying(coursewareLibraryPO, CoursewareLibraryVO.class);
        //主题信息
        ThemePO themePO = this.themeService.getById(coursewareLibraryPO.getThemeId());
        if (Objects.nonNull(themePO)) {
            coursewareLibraryVO.setThemeName(themePO.getName());
        }
        List<FileVO> fileVOList = this.fileClient.getByBusinessKey(coursewareLibraryPO.getId());
        if (!CollectionUtils.isEmpty(fileVOList)) {
            coursewareLibraryVO.setFile(fileVOList.getFirst());
        }
        return coursewareLibraryVO;
    }

    /**
     * 课件分页列表
     *
     * @param coursewareLibraryQuery
     *
     * @return
     */
    @GetMapping("/courseware-library/page")
    public Pagination<CoursewareLibraryVO> getCoursewareLibraryPage(@Valid CoursewareLibraryQuery coursewareLibraryQuery) {
        Page<CoursewareLibraryVO> page = this.coursewareLibraryService.getCoursewareLibraryPage(coursewareLibraryQuery);
        if (CollectionUtils.isEmpty(page.getRecords())) {
            return Pagination.empty();
        }
        List<Long> ids = page.getRecords().stream().map(CoursewareLibraryVO::getId).collect(Collectors.toList());
        BusinessKeysParam businessKeysParam = new BusinessKeysParam();
        businessKeysParam.setBusinessKeys(ids);
        //获取附件
        Map<Long, List<FileVO>> fileMap = this.fileClient.getByBusinessKeys(businessKeysParam);
        if (!MapUtils.isEmpty(fileMap)) {
            page.getRecords().forEach(coursewareLibraryVO -> {
                if (fileMap.containsKey(coursewareLibraryVO.getId())) {
                    coursewareLibraryVO.setFile(fileMap.get(coursewareLibraryVO.getId()).getFirst());
                }
            });
        }
        return PagingUtils.convert(page);
    }

    /**
     * 课件删除
     *
     * @param id
     */
    @DeleteMapping("/courseware-library/{id}")
    public void removeCoursewareLibrary(@PathVariable("id") Long id) {
        DatabaseContextHolder.softDelete(CoursewareLibraryPO.class, id);
    }
}
