package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.page;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class AreaPageVO {
    /**
     * 主键ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 消杀项目ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long projectId;

    /**
     * 消杀项目名称
     */
    private String projectName;

    /**
     * 消杀区域名称
     */
    private String areaName;

    /**
     * 消杀区域详情
     */
    private String areaDetail;
}
