package com.guhecloud.subsystem.requester.sdk.vo.accesscontrol;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoorControlResultVo {
    //门禁点唯一标识
    private String doorIndexCode;
    //是否成功标识
    private String controlResultCode;
    //描述
    private String controlResultDesc;
}
