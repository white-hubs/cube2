package com.guhecloud.cube.base.flow.flowable.process.application.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author wql
 * @describe 所有工单列表查询条件
 * @date 2024/1/25 10:21
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AllFlowOrderPageQuery extends Paging {
    /**
     * 业务类型(多个 , 分隔)
     */
    private String businessTypes;
    /**
     * 状态（FINISHED：完成(包含通过，拒绝，撤销)，ACTIVE：处理中 ）
     */
    private String status;
    /**
     * 应用编码(多个 , 分隔)
     */
    private String applicationCodes;
}
