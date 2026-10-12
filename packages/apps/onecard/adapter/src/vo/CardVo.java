package com.guhecloud.cube.srv.gen.adapter.onecard.server.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CardVo {
    //人员序号
    @JsonProperty(value = "userSerial")
    private String userSerial;
    //userNo
    @JsonProperty(value = "userNo")
    private String userNo;
    //姓名
    @JsonProperty(value = "userLname")
    private String userName;
    //	物理卡号
    @JsonProperty(value = "userCard")
    private String userCard;
    //是否有账户 0无账户 1有账户
    @JsonProperty(value = "userAc")
    private String userAc;
    //卡号状态 0：正常，1：挂失，2：退卡，3：临时卡，4：开始发卡，5：坏卡
    @JsonProperty(value = "cardType")
    private Integer cardType;
}
