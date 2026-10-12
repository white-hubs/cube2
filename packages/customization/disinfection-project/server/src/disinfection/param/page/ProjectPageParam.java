package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.page;

import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.DisinfectionProjectStatusEnum;
import com.guhecloud.cube.srv.gen.disinfection.server.disinfection.enumeration.TimeCycleTypeEnum;
import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProjectPageParam extends Paging {
    private DisinfectionProjectStatusEnum status;
    private String keyword;

    /**
     * 项目区域
     */
    private String area;

    /**
     * 项目周期
     */
    private TimeCycleTypeEnum typeEnum;
}
