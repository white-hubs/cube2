package com.guhecloud.cube.srv.gen.official.management.server.dao.dto;

import lombok.Data;

@Data
public class FocusWorkUpdateStatDTO {
    //部门ID
    private Long deptId;
    //部门名称
    private String deptName;
    //事项ID
    private Long focusWorkId;
    private String focusWorkName;
    private Long workCategoryId;
    private String workCategoryName;
    //申请次数
    private int applyNum;
    //终止次数
    private int stopNum;

    //延迟次数
    private int delayNum;
}
