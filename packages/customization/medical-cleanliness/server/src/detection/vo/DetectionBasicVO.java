package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class DetectionBasicVO {
    /**
     * ID
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;
    /**
     * 名称
     */
    private String name;
    /**
     * 检测属性数量
     */
    private Integer propertyNum;
    /**
     * 标准级别名称
     */
    private String standardLevelNames;

}
