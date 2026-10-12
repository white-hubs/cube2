package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.controller;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.insert.TaskCheckInParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.insert.TaskCompletionRecordsParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page.TaskPageParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.query.TaskQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert.TaskUpsertParam;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.service.BusinessDisinfectionTaskService;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.detail.ProjectStatisticsBaseVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.DisinfectionTaskStatusVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.TaskCompletionRecordVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list.TaskInfoVO;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page.TaskPageVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 消杀任务接口
 */
@RestController
@RequestMapping("/disinfection/tasks")
@RequiredArgsConstructor
@Slf4j
public class BusinessDisinfectionTaskController {
    private final BusinessDisinfectionTaskService businessTaskService;

    /**
     * 根据条件获取全部列表
     */
    @GetMapping("/list-all")
    public List<TaskInfoVO> listAll(TaskQueryParam param) {

        return businessTaskService.listAll(param);

    }

    /**
     * 新增任务
     */
    @PostMapping
    public Boolean insertTask(@RequestBody TaskUpsertParam param) {
        return businessTaskService.insertTask(param);
    }

    /**
     * 编辑任务
     */
    @PutMapping("{id}")
    public Boolean updateTask(@PathVariable Long id, @RequestBody TaskUpsertParam param) {
        return businessTaskService.updateTask(id, param);
    }

    /**
     * 删除任务
     */
    @DeleteMapping("/{id}")
    public Boolean deleteTask(@PathVariable long id) {
        return businessTaskService.deleteTask(id);
    }

    /**
     * 获取任务列表
     */
    @GetMapping
    public Pagination<TaskPageVO> listTasks(TaskPageParam pageParam) {

        return businessTaskService.pageTasks(pageParam);
    }

    /**
     * 任务状态分类个数
     */
    @GetMapping("/status-count")
    public List<ProjectStatisticsBaseVO> countTasksByStatus() {
        return businessTaskService.countTasksByStatus();
    }

    /**
     * 任务状态分类总览
     */
    @GetMapping("/status-overview")
    public DisinfectionTaskStatusVO overviewTasksByStatus(TaskQueryParam param) {
        return businessTaskService.overviewTasksByStatus(param);
    }

    /**
     * 任务终止
     */

    @PutMapping("/{taskId}/stop")
    public Boolean stopTask(@PathVariable long taskId) {
        return businessTaskService.stopTask(taskId);
    }

    /**
     * 任务接收
     */
    @PutMapping("/{taskId}/accept")
    public Boolean acceptTask(@PathVariable long taskId) {
        return businessTaskService.acceptTask(taskId);
    }

    /**
     * 任务打卡
     */
    @PutMapping("/{taskId}/check-in")
    public Boolean checkIn(@PathVariable long taskId, @RequestBody @Valid TaskCheckInParam param) {
        return businessTaskService.checkIn(taskId, param);
    }

    /**
     * 获取任务详情
     */
    @GetMapping("/{taskId}")
    public TaskInfoVO getTask(@PathVariable long taskId) {
        return businessTaskService.detail(taskId);
    }

    /**
     * 任务移交
     */
    @PutMapping("/{taskId}/{executorId}/transfer")
    public Boolean transferTask(@PathVariable long taskId, @PathVariable long executorId) {
        return businessTaskService.transferTask(taskId, executorId);
    }

    /**
     * 上传任务完成情况
     */
    @PostMapping("/{taskId}/upload-completion-records")
    public Boolean uploadCompletionRecords(@PathVariable long taskId,
                                           @RequestBody @Valid TaskCompletionRecordsParam param
                                          ) {
        return businessTaskService.uploadCompletionRecords(taskId, param);
    }

    /**
     * 获取任务完成情况
     */
    @GetMapping("/{taskId}/completion-records")
    public List<TaskCompletionRecordVO> getTaskCompletionRecords(@PathVariable long taskId) {
        return businessTaskService.getTaskCompletionRecords(taskId);
    }

}
