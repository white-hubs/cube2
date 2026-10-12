package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;

/**
 * @Author: lijie
 * @CreateTime: 2024-07-29  10:57
 * @Description:
 * @Version: 1.0
 */
@Data
public class ApprovePageParam extends Paging {
    /**
     * 业务类型
     */
    @NotBlank(message = "业务类型不能为空")
    private String businessType;
    /**
     * 场景编码
     */
    private String sceneCode;
    /**
     * 时间
     */
    private Period period;
    /**
     * 用户id
     */
    private Long userId;
}
