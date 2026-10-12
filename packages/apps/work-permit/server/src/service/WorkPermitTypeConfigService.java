package com.guhecloud.cube.srv.gen.work.permit.server.service;

import com.guhecloud.cube.srv.gen.work.permit.server.param.WorkPermitAssociatedItemQueryParam;
import com.guhecloud.cube.srv.gen.work.permit.server.param.WorkPermitTypeConfigParam;
import com.guhecloud.cube.srv.gen.work.permit.server.param.WorkPermitTypeConfigQueryParam;
import com.guhecloud.cube.srv.gen.work.permit.server.vo.WorkPermitAssociatedItemPageVO;
import com.guhecloud.cube.srv.gen.work.permit.server.vo.WorkPermitTypeConfigDetailVO;
import com.guhecloud.cube.srv.gen.work.permit.server.vo.WorkPermitTypeConfigNameVO;
import com.guhecloud.cube.srv.gen.work.permit.server.vo.WorkPermitTypeConfigPageVO;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

/**
 * 作业类型配置服务。
 */
public interface WorkPermitTypeConfigService {
    /**
     * 新增作业类型配置。
     */
    Long add(WorkPermitTypeConfigParam param);

    /**
     * 修改作业类型配置。
     */
    void update(Long id, WorkPermitTypeConfigParam param);

    /**
     * 删除作业类型配置。
     */
    void delete(Long id);

    /**
     * 获取作业类型配置详情。
     */
    WorkPermitTypeConfigDetailVO getById(Long id);

    /**
     * 分页查询作业类型配置。
     */
    Pagination<WorkPermitTypeConfigPageVO> getPage(WorkPermitTypeConfigQueryParam param);

    /**
     * 查询全量作业等级。
     */
    List<String> listLevels(WorkPermitTypeConfigQueryParam param);

    /**
     * 查询作业名称列表。
     */
    List<WorkPermitTypeConfigNameVO> listNames(WorkPermitTypeConfigQueryParam param);

    /**
     * 分页查询检查项列表。
     */
    Pagination<WorkPermitAssociatedItemPageVO> getRiskCheckItemPage(WorkPermitAssociatedItemQueryParam param);

    /**
     * 查询检查项全量ID列表。
     */
    List<Long> listRiskCheckItemIds(WorkPermitAssociatedItemQueryParam param);

    /**
     * 分页查询验收项列表。
     */
    Pagination<WorkPermitAssociatedItemPageVO> getAcceptanceItemPage(WorkPermitAssociatedItemQueryParam param);

    /**
     * 查询验收项全量ID列表。
     */
    List<Long> listAcceptanceItemIds(WorkPermitAssociatedItemQueryParam param);

    /**
     * 导出作业类型配置。
     */
    void export(WorkPermitTypeConfigQueryParam param, HttpServletResponse response);
}
