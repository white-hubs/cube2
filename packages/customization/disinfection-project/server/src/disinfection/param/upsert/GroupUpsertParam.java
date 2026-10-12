package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.upsert;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Data
public class GroupUpsertParam {
    private Long projectId;
    private List<GroupParam> groups;

    @Data
    public static class GroupParam {
        private Long groupId;

        @NotBlank(message = "分组名称不能为空")
        private String groupName;

        private List<Long> userIds;
    }

}
