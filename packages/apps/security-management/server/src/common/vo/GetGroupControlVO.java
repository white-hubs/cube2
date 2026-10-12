package com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo;

import lombok.Data;

@Data
public class GetGroupControlVO {
    /**
     * 群控组总数
     */
    private Integer groupTotal;
    /**
     * 控制回路总数
     */
    private Long deviceTotal;
    /**
     * 开启总数
     */
    private Integer openTotal;
    /**
     * 关闭总数
     */
    private Integer closeTotal;
    /**
     * 总的回路开关总数
     */
    private Integer total;

    private String name;

}
