package com.guhecloud.cube.srv.gen.adapter.access.control.server.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DoorControlResultVo {
    //门禁点唯一标识
    private String doorIndexCode;
    //是否成功标识
    private String controlResultCode;
    //描述
    private String controlResultDesc;
}
