package com.guhecloud.cube.srv.gen.official.management.server.interfaces.vo;

import lombok.Data;

@Data
public class CmmFlowStatVO {
    //部门ID
    private Long deptId;
    //部门
    private String deptName;
    //申请次数
    private int applyNum;
    //通过次数
    private int passNum;
}
