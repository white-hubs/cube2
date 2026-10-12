package com.guhecloud.cube.srv.gen.disinfection.server.member.param;

import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MemberDevicePropDTO;
import lombok.Data;

import java.util.List;

@Data
public class DevicePropControlCommandParam {
    /**
     * 设备id
     */
    private Long deviceId;
    /**
     * 设备名
     */
    private String deviceName;
    /**
     * 属性控制集合
     */
    private List<MemberDevicePropDTO> propWriteValueList;
}
