package com.guhecloud.cube.biz.mdata.server.scene.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.Size;

/**
 * 区域漫游查询参数
 *
 * @author lanchun
 * @date 2022/9/19 15:59
 */
@Data
public class AreaRoamingConfigQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

    /**
     * 场景类型
     */
    private Long sceneType;

    /**
     * 区域主键
     */
    private Long areaId;
}
