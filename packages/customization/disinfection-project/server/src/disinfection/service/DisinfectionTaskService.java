package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.entity.DisinfectionTask;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionTaskStatusEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.query.TaskQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.ProjectStatisticsBaseVO;

import java.util.List;

public interface DisinfectionTaskService extends IService<DisinfectionTask> {
    /**
     * 更新任务类型名称
     *
     * @param taskTypeId 任务类型ID
     * @param dataName   新名称
     *
     * @return 是否成功
     */
    boolean updateTaskTypeName(long taskTypeId, String dataName);

    /**
     * 判断任务是否存在
     *
     * @param projectId
     *
     * @return 是否存在 true:存在 false:不存在
     */
    boolean hasTask(long projectId);

    /**
     * 根据项目ID获取任务列表
     *
     * @param projectId 项目ID
     *
     * @return 任务列表
     */
    List<DisinfectionTask> listByProjectId(long projectId);

    /**
     * 判断区域是否有关联未完成的任务
     *
     * @param areaIds 区域ID
     *
     * @return 是否有关联未完成的任务
     */
    boolean existNotCompletedTasksByAreaId(List<Long> areaIds);

    /**
     * 判断分组是否有关联未完成的任务
     *
     * @param groupId 分组ID
     *
     * @return 是否有关联未完成的任务
     */
    boolean existNotCompletedTasksByGroupId(long groupId);

    /**
     * 任务状态个数
     *
     * @return
     */
    List<ProjectStatisticsBaseVO> countTasksByStatus(TaskQueryParam taskQueryParam);

    /**
     * 更新任务状态
     *
     * @param taskId 任务ID
     * @param status 任务状态
     *
     * @return 是否成功
     */
    boolean updateTaskStatus(long taskId, DisinfectionTaskStatusEnum status);

    List<Integer> listAllTenantCodes();

}