package com.guhecloud.cube.srv.gen.securitymanagement.server.keypersonnel.vo;

import lombok.Data;

@Data
public class KeyPersonOptionVO {
    /**
     * 重点人员主键。
     */
    private Long id;

    /**
     * 重点人员姓名。
     */
    private String name;

    /**
     * 重点人员编号。
     */
    private String personCode;

    /**
     * mdata 区域主键。
     */
    private Long areaId;

    /**
     * 区域名称快照。
     */
    private String areaName;

    /**
     * mdata 区域 sequence。
     */
    private String areaSequence;
}
