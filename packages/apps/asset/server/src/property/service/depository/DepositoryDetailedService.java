package com.guhecloud.cube.srv.gen.property.management.server.property.service.depository;

import com.guhecloud.cube.property.management.client.common.param.WorkflowRelevance;
import com.guhecloud.cube.property.management.client.property.param.property.DepositoryDetailedPageParam;
import com.guhecloud.cube.property.management.client.property.param.property.DepositoryDetailedParam;
import com.guhecloud.cube.property.management.client.property.vo.property.DepositoryDetailedPageVO;
import com.guhecloud.platform.base.model.Pagination;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface DepositoryDetailedService {
    /**
     * 通过ID查询入库详情
     *
     * @param id
     *
     * @return 入库详情
     */
    DepositoryDetailedPageVO queryById(Long id);

    Pagination<DepositoryDetailedPageVO> getDepositoryDetailedPage(DepositoryDetailedPageParam depositoryDetailed);

    /**
     * 资产入库导出数据
     *
     * @param ids 入库id
     */
    void getDepositoryDetailedExcel(HttpServletResponse response, HttpServletRequest request, String ids) throws IOException;

    /**
     * 新增-资产入库
     *
     * @param depositoryDetailed 入库信息
     *
     * @return 入库结果
     */
    Long insert(DepositoryDetailedParam depositoryDetailed);

    /**
     * 删除资产入库记录
     *
     * @param id 主键
     *
     * @return 是否成功
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
     * 审核
     *
     * @param param 工作流信息
     *
     * @return 是否成功
     */
    Boolean submit(DepositoryDetailedParam param);
}
