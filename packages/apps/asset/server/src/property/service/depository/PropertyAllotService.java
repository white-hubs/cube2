package com.guhecloud.cube.srv.gen.property.management.server.property.service.depository;

import com.guhecloud.cube.property.management.client.common.param.WorkflowRelevance;
import com.guhecloud.cube.property.management.client.property.param.property.AllotPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.AllotParam;
import com.guhecloud.cube.property.management.client.property.param.property.DepositoryDetailedPageExcelParam;
import com.guhecloud.cube.property.management.client.property.vo.property.AllotPageVO;
import com.guhecloud.platform.base.model.Pagination;
import instance.event.ProcessCompletedMessage;

import jakarta.servlet.http.HttpServletResponse;

public interface PropertyAllotService {
    /**
     * 通过ID查询详情
     *
     * @param id
     *
     * @return 派发详情
     */
    AllotPageVO queryById(Long id);

    /**
     * 通过资产id查询使用情况
     */
    AllotPageVO queryByPropertyId(Long propertyId);

    /**
     * 分页查询派发、调拨
     *
     * @param param 筛选条件
     *
     * @return 派发、调拨列表
     */
    Pagination<AllotPageVO> getAllotPage(AllotPageParam param);

    /**
     * 新增派发、调拨
     *
     * @param param 资产信息
     *
     * @return 派发、调拨结果
     */
    Long insert(AllotParam param);

    /**
     * 通过id删除数据
     *
     * @param id 主键
     *
     * @return 是否成功
     */
    Boolean deleteById(Long id);

    void getExcel(HttpServletResponse response, DepositoryDetailedPageExcelParam param);

    /**
     * 插入工作流id
     *
     * @param param 工作流信息
     *
     * @return 是否成功
     */
    Boolean update(WorkflowRelevance param);

    /**
     * 审核
     *
     * @param message 审核信息
     *
     * @return 是否成功
     */
    Boolean submit(ProcessCompletedMessage message);
}
