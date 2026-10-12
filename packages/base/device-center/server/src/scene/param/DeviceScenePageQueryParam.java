package com.guhecloud.cube.base.device.center.scene.param;

import com.guhecloud.cube.base.device.center.metadata.lang.DeviceInstanceStatusEnum;
import com.guhecloud.cube.base.device.center.scene.enumeration.BusinessSceneConfigTypeEnum;
import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Data
public class DeviceScenePageQueryParam extends Paging {

    /**
     * 场景类型
     */
    @NotEmpty(message = "场景类型不能为空")
    private String sceneType;

    /**
     * 区域主键
     */
    private Long areaId;

    /**
     * 日期类型
     */
    private String type;

    /**
     * 设施类型
     */
    private BusinessSceneConfigTypeEnum businessType;

    private DeviceInstanceStatusEnum status;

    private String keyword;

    private Long genreId;

    private List<Long> deviceIds;

    private Period period;
}
