package com.guhecloud.cube.srv.gen.property.management.server.property.service.depository;

import com.guhecloud.cube.property.management.client.property.param.property.PropertyHistoryPageParam;
import com.guhecloud.cube.property.management.client.property.vo.property.PropertyHistoryListVO;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyDetailed;
import com.guhecloud.cube.srv.gen.property.management.server.property.entity.depository.PropertyHistory;
import com.guhecloud.cube.srv.gen.property.management.server.property.enumeration.HandleTypeEnum;
import com.guhecloud.platform.base.model.Pagination;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface PropertyHistoryService {
    /**
     * 查询资产履历
     *
     * @param propertyHistory 筛选条件
     *
     * @return 资产履历
     */
    Pagination<PropertyHistoryListVO> getPropertyHistoryList(PropertyHistoryPageParam propertyHistory);

    void getPropertyHistorygetExcel(PropertyHistoryPageParam propertyHistory, HttpServletRequest request, HttpServletResponse response);

    /**
     * 新增履历
     *
     * @param propertyHistory
     *
     * @return 新增结果
     */
    Boolean insert(PropertyHistory propertyHistory);

    Boolean insert(List<PropertyDetailed> propertyDetaileds);

    Boolean insertByIds(List<Long> ids, HandleTypeEnum handleTypeEnum, String remarks);

    /**
     * 资产履历导出
     *
     * @param
     */
    void getExcel();
}
