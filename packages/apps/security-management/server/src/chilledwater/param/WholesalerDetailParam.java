package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

@Data
public class WholesalerDetailParam extends Paging {
    //部门
    private String departmentId;
    //档位号
    private String gearPositionCode;
    //档主编号
    private String code;
    //档主姓名
    private String shiftMaster;
    //钛泡编号
    private String deviceCode;
    //操作员
    private String operator;
}
