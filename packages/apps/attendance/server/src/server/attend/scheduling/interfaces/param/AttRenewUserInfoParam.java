package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 更新考勤人员信息入参
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AttRenewUserInfoParam {
    private Long id;
    private String name;
    private Long photographKey;
    private Integer tenant;
}
