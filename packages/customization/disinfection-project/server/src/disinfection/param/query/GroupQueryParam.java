package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.param.query;

import lombok.Data;

@Data
public class GroupQueryParam {
    private String projectId;
    private Long groupId;
    private Long deptId;
    private Long postId;
    private String keyword;
}
