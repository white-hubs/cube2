package com.guhecloud.cube.srv.gen.attendance.management.server.stat.vo.req;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class AttendanceStatParam extends Paging {
    private Long id;
    private String keyword;
    private Long groupId;
    private String month;
    private Long organizationId;
}
