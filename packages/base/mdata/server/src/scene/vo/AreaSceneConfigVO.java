package com.guhecloud.cube.biz.mdata.server.scene.vo;

import lombok.Data;

/**
 * 区域场景配置返回参数
 *
 * @author lanchun
 * @date 2021/12/2 15:00
 */
@Data
public class AreaSceneConfigVO {

    private Long id;

    /**
     * 所属项目id
     */
    private Long projectId;

    /**
     * 区域主键
     */
    private Long areaId;

    /**
     * 场景类型
     */
    private Long sceneType;

    /**
     * 场景类型序列号
     */
    private String sceneTypeSequence;
}
