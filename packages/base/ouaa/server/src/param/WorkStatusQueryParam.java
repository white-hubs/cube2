package com.guhecloud.cube.biz.ouaa.server.param;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.Set;

@Data
public class WorkStatusQueryParam {
    @NotNull(message = "租户不能为空")
    private Integer tenant;
    @NotNull(message = "人员不能为空")
    private Set<Long> userIdList;
}
