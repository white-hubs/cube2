package com.guhecloud.cube.srv.gen.disinfection.server.member.param;

import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MemberDeviceMethodDTO;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class DeviceMethodControlCommandParam {
    /**
     * 设备id
     */
    @NotNull(message = "设备id不能为空")
    private Long deviceId;
    /**
     * 设备名
     */
    private String deviceName;
    /**
     * 方法参数控制集合
     */
    private List<MemberDeviceMethodDTO> methodList;
}
