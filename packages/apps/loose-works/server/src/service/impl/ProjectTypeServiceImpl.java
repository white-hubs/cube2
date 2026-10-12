package com.guhecloud.cube.srv.gen.loose.works.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.loose.works.dao.ProjectTypeMapper;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.ProjectTypeParam;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectTypeVO;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectTypePO;
import com.guhecloud.cube.srv.gen.loose.works.service.ProjectTypeService;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.util.PagingUtils;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author 15561
 * @description 针对表【project_type(工程类型)】的数据库操作Service实现
 * @createDate 2024-11-08 16:25:44
 */
@Service
public class ProjectTypeServiceImpl extends ServiceImpl<ProjectTypeMapper, ProjectTypePO> implements ProjectTypeService {

    @Override
    public Pagination<ProjectTypeVO> getProjectTypePage(ProjectTypeParam param) {
        Page<ProjectTypePO> projectTypePOPage = this.lambdaQuery()
                .eq(StringUtils.isNotBlank(param.getName()), ProjectTypePO::getName, param.getName())
                .page(PagingUtils.convert(param));
        return PagingUtils.convert(projectTypePOPage, ProjectTypeVO.class);
    }

    @Override
    public boolean saveOrUpdateProjectType(ProjectTypeVO projectTypeVO) {
        if (Objects.nonNull(projectTypeVO.getId())) {
            ProjectTypePO projectTypePO = this.getById(projectTypeVO.getId());
            Assert.isNotFound(Objects.isNull(projectTypePO), "工程类型不存在");
            projectTypePO.setName(projectTypeVO.getName());
            return this.updateById(projectTypePO);
        } else {
            ProjectTypePO projectTypePO = new ProjectTypePO();
            projectTypePO.setName(projectTypeVO.getName());
            projectTypePO.setId(IdWorker.getId());
            return this.save(projectTypePO);
        }
    }
}




