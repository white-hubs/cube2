package com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo;

import lombok.Data;

@Data
public class KeyPersonAreaCountVO {
    /**
     * mdata 区域主键。
     */
    private Long areaId;

    /**
     * 区域名称快照。
     */
    private String areaName;

    /**
     * 统计数量。
     */
    private Long count;
}
