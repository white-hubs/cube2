package com.guhecloud.cube.srv.gen.adapter.onecard.server.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountOutVo {
    //人员序号
    private String userSerial;
    //工号
    private String userNo;
    //姓名
    private String userName;
    //人员档案状态
    private Integer userType;
    //账户状态
    private String accountStatus;
    //正常状态的物理卡号
    private String userCard;
}
