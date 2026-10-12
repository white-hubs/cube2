package com.guhecloud.cube.srv.gen.securitymanagement.server.chilledwater.entity;

import com.guhecloud.platform.database.entity.enhanced.BasicEnhancedTenantEntity;
import lombok.Data;

@Data

public class ColdWholesaler extends BasicEnhancedTenantEntity<Long> {
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

}
