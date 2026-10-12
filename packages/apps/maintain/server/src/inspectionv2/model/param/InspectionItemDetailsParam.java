package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-06-18  11:33
 * @Description:
 * @Version: 1.0
 */
@Data
public class InspectionItemDetailsParam extends Paging {
    private String targetId;
    private Period period;
}
