package com.guhecloud.cube.srv.gen.personnel.management.server.event.message;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class IncreaseStaffMessage {
    private Long employeeId;
    private String name;
    private String mobile;
    private Long organizationId;
    private LocalDateTime inductionDate;
    private LocalDateTime paySocialInsuranceFirstDate;
    private String status;
    private Integer tenant;
    private String idCard;
    /**
     * 短号
     */
    private String cornet;
    private String gender;
    public static final String TOPIC = "INCREASE_STAFF";
}
