package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param;

import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class AttClockRulesParam {
    private Long id;
    @NotBlank(message = "名称不能为空")
    private String name;
    /**
     * 打卡方式(枚举AttClockWayEnum)
     */
    private String ways;
    /**
     * 是否可以外勤
     */
    private Boolean outClock;
    /**
     * 是否可以人脸识别
     */
    private Boolean faceRecognition;
    /**
     * 打卡地点
     */
    private List<AttClockLocationParam> locationList;
    /**
     * 打卡设备
     */
    private List<AttClockDeviceParam> deviceList;

    /**
     * 业务校验
     */
    public void validBusiness() {
        if (StringUtils.isNotBlank(ways) && locationList == null && deviceList == null) {
            throw new ServiceException("需要打卡，但没有任何打卡方式");
        }
        if (StringUtils.isBlank(ways) && (locationList != null || deviceList != null)) {
            throw new ServiceException("添加了打卡方式，但是不需要打卡");
        }
    }
}
