package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.ProjectPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert.ProjectUpsertParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.ProjectInfoVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.ProjectAreaCountVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page.ProjectPageVO;
import com.guhecloud.platform.base.model.Pagination;

import jakarta.validation.Valid;

import java.util.List;

public interface BusinessDisinfectionProjectService {
    /**
     * 插入项目
     *
     * @param param 项目参数
     *
     * @return 是否成功
     */
    boolean insertProject(@Valid ProjectUpsertParam param);

    /**
     * 删除项目
     *
     * @param id 项目ID
     *
     * @return 是否成功
     */
    boolean delProject(long id);

    /**
     * @param id    项目ID
     * @param param 项目参数
     *
     * @return 是否成功
     */
    boolean updateProject(long id, @Valid ProjectUpsertParam param);

    /**
     * 分页
     *
     * @param param 分页参数
     *
     * @return 分页结果
     */
    Pagination<ProjectPageVO> pageProject(ProjectPageParam param);

    /**
     * 详情
     *
     * @param productId 项目ID
     *
     * @return 详情
     */
    ProjectInfoVO detailProject(long productId);

    /**
     * 获取项目区域个数信息列表
     */
    List<ProjectAreaCountVO> projectAreaCount();

    /**
     * 全量查询
     *
     * @param param
     *
     * @return
     */
    List<ProjectPageVO> listAll(ProjectPageParam param);
}
