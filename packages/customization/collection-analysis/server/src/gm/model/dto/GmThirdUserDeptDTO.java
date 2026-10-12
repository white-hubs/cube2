package com.guhecloud.cube.biz.collection.analysis.gm.model.dto;

import lombok.Data;

/**
 * 光明三方用户所属部门
 */
@Data
public class GmThirdUserDeptDTO {
    private Long deptId;
    private Long parentId;
    private String deptName;
    private Integer orderNum;
    private String leader;
    private String status;
    private String delFlag;
}
