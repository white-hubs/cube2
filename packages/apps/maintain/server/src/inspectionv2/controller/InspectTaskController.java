package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.controller;

import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectTaskCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query.InspectTaskQueryService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectTaskQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.TaskPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.TaskSplitParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.DetailRateResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.StandardResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectTaskVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 巡检任务 前端控制器
 * </p>
 *
 * @author zwb
 * @since 2022-04-26
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectTaskController {

    private final InspectTaskQueryService queryService;
    private final InspectTaskCommandService commandService;

    /**
     * 任务详情
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/inspect-task/{id}")
    public InspectTaskVO getById(@PathVariable Long id) {
        return queryService.getVOById(id);
    }

    /**
     * 巡检计划-详情-对象的巡检标准列表
     * 根据巡检任务和对象id获取巡检标准列表
     * taskId: 任务id Long 必填
     * targetId：对象id Long 必填
     *
     * @return
     */
    @GetMapping("/inspect-task/detail-standard")
    public List<StandardResult> getDeailStandardList(InspectTaskQueryParam param) {
        return queryService.getDeailStandardList(param.getTaskId(), param.getTargetId());
    }

    /**
     * 巡检任务-详情-执行进度
     *
     * @param param 入参
     *              id：任务id，isSplit:是否是拆解任务
     *
     * @return
     */
    @GetMapping("/inspect-task/detail-rate")
    public DetailRateResult getDetailRateResult(TaskPageParam param) {
        return queryService.getDetailRateResult(param.getId());
    }

    /**
     * 任务生成
     */
    @GetMapping("/inspect-task/{planId}/generate")
    public void generate(@PathVariable Long planId) {
        commandService.saveByPlanPO(planId);
    }

    /**
     * 任务拆解
     *
     * @param taskSplitParam
     */
    @PostMapping("/inspect-task/split")
    public void splitTask(@RequestBody TaskSplitParam taskSplitParam) {
        commandService.splitTask(taskSplitParam);
    }

    @GetMapping("/inspect-task/page")
    @DataPermissionSwitch(dataModel = "inspect:inspectTask")
    public Pagination<InspectTaskVO> getPage(InspectTaskQueryParam param) {
        return queryService.getPage(param);
    }

    /**
     * 根据目标对象 获取巡检任务
     */
    @GetMapping("/inspect-task/{targetCategory}/{targetId}/page")
    public Pagination<InspectTaskVO> getPageByTarget(@PathVariable("targetCategory") String targetCategory,
                                                     @PathVariable("targetId") Long targetId,
                                                     InspectTaskQueryParam param
                                                    ) {
        return queryService.getPageByTarget(targetCategory, targetId, param);
    }

    /**
     * 默认查询自己为候选人的任务
     */
    @GetMapping("/inspect-task/mytask")
    @DataPermissionSwitch(dataModel = "inspect:inspectTask")
    public Pagination<InspectTaskVO> getMyTask(InspectTaskQueryParam param) {
        return queryService.getMyTask(param);
    }

    /**
     * @Author wql
     * @Description 接收任务
     * @Date 10:50 2023/4/20
     **/
    @PutMapping("/inspect-task/{id}/accept")
    public void acceptTask(@PathVariable("id") Long id) {
        commandService.acceptTask(id);
    }

    /**
     * @Author wql
     * @Description 移交任务
     * @Date 10:51 2023/4/20
     **/
    @PutMapping("/inspect-task/{id}/turnOver/{userId}")
    public void turnOverTask(@PathVariable("id") Long taskId, @PathVariable("userId") Long userId) {
        commandService.turnOverTask(taskId, userId);
    }

    /**
     * @Author wql
     * @Description 添加协作人
     * @Date 10:57 2023/4/20
     **/
    @PutMapping("/inspect-task/{id}/addCollaborator")
    public void addSynergyUser(@PathVariable("id") Long taskId, String userId) {
        commandService.addSynergyUser(taskId, userId);
    }

    /**
     * 任务停止
     */
    @PutMapping("/inspect-task/{id}/stop")
    public void getMyTask(@PathVariable("id") Long id) {
        commandService.stopById(id);
    }

    /**
     * 批量删除巡检任务
     *
     * @param param taskIdList 巡检任务id集合
     *
     * @return 是否成功
     */
    @PutMapping("/inspect-task/remove-batch")
    public Boolean removeBatch(@RequestBody InspectTaskQueryParam param) {
        return commandService.removeBatch(param.getIdList());
    }

}

