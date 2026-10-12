package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class ProjectAreaCountVO {
    /**
     * 项目ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long projectId;
    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 区域数量
     */
    private Long count;
}
