package com.guhecloud.cube.srv.gen.securitymanagement.server.information.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class ProgramPageParam extends Paging {
    /**
     * 节目名称
     */
    private String programName;
    /**
     * 节目分类名称
     */
    private String categoryName;

}
