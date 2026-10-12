package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.vo;

import lombok.Data;

@Data
public class WholesalerVo {
    private Long id;
    //批发商id
    private Long wholesalerId;
    //批发商
    private String wholesalerName;
    //批发商编码
    private String code;
    //批发商档主
    private String shiftMaster;
    //档主电话
    private String shiftMasterPhone;
    //档主身证
    private String shiftMasterCard;
    //其他联系
    private String otherContacts;
    //是否关联=0未关联
    private String association;
    //档位
    private String gearPositionName;
    //档位区域
    private String gearPositionArea;
    //计费时间
    private String billableTime;
}
