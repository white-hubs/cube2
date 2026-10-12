package com.guhecloud.cube.srv.gen.property.management.server.property.controller.depository;

import com.guhecloud.cube.property.management.client.property.param.property.CheckTaskRelationParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyCheckPlanPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyCheckTaskParam;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyCheckTaskPageVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedVO;
import com.guhecloud.cube.srv.gen.property.management.server.property.service.depository.PropertyCheckTaskService;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 盘点任务控制层
 *
 * @author : lijie
 * @date : 2023-4-03
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/property/check/task")
public class PropertyCheckTaskController {
    private final PropertyCheckTaskService taskService;

    /**
     * 盘点计划-盘点明细(web)
     *
     * @param param 盘点计划查询条件
     *
     * @return 盘点资产明细
     */
    @GetMapping("/queryByPlanIdPage")
    public Pagination<PropertyDetailedVO> queryByPlanIdPage(PropertyCheckPlanPageParam param) {
        return taskService.queryByPlanIdPage(param);
    }

    /**
     * 盘点任务列表(app)
     *
     * @param param 盘点任务查询条件
     *
     * @return 盘点任务信息
     */
    @GetMapping("/queryTaskByUserIdPage")
    public Pagination<PropertyCheckTaskPageVO> queryTaskByUserIdPage(@Valid PropertyCheckPlanPageParam param) {
        return taskService.queryTaskByUserIdPage(param);
    }

    /**
     * 盘点任务
     *
     * @param planId 盘点计划id
     *
     * @return 盘点任务信息
     */
    @GetMapping("/queryTaskByPlan")
    public List<PropertyCheckTaskPageVO> queryTaskByPlan(@RequestParam("planId") Long planId) {
        return taskService.queryTaskByPlan(planId);
    }

    /**
     * 盘点单详情（app）
     *
     * @param taskId 任务id
     *
     * @return 盘点单详情
     */
    @GetMapping("/queryCheckTaskByTaskIdPage")
    public Pagination<PropertyDetailedVO> queryCheckTaskPage(Long pageNumber, Long pageSize, Long taskId, String taskStatus) {
        return taskService.queryCheckTaskPage(pageNumber, pageSize, taskId, taskStatus);
    }

    /**
     * 盘点单详情提交
     *
     * @param param 盘点资产信息
     *
     * @return 提交结果
     */
    @PostMapping("/submitProperty")
    public Boolean submitProperty(@RequestBody @Valid CheckTaskRelationParam param) {
        return taskService.submitProperty(param);
    }

    /**
     * 资产盘点任务列表
     *
     * @author: lijie
     * @date: 2024/2/3 14:26
     * @param: 查询条件
     * @return: 资产信息
     **/
    @GetMapping("/queryTaskByPropertyIdPage")
    public Pagination<PropertyCheckTaskPageVO> queryTaskByPropertyId(PropertyCheckTaskParam param) {
        return taskService.queryTaskByPropertyId(param);
    }
}
