package com.guhecloud.cube.srv.gen.reportmanagement.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.reportmanagement.server.entity.WorkPlanType;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.WorkPlanTypeParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.param.WorkPlanTypeQueryParam;
import com.guhecloud.cube.srv.gen.reportmanagement.server.vo.WorkPlanTypeVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

/**
 * 描述：工作安排类型服务层
 *
 * @author Appet
 * @date 2024-01-04 10:08:36
 */
public interface WorkPlanTypeService extends IService<WorkPlanType> {

    Long add(WorkPlanTypeParam param);

    void update(Long id, WorkPlanTypeParam param);

    void delete(Long id);

    void batchDelete(List<Long> ids);

    WorkPlanTypeVO getById(Long id);

    Pagination<WorkPlanTypeVO> getPage(WorkPlanTypeQueryParam param);

    List<WorkPlanTypeVO> getList(WorkPlanTypeQueryParam param);

    void updateStatus(Long id, Boolean status);
}