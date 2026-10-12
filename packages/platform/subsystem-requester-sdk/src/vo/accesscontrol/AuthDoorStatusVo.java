package com.guhecloud.subsystem.requester.sdk.vo.accesscontrol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthDoorStatusVo {
    private String doorIndexCode;
    private Integer doorState;
}
