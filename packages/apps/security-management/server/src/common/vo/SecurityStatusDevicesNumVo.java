package com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo;

import lombok.Data;

import java.util.List;

@Data
public class SecurityStatusDevicesNumVo {
    private Integer total;
    private List<SecurityStatusListVo> statusListVos;
}
