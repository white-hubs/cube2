package com.guhecloud.cube.srv.gen.adapter.onecard.server.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountVo {
    //人员序号
    @JsonProperty(value = "userSerial")
    private String userSerial;
    //工号
    @JsonProperty(value = "userNo")
    private String userNo;
    //姓名
    @JsonProperty(value = "userLname")
    private String userName;
    //人员档案状态
    @JsonProperty(value = "userType")
    private Integer userType;
    //账户状态
    @JsonProperty(value = "accountStatus")
    private String accountStatus;
    //正常状态的物理卡号
    @JsonProperty(value = "userCard")
    private String userCard;

}
