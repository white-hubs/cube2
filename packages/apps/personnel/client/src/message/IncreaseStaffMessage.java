package com.guhecloud.cube.srv.gen.personnel.management.client.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IncreaseStaffMessage {
    public static final String TOPIC = "INCREASE_STAFF";
    private Long employeeId;
    private String name;
    private String mobile;
    private Long organizationId;
    private LocalDateTime inductionDate;
    private LocalDateTime paySocialInsuranceFirstDate;
    /**
     * 员工状态。
     */
    private String status;
    /**
     * 租户。
     */
    private Integer tenant;
    /**
     * 身份证号。
     */
    private String idCard;
    /**
     * 短号。
     */
    private String cornet;
    /**
     * 性别。
     */
    private String gender;
}
