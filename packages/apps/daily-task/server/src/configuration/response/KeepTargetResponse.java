package com.guhecloud.cube.biz.daily.task.configuration.response;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeepTargetResponse {
    private Long removeCount;
    private List<TargetRelationItem> targets;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class TargetRelationItem {
        @NotNull(message = "对象ID不能为空")
        private Long id;

        @NotNull(message = "typeId不能为空")
        private String typeId;
        private Set<Long> tagIds;
    }

    public static KeepTargetResponse empty() {
        return new KeepTargetResponse(0L, List.of());
    }
}
