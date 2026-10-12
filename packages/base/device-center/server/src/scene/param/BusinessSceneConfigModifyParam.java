package com.guhecloud.cube.base.device.center.scene.param;

import com.guhecloud.cube.base.device.center.scene.enumeration.SceneDeviceBindWayEnum;
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
public class BusinessSceneConfigModifyParam {

    /**
     * 场景类型
     */
    @NotNull(message = "场景类型不能为空")
    private Long sceneType;

    /**
     * 产品分类编号序列号
     */
    private String deviceProdCategoryCodeConcat;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 设备绑定方式(指定设备 or 所有设备)
     */
    @NotNull(message = "设备绑定方式不能为空")
    private SceneDeviceBindWayEnum deviceBindWay;

    /**
     * 业务主键
     */
    private Set<Long> businessIds;
}
