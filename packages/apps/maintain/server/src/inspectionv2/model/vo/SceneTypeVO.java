package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo;

import lombok.Data;
import lombok.experimental.SuperBuilder;

/**
 * @author wql
 * @describe 现场点类型
 * @date 2023/6/5 16:28
 */
@Data
@SuperBuilder
public class SceneTypeVO {
    /**
     * 现场点类型id
     */
    private Long id;
    /**
     * 现场点类型名称
     */
    private String name;
}
