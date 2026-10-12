package com.guhecloud.cube.srv.gen.medical.cleanliness.server.detection.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.List;

/**
 * 检测标准树对象
 */
@Data
public class StandardTreeVO {
    /**
     * id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 依据id
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long basicId;

    /**
     * 依据名称
     */
    private String basicName;

    /**
     * 检测项目集合
     */
    private List<PropertyVO> propertyList;
}
