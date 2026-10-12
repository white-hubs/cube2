package com.guhecloud.cube.base.visualization.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.base.visualization.server.entity.ProjectManage;
import com.guhecloud.cube.base.visualization.server.param.ProjectManageParam;
import com.guhecloud.cube.base.visualization.server.param.ProjectManageUe5UpdateParam;
import com.guhecloud.cube.base.visualization.server.param.ProjectManageUpdateParam;
import com.guhecloud.cube.base.visualization.server.vo.ProjectManageVO;
import com.guhecloud.cube.base.visualization.server.vo.ProjectModelConfigurationVO;

import java.util.List;

public interface ProjectManageService extends IService<ProjectManage> {
    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     *
     * @return 实例对象
     */
    ProjectManageVO queryById(Long id);

    /**
     * 复制项目
     *
     * @param copyProject
     *
     * @return
     */
    Long copyProject(ProjectManage copyProject);

    /**
     * @description:根据模型查询项目
     * @author: lijie
     * @date: 2023/8/29 14:38
     * @param: modelId 模型id
     * @return: 是否已绑定
     **/
    Boolean queryByModelId(Long modelId);

    /**
     * 查询我的所有项目
     *
     * @return 项目信息
     */
    List<ProjectManageVO> queryProjectAll();

    /**
     * 新增数据
     *
     * @param projectManage 实例对象
     *
     * @return 实例对象
     */
    Boolean insert(ProjectManageParam projectManage);

    /**
     * 更新数据
     *
     * @param projectManage 实例对象
     *
     * @return 实例对象
     */
    Boolean update(ProjectManageUpdateParam projectManage);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    Boolean deleteById(Long id);

    /**
     * 更新ue5配置
     *
     * @param param
     *
     * @return
     */
    Boolean updateBaseConfig(ProjectManageUe5UpdateParam param);

    ProjectModelConfigurationVO getModelConfigurationQueryByProjectId(Long projectId);

    void dataMigration();

}
