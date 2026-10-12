package com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileClient;
import com.guhecloud.cube.base.file.client.FileOperateClient;
import com.guhecloud.cube.base.file.client.param.FileInduceParam;
import com.guhecloud.cube.base.file.client.param.FileParam;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.srv.gen.safety.education.server.common.enumeration.SafetyEducationFileSceneCodeEnum;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.dao.HeadquartersCultivateTaskCoursewarLibraryMapper;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.entity.HeadquartersCultivateTaskCoursewareLibrary;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.param.HeadquartersCultivateCoursewarParam;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.param.HeadquartersCultivateTaskCoursewarLibraryQueryParam;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.service.HeadquartersCultivateTaskCoursewarLibraryService;
import com.guhecloud.cube.srv.gen.safety.education.server.headquarters.cultivate.vo.HeadquartersCultivateCoursewarVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 培训课件管理服务层
 *
 * @author lanchun
 * @date 2024/4/10 15:04
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HeadquartersCultivateTaskCoursewarLibraryServiceImpl extends ServiceImpl<HeadquartersCultivateTaskCoursewarLibraryMapper, HeadquartersCultivateTaskCoursewareLibrary> implements HeadquartersCultivateTaskCoursewarLibraryService {
    private final FileClient fileClient;
    private final FileOperateClient fileOperateClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long add(HeadquartersCultivateCoursewarParam param) {
        HeadquartersCultivateTaskCoursewareLibrary cultivateTaskCoursewarLibrary = ObjectUtils.copying(param, HeadquartersCultivateTaskCoursewareLibrary.class);
        this.baseMapper.insert(cultivateTaskCoursewarLibrary);
        if (StringUtils.isNotBlank(param.getFileIds())) {
            FileInduceParam fileInduceParam = new FileInduceParam();
            fileInduceParam.setFileIdStr(param.getFileIds());
            fileInduceParam.setSceneCode(SafetyEducationFileSceneCodeEnum.SAFETY_EDUCATION_COURSEWARE.getCode());
            fileInduceParam.setBusinessKey(cultivateTaskCoursewarLibrary.getId());
            fileOperateClient.fileInduceSingleBody(fileInduceParam);
        }
        return cultivateTaskCoursewarLibrary.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(Long id, HeadquartersCultivateCoursewarParam param) {
        HeadquartersCultivateTaskCoursewareLibrary cultivateTaskCoursewarLibrary = this.baseMapper.selectById(id);
        Assert.isNotFound(cultivateTaskCoursewarLibrary, "未查询到相关信息");
        if (!StringUtils.equals(cultivateTaskCoursewarLibrary.getFileIds(), param.getFileIds())) {
            if (StringUtils.isNotBlank(cultivateTaskCoursewarLibrary.getFileIds())) {
                FileParam fileParam = new FileParam();
                fileParam.setIds(Arrays.stream(cultivateTaskCoursewarLibrary.getFileIds().split(",")).map(Long::valueOf)
                                         .collect(Collectors.toList()));
                fileOperateClient.physicalDeleteByIds(fileParam);
            }
            if (StringUtils.isNotBlank(param.getFileIds())) {
                FileInduceParam fileInduceParam = new FileInduceParam();
                fileInduceParam.setBusinessKey(id);
                fileInduceParam.setFileIdStr(param.getFileIds());
                fileInduceParam.setSceneCode(SafetyEducationFileSceneCodeEnum.SAFETY_EDUCATION_COURSEWARE.getCode());
                fileOperateClient.fileInduceSingleBody(fileInduceParam);
            }
        }
        ObjectUtils.copying(param, cultivateTaskCoursewarLibrary);
        this.baseMapper.updateById(cultivateTaskCoursewarLibrary);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDelete(List<Long> ids) {
        ids.stream().forEach(id -> DatabaseContextHolder.softDelete(this, id));
    }

    @Override
    public HeadquartersCultivateCoursewarVO getById(Long id) {
        HeadquartersCultivateTaskCoursewareLibrary cultivateTaskCoursewarLibrary = this.baseMapper.selectById(id);
        return ObjectUtils.copying(cultivateTaskCoursewarLibrary, HeadquartersCultivateCoursewarVO.class);
    }

    @Override
    public Pagination<HeadquartersCultivateCoursewarVO> getPage(HeadquartersCultivateTaskCoursewarLibraryQueryParam param) {
        //请自行添加查询条件
        Page<HeadquartersCultivateTaskCoursewareLibrary> page = this.lambdaQuery()
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(page, HeadquartersCultivateCoursewarVO.class);
    }

    @Override
    public List<HeadquartersCultivateCoursewarVO> getCoursewarsByCultivateId(Long cultivateId) {
        List<HeadquartersCultivateTaskCoursewareLibrary> coursewars = this.baseMapper.selectList(Wrappers
                                                                                                         .<HeadquartersCultivateTaskCoursewareLibrary>lambdaQuery()
                                                                                                         .eq(HeadquartersCultivateTaskCoursewareLibrary::getHeadquartersCultivateTaskId, cultivateId));
        if (CollectionUtils.isNotEmpty(coursewars)) {
            return coursewars.stream().map(c -> {
                HeadquartersCultivateCoursewarVO vo = ObjectUtils.copying(c, HeadquartersCultivateCoursewarVO.class);
                //查询附件
                if (StringUtils.isNotEmpty(vo.getFileIds())) {
                    List<Long> collect = Arrays.stream(vo.getFileIds().split(",")).map(Long::parseLong).collect(Collectors.toList());
                    List<FileVO> files = fileClient.getByIds(collect);
                    vo.setFiles(files);
                }
                return vo;
            }).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }
}