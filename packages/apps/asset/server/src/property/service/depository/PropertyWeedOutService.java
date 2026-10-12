package com.guhecloud.cube.srv.gen.property.management.server.property.service.depository;

import com.guhecloud.cube.property.management.client.common.param.WorkflowRelevance;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyWeedOutPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.PropertyWeedOutParam;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyWeedOutPageVO;
import com.guhecloud.platform.base.model.Pagination;
import instance.event.ProcessCompletedMessage;

/**
 * 清理报废服务接口
 *
 * @author : lijie
 * @date : 2023-3-17
 */
public interface PropertyWeedOutService {
    /**
     * 通过ID查询详情
     *
     * @param id 报废记录id
     *
     * @return 报废详情
     */
    PropertyWeedOutPageVO queryById(Long id);

    /**
     * 分页查询
     *
     * @param param 筛选条件
     *
     * @return 查询结果
     */
    Pagination<PropertyWeedOutPageVO> getPropertyWeedOutPage(PropertyWeedOutPageParam param);

    /**
     * 新增
     *
     * @param propertyWeedOut
     *
     * @return 新增结果
     */
    Long insert(PropertyWeedOutParam propertyWeedOut);

    /**
     * 通过id删除数据
     *
     * @param id id
     *
     * @return 删除结果
     */
    Boolean deleteById(Long id);

    /**
     * 插入工作流id
     *
     * @param param 工作流信息
     *
     * @return 是否成功
     */
    Boolean update(WorkflowRelevance param);

    /**
     * 审批
     *
     * @param message 审批信息
     *
     * @return 是否成功
     */
    Boolean submit(ProcessCompletedMessage message);
}
