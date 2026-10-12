package com.guhecloud.cube.base.device.center.scene.param;

import com.guhecloud.cube.base.device.center.metadata.lang.DeviceInstanceStatusEnum;
import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/**
 * 设施场景查询参数
 *
 * @author lanchun
 * @date 2021/12/3 11:26
 */
@Data
public class DeviceSceneQueryParam extends Paging {

    /**
     * 场景类型
     */
    @NotNull(message = "场景类型不能为空")
    @Min(value = 1, message = "场景类型不能小于1")
    private Long sceneType;

    /**
     * 区域主键
     */
    private Long areaId;

    /**
     * 状态
     */
    private DeviceInstanceStatusEnum status;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 产品品类编号
     */
    private String prodCategoryCode;

}
