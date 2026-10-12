package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.TaskSplitParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectPlanPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.PlanRelationPO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InspectTaskCommandService extends IService<InspectTaskPO> {

    @Transactional
    void splitTask(TaskSplitParam taskSplitParam);

    @Deprecated
    String createTaskByPlan(InspectPlanPO planPO);

    String createTaskByPlan(InspectPlanPO planPO, List<PlanRelationPO> targetDTOList);

    void saveByPlanPO(long planId);

    void saveByPlanPO(InspectPlanPO planPO);

    void stopById(Long id);

    /**
     * 巡检任务过期
     */
    void expire(InspectTaskPO po);

    /**
     * @Author wql
     * @Description 接收任务
     * @Date 10:38 2023/4/20
     **/
    void acceptTask(Long id);

    /**
     * @Author wql
     * @Description 移交任务
     * @Date 10:54 2023/4/20
     **/
    void turnOverTask(Long taskId, Long userId);

    /**
     * @Author wql
     * @Description 添加写同人
     * @Date 10:58 2023/4/20
     **/
    void addSynergyUser(Long taskId, String userId);

    /**
     * 批量删除巡检任务
     *
     * @param taskIdList 巡检任务id
     *
     * @return 是否成功
     */
    Boolean removeBatch(List<Long> taskIdList);
}
