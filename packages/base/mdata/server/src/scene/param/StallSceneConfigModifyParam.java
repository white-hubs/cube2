package com.guhecloud.cube.biz.mdata.server.scene.param;

import com.guhecloud.cube.biz.mdata.server.scene.enumeration.SceneStallBindWayEnum;
import lombok.Data;

import jakarta.validation.constraints.NotNull;

import java.util.Set;

/**
 * 设施场景绑定保存参数
 *
 * @author lanchun
 * @date 2021/12/2 14:21
 */
@Data
public class StallSceneConfigModifyParam {

    /**
     * 场景类型
     */
    @NotNull(message = "场景类型不能为空")
    private Long sceneType;

    /**
     * 档位类型主键
     */
    private Long categoryId;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 档位绑定方式(指定档位 or 所有档位)
     */
    @NotNull(message = "档位绑定方式不能为空")
    private SceneStallBindWayEnum stallBindWay;

    /**
     * 业务主键
     */
    private Set<Long> businessIds;
}
