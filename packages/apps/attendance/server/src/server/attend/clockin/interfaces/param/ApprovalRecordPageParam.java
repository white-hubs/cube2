package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 考勤审批记录分页查询参数
 */
@Getter
@Setter
public class ApprovalRecordPageParam extends Paging {
    private String type;
    private String businessType;
    private String status;
    private Long startUserId;
    private String username;
    private Long organizationId;
    private Period submitTime;
    private Period completionTime;
    private String applicationCode;
    private String processInstanceIds;
    private List<String> processInstanceIdsList;
    private String currentTaskName;

    /**
     * 业务开始时间范围
     */
    private Period businessTime;
}
