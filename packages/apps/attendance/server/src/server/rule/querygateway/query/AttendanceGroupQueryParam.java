package com.guhecloud.cube.srv.gen.attendance.management.server.rule.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class AttendanceGroupQueryParam extends Paging {

    private String keyword;

    /**
     * 考勤组类型
     */
    private String type;

    private int version;

}
