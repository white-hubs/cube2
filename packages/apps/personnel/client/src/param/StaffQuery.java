package com.guhecloud.cube.srv.gen.personnel.management.client.param;

import com.guhecloud.cube.srv.gen.personnel.management.client.enumeration.ArchiveStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StaffQuery {
    private String keyword;

    private String organizationId;

    private String type;

    /**
     * 档案状态筛选
     */
    private ArchiveStatus archiveStatus;

    private Set<Long> staffIds;

    /**
     * 是否包含离职人员
     */
    private Boolean containLeaveOffice = false;
}
