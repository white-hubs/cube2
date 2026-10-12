package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.querygateway.query;

import com.guhecloud.cube.srv.gen.personnel.management.client.param.StaffQuery;
import lombok.Data;

@Data
public class ArrangeAndDutyWorkQueryParam extends StaffQuery {

    private Long groupId;

    // 考勤组类型
    private String groupType;

    private Integer pageNumber = 1;

    private Integer pageSize = 10;

}
