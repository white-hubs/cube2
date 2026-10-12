package com.guhecloud.cube.biz.mdata.server.scene.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 漫游配置分页返回参数
 *
 * @author lanchun
 * @date 2022/9/19 16:18
 */
@Data
public class AreaRoamingConfigPageVO {

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
     * 区域名称
     */
    private List<String> areaNames;

    /**
     * 区域序列号
     */
    private String areas;

    /**
     * 漫游配置
     */
    private String roamingConfig;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
