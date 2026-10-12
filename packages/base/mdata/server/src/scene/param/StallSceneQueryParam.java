package com.guhecloud.cube.biz.mdata.server.scene.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.Min;

/**
 * 档位场景查询参数
 *
 * @author lanchun
 * @date 2024/4/1 10:42
 */
@Data
public class StallSceneQueryParam extends Paging {

    /**
     * 所属项目id
     */
    private Long projectId;

    /**
     * 场景类型
     */
    @Min(value = 1, message = "场景类型不能小于1")
    private Long sceneType;

    /**
     * 区域主键
     */
    @Min(value = 1, message = "区域主键不能小于1")
    private Long areaId;

    /**
     * 档位类型主键
     */
    private Long categoryId;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 启动状态
     */
    private Boolean enabled;

}
