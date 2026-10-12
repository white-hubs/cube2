package com.guhecloud.cube.srv.gen.property.management.server.property.service.depository;

import com.guhecloud.cube.property.management.client.property.param.property.PropertyCheckPlanPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyCheckPlanParam;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyCheckPlanPageVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyCheckTaskEvolveVO;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyCheckTaskVO;
import com.guhecloud.platform.base.model.Pagination;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 盘点计划服务接口
 *
 * @author : lijie
 * @date : 2023-3-17
 */
public interface PropertyCheckPlanService {

    /**
     * 通过ID查询盘点计划详情
     *
     * @param id
     *
     * @return 盘点计划详情
     */
    PropertyCheckTaskVO queryById(Long id);

    /**
     * 查询盘点计划统计
     *
     * @param id
     *
     * @return 查询盘点计划统计
     */
    PropertyCheckTaskEvolveVO queryByPalan(Long id);

    /**
     * 分页查询盘点计划
     *
     * @param param 筛选条件
     *
     * @return 盘点计划信息
     */
    Pagination<PropertyCheckPlanPageVO> getPropertyCheckPlanPage(PropertyCheckPlanPageParam param);

    /**
     * 新增盘点计划
     *
     * @param param 盘点计划数据
     *
     * @return 新增盘点计划结果
     */
    Boolean insert(PropertyCheckPlanParam param);

    /**
     * 通过id删除数据
     *
     * @param id id
     *
     * @return 删除结果
     */
    Boolean deleteById(Long id);

    /**
     * 提交盘点计划、任务
     *
     * @param planId 计划id
     * @param taskId 计划id
     *
     * @return 提交结果
     */
    Boolean submit(Long planId, Long taskId);

    /**
     * 同步盘赢资产到清单
     *
     * @param planId 计划id
     * @param taskId 计划id
     *
     * @return 同步结果
     */
    Boolean submitWin(Long planId, Long taskId);

    /**
     * 资产盘点导出
     *
     * @param ids 盘点计划id
     */
    void getExcel(HttpServletResponse response, String ids);
}
