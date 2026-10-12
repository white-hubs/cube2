package com.guhecloud.cube.base.device.center.open.interfaces.web.query;

import com.guhecloud.platform.base.model.Paging;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class TagDeviceGroupDeviceQueryParam extends Paging {

    /**
     * 标签code
     */
    @NotNull(message = "标签编号不能为空")
    private String tagCode;

    /**
     * 设备组id集合
     */
    private Set<Long> groupIds;
}
