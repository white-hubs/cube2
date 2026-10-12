package com.guhecloud.cube.srv.gen.official.management.server.dao.dto;

import lombok.Data;

@Data
public class CmmFlowStatDTO {
    //部门ID
    private Long deptId;
    //部门
    private String deptName;
    //申请次数
    private int applyNum;
    //通过次数
    private int passNum;
}
