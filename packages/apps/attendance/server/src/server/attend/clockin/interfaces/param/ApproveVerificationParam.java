package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param;

import com.guhecloud.cube.base.flow.client.param.FormDataParam;
import lombok.Data;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-07  19:35
 * @Description:
 * @Version: 1.0
 */
@Data
public class ApproveVerificationParam {
    /**
     * 审核类型
     */
    private String businessType;
    /**
     * 审核数据
     */
    private FormDataParam flowData;
}
