package com.guhecloud.cube.srv.gen.official.management.server.dao.dto;

import lombok.Data;

@Data
public class FocusWorkStatDTO {
    //申请人
    private Long applicantId;
    //申请人姓名
    private String applicantName;
    //部门ID
    private Long deptId;
    //部门名称
    private String deptName;
    //事项类型ID
    private Long workCategoryId;
    private String workCategoryName;
    //申请次数
    private int applyNum;
    //通过次数
    private int passNum;
}
