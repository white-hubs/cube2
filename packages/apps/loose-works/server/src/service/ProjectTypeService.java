package com.guhecloud.cube.srv.gen.loose.works.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.loose.works.dto.request.ProjectTypeParam;
import com.guhecloud.cube.srv.gen.loose.works.dto.vo.ProjectTypeVO;
import com.guhecloud.cube.srv.gen.loose.works.po.ProjectTypePO;
import com.guhecloud.platform.base.model.Pagination;

/**
 * @author 15561
 * @description 针对表【project_type(工程类型)】的数据库操作Service
 * @createDate 2024-11-08 16:25:44
 */
public interface ProjectTypeService extends IService<ProjectTypePO> {

    /**
     * 获取工程类型列表
     *
     * @param param
     *
     * @return
     */
    Pagination<ProjectTypeVO> getProjectTypePage(ProjectTypeParam param);

    /**
     * 新增或删除工程类型
     *
     * @param projectTypeVO
     */
    boolean saveOrUpdateProjectType(ProjectTypeVO projectTypeVO);
}
