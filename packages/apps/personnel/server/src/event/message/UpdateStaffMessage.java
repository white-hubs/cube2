package com.guhecloud.cube.srv.gen.personnel.management.server.event.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStaffMessage {
    public static final String TOPIC = "UPDATE_STAFF_MESSAGE";

    private Long staffId;

    private Long userId;

    private String name;

    private String mobile;

    private Long organizationId;

    // 是否离职
    private boolean leaveOffice;
    /**
     * 员工状态
     */
    private String status;

    /**
     * 租户
     */
    private Integer tenant;

    /**
     * 短号
     */
    private String cornet;

    private String idCard;
}
