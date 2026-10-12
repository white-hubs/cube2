package com.guhecloud.cube.srv.gen.disinfection.server.disinfection.vo.list;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class DataTypeVO {
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    /**
     * 名称
     */
    private String dataName;
    /**
     * 数量
     */
    private Long count;

    /**
     * 图标文件信息
     */
    private Long icon;
}
