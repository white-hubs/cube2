package com.guhecloud.cube.srv.gen.attendance.management.server.attend.scheduling.interfaces.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

/**
 * @Author: lijie
 * @CreateTime: 2024-08-15  10:46
 * @Description:
 * @Version: 1.0
 */
@Data
public class ArrangeWorkParam {
    /**
     * 人员id集合
     */
    @NotNull(message = "人员id集合不能为空")
    private Set<Long> userIds;
    /**
     * 开始时间
     */
    @NotNull(message = "工作时间不能为空")
    private LocalDateTime startDate;
}
