package com.guhecloud.cube.srv.gen.disinfection.server.member.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DistrictDeviceCountVO {
    /**
     * 区县名称
     */
    private String district;
    /**
     * 绑定设备数量
     */
    private Long count;
}

