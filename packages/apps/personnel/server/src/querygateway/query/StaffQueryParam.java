package com.guhecloud.cube.srv.gen.personnel.management.server.querygateway.query;

import com.guhecloud.cube.srv.gen.personnel.management.server.enumeration.ArchiveStatus;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import java.util.Set;

@Data
public class StaffQueryParam extends Paging {

    private String keyword;

    private String organizationId;

    private String type;

    private Set<Long> staffIds;
    /**
     * 档案状态筛选
     */
    private ArchiveStatus archiveStatus;

    private Period period;

    /**
     * mysql日期格式
     */
    private String dateExp;

    private String timeType;

    /**
     * 是否包含离职人员
     */
    private Boolean containLeaveOffice;

    private Long postId;

    /**
     * 是否在岗
     */
    private Boolean onDuty;
    private String status;

}
