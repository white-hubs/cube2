package com.guhecloud.cube.base.videogw.server.common.domain.enumeration;

import lombok.Getter;

@Getter
public enum VideoTransportModeEnum {

    AUTO,//AUTO是底层支持的默认协议或自动协商的协议. 仅对于rtsp等涉及传输层协议选择的协议适用

    UDP,//UDP协议

    TCP,//TCP协议

    MUTLICAST,//MUTLICAST协议


}
