package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import java.util.List;

@Data
public class ArrangeWorkQueryParam extends Paging {

    private String keyword;

    private Long groupId;

    private String month;

    private Boolean isCurrentUser;

    private Period period;

    private Long organizationId;

    private Long staffId;

    private Long currentUserId;

    private List<Long> userIds;
}
