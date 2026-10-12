package com.guhecloud.cube.customized.youxianqu.park.management.server.project.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 项目管理条件入参对象
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectManagementQueryParam extends Paging {
    /**
     * 是否立项
     * 0.未立项,1.已立项
     */
    private Boolean foundStatus;

    private Period period;
}
