package com.guhecloud.cube.srv.gen.securitymanagement.server.electrical.vo;

import lombok.Data;

import java.util.List;

@Data
public class GroupAttributiveInfoVo {
    private Long id;
    /**
     * 组名称
     */
    private String name;
    private List<GroupAttributiveDetailsVo> groupAttributiveDetailsVos;
}
