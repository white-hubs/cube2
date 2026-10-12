package com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.vo;

import lombok.Data;

@Data
public class PersonRollCallTodayCountVO {
    /**
     * 未到
     */
    private Long unattendedCount;
    /**
     * 应到
     */
    private Long expectedCount;
}
