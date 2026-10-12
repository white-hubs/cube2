package com.guhecloud.cube.biz.collection.analysis.gm.model.dto;

import lombok.Data;

/**
 * 光明三方用户
 */
@Data
public class GmThirdUserDTO {
    private Long userId;
    private Long deptId;
    private String userNo;
    private String userName;
    private String nickName;
    private String phonenumber;
    private String sex;
    private String status;
    private String delFlag;
    /**
     * 人员负责人字段。若三方实际放在 dept.leader，服务层会兼容读取。
     */
    private String leader;
    private GmThirdUserDeptDTO dept;
}
