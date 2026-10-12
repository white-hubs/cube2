package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result;

import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2024-03-11  16:51
 * @Description:
 * @Version: 1.0
 */
@Data
public class AttDeviceResult {
    private Long id;
    private String name;
    private AttProductInfoResult productInfo;
    private AttDepartmentResult department;
    private AttAreaResult area;
}
