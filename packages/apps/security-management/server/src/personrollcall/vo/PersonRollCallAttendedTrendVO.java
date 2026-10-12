package com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonRollCallAttendedTrendVO {

    /**
     * 日期 MM-DD
     */
    private String date;

    /**
     * 实到人数
     */
    private Long attendedCount;
}
