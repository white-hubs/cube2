package com.guhecloud.cube.biz.todo.client;

import com.guhecloud.cube.biz.todo.client.param.TodoTagReferBusinessTypeModifyParam;
import com.guhecloud.cube.biz.todo.client.param.TodoTaskNumberStatisticsParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Set;

@FeignClient(name = "todoCenter")
public interface TodoTaskClient {

    /**
     * 根据业务主键批量删除待办任务
     *
     * @param businessIds 业务主键集合
     */
    @DeleteMapping("/todo-task/business/batch")
    void removeBatchByBusinessIds(@RequestBody Set<String> businessIds);

    /**
     * 待办任务标签关联业务类型
     *
     * @param todoTagCode
     * @param param
     *
     * @return
     */
    @PostMapping("/todo-tag/{todoTagCode}/refer/business-type")
    Long saveTodoTagReferBusinessType(@PathVariable("todoTagCode") String todoTagCode,
                                      @RequestBody TodoTagReferBusinessTypeModifyParam param
                                     );

    /**
     * 待办任务标签删除
     *
     * @param id
     */
    @DeleteMapping("/todo-tag/{id}")
    void removeById(@PathVariable("id") Long id);

    /**
     * 条件统计任务数量
     *
     * @param param 查询参数
     *
     * @return 任务数量
     */
    @PostMapping("/todo-task-tag/number-statistics")
    Long todoTaskNumberStatistics(@RequestBody TodoTaskNumberStatisticsParam param);
}
