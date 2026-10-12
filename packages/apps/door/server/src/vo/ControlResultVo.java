package com.guhecloud.cube.biz.door.server.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ControlResultVo {
    //门禁点唯一标识
    private String doorIndexCode;
    //是否成功标识
    private String controlResultCode;
    //描述
    private String controlResultDesc;
}
