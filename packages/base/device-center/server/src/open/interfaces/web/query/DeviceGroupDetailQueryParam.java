package com.guhecloud.cube.base.device.center.open.interfaces.web.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotNull;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceGroupDetailQueryParam extends Paging {

    /**
     * 分组id
     */
    @NotNull(message = "分组id不能为空")
    private Long id;
}
