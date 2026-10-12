package com.guhecloud.cube.srv.gen.work.permit.server.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * 作业关联项查询参数。
 */
@Data
public class WorkPermitAssociatedItemQueryParam extends Paging {
    /**
     * 作业类型名称，模糊搜索。
     */
    private String typeName;
    /**
     * 作业等级。
     */
    private String workPermitLevel;
}
