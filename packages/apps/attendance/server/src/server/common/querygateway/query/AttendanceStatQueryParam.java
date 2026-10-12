package com.guhecloud.cube.srv.gen.attendance.management.server.common.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceStatQueryParam extends Paging {
    private Long organizationId;
    private Long groupId;
    private String month;
    private Long userId;
    private List<Long> userIds;
    /**
     * 日期条件类型（QueryDateTypeEnum）
     */
    private String dateType;
    private Period period;
    private Integer tenant;
    private String type;
    private String staffName;
}
