package com.guhecloud.cube.srv.gen.medical.cleanliness.server.rule.task.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RuleIssuedIotDTO {

    private Long id;

    /**
     * 时间戳
     */
    private Long timestamp;

    /**
     * 租户编号
     */
    private Integer tenant;
}
