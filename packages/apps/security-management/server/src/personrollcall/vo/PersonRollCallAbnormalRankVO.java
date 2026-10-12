package com.guhecloud.cube.srv.gen.securitymanagement.server.personrollcall.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonRollCallAbnormalRankVO {

    /**
     * 未到
     */
    private Long unattendedCount;
    /**
     * 特殊人数
     */
    private Long specialCount;
    /**
     * 已点到人数
     */
    private Long attendedCount;
    /**
     * 应到
     */
    private Long expectedCount;

    /**
     * 场所名称
     */
    private String locationName;
}
