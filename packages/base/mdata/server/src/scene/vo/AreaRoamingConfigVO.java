package com.guhecloud.cube.biz.mdata.server.scene.vo;

import lombok.Data;

import java.util.List;

/**
 * 漫游配置返回参数
 *
 * @author lanchun
 * @date 2022/9/19 16:18
 */
@Data
public class AreaRoamingConfigVO {

    private Long id;

    /**
     * 漫游名称
     */
    private String name;

    /**
     * 场景类型
     */
    private Long sceneType;

    /**
     * 区域主键
     */
    private Long areaId;

    /**
     * 区域主键集合
     */
    private List<String> areaIds;

    /**
     * 区域序列号
     */
    private String areas;

    /**
     * 漫游配置
     */
    private String roamingConfig;
}
