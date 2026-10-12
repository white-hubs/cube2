package com.guhecloud.subsystem.requester.sdk.vo.onecard;

import lombok.Data;

@Data
public class CardVo {
    //人员序号
    private String userSerial;
    //userNo
    private String userNo;
    //姓名
    private String userName;
    //	物理卡号
    private String userCard;
    //是否有账户 0无账户 1有账户
    private String userAc;
    //卡号状态 0：正常，1：挂失，2：退卡，3：临时卡，4：开始发卡，5：坏卡
    private Integer cardType;
}
