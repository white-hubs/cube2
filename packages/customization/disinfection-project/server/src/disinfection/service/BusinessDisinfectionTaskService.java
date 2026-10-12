package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.insert.TaskCheckInParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.insert.TaskCompletionRecordsParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.TaskPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.query.TaskQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert.TaskUpsertParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.ProjectStatisticsBaseVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.DisinfectionTaskStatusVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.TaskCompletionRecordVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.TaskInfoVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page.TaskPageVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface BusinessDisinfectionTaskService {

    /**
     * 获取项目下的任务
     *
     * @return 任务列表
     */
    List<TaskInfoVO> listAll(TaskQueryParam param);

    /**
     * 创建任务
     *
     * @param param 任务参数
     *
     * @return 是否成功
     */
    boolean insertTask(TaskUpsertParam param);

    /**
     * 更新任务
     *
     * @param id    任务ID
     * @param param 任务参数
     *
     * @return 是否成功
     */
    boolean updateTask(long id, TaskUpsertParam param);

    /**
     * 删除任务
     *
     * @param id 任务ID
     *
     * @return 是否删除成功
     */
    boolean deleteTask(long id);

    /**
     * 分页获取任务列表
     *
     * @param pageParam 分页参数
     *
     * @return 任务列表
     */
    Pagination<TaskPageVO> pageTasks(TaskPageParam pageParam);

    /**
     * 任务状态分类个数
     **/
    List<ProjectStatisticsBaseVO> countTasksByStatus();

    /**
     * 按照状态获取任务列表
     **/
    DisinfectionTaskStatusVO overviewTasksByStatus(TaskQueryParam queryParam);

    /**
     * 终止任务
     */
    boolean stopTask(long taskId);

    /**
     * 任务打卡
     */
    boolean checkIn(long taskId, TaskCheckInParam param);

    /**
     * 接受任务
     */
    boolean acceptTask(long taskId);

    /**
     * 上传任务完成记录
     */
    boolean uploadCompletionRecords(long taskId, TaskCompletionRecordsParam param);

    /**
     * 获取任务完成记录
     */
    List<TaskCompletionRecordVO> getTaskCompletionRecords(long taskId);

    TaskInfoVO detail(long taskId);

    /**
     * 转移任务
     */
    boolean transferTask(long taskId, long executorId);
}
