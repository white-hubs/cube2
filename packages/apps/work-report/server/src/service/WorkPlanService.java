package com.guhecloud.cube.srv.gen.reportmanagement.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.reportmanagement.server.entity.WorkPlan;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.WorkPlanParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.WorkPlanQueryParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.vo.WorkPlanStatVO;
import com.guhecloud.cube.srv.gen.reportmanagement.server.vo.WorkPlanVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：工作安排服务层
 *
 * @author Appet
 * @date 2024-01-04 10:08:36
 */
public interface WorkPlanService extends IService<WorkPlan> {

    Long add(WorkPlanParam param);

    void update(Long id, WorkPlanParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    WorkPlanVO getById(Long id);

    Pagination<WorkPlanVO> getPage(WorkPlanQueryParam param);

    List<WorkPlanVO> getList(WorkPlanQueryParam param);

    List<WorkPlanStatVO> getWorkPlanStatByPerson(WorkPlanQueryParam param);

    List<WorkPlanStatVO> getWorkPlanStatByDept(WorkPlanQueryParam param);
}