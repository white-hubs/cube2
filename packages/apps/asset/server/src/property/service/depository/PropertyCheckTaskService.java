package com.guhecloud.cube.srv.gen.property.management.server.property.service.depository;

import com.guhecloud.cube.property.management.client.property.param.property.CheckTaskRelationParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyCheckPlanPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyCheckTaskParam;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyCheckTaskPageVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedExcelVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedPageVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyDetailedVO;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyCheckPlan;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface PropertyCheckTaskService {
    /**
     * 盘点计划-盘点明细
     *
     * @param param 盘点计划查询条件
     *
     * @return 盘点资产明细
     */
    Pagination<PropertyDetailedVO> queryByPlanIdPage(PropertyCheckPlanPageParam param);

    /**
     * 盘点计划-盘点明细
     *
     * @param id 计划id
     *
     * @return 盘点资产明细
     */
    List<PropertyDetailedExcelVO> queryByPlanIdPage(String id);

    /**
     * 盘点任务列表(app)
     *
     * @param param 盘点任务查询条件
     *
     * @return 盘点任务信息
     */
    Pagination<PropertyCheckTaskPageVO> queryTaskByUserIdPage(PropertyCheckPlanPageParam param);

    List<PropertyCheckTaskPageVO> queryTaskByPlan(Long planId);

    Pagination<PropertyCheckTaskPageVO> queryTaskByPropertyId(PropertyCheckTaskParam param);

    /**
     * 盘点单详情（app）
     *
     * @param taskId 任务id
     *
     * @return 盘点单详情
     */
    Pagination<PropertyDetailedVO> queryCheckTaskPage(Long pageNumber, Long pageSize, Long taskId, String taskStatus);

    /**
     * 生成盘点任务
     */
    Integer insertList(PropertyCheckPlan plan, List<PropertyDetailedPageVO> params);

    /**
     * 盘点单详情提交
     *
     * @param param 盘点资产信息
     *
     * @return 提交结果
     */
    Boolean submitProperty(CheckTaskRelationParam param);
}
