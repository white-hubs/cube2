package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectPlanCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectResultCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.PlanAddParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectPlanPO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InspectPlanCommandService extends IService<InspectPlanPO> {

    Long save(InspectPlanCommandModel commandModel);

    @Transactional
    Long addPlan(PlanAddParam param);

    void update(InspectResultCommandModel commandModel);

    void remove(Long id);

    void removeBatch(String ids);

    void removeAll(String ids);

    /**
     * 开启xxl任务
     */
    void startPlan(Long id);

    /**
     * 开启xxl任务
     */
    void startPlan(InspectPlanPO po);

    /**
     * 停止xxl任务
     */
    void stopPlan(Long id);

    /**
     * 结束xxl任务
     */
    void overPlan(Long id);

    /**
     * 批量停止计划
     */
    void overPlanBatch(List<Long> planIdList);
}
