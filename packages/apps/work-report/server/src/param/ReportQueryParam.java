package com.guhecloud.cube.srv.gen.reportmanagement.server.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import java.util.List;

@Data
public class ReportQueryParam extends Paging {

    private String status;

    /**
     * 时间
     */
    private Period period;

    /**
     * 发起人
     */
    private Long startUserId;
    /**
     * 部门id （下拉框）
     */
    private String departmentId;
    /**
     * 部门id （下钻）
     */
    private Long organizationId;
    /**
     * 发起人
     */
    private String userName;
    /**
     * 发起人
     */
    private List<Long> reportByList;

    /**
     * 部门集合
     */
    List<Long> departmentIdList;

    /**
     * 导出勾选的用户id集合
     */
    private String selectReportBys;
}
