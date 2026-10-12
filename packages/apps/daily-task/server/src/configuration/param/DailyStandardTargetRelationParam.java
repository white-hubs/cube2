package com.guhecloud.cube.biz.daily.task.configuration.param;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * 根据对象ID及类型ID查询关联标准的请求参数
 */
@Data
public class DailyStandardTargetRelationParam {

    @NotEmpty(message = "对象列表不能为空")
    private List<TargetRelationItem> targets;

    @Data
    public static class TargetRelationItem {
        @NotNull(message = "对象ID不能为空")
        private Long id;

        @NotNull(message = "typeId不能为空")
        private String typeId;

        private Set<Long> tagIds;
    }
}

