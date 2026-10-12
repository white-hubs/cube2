package com.guhecloud.cube.base.device.center.open.interfaces.web.query;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
public class DeviceGroupQueryParam extends Paging {

    /**
     * 标签编码
     */
    @NotBlank(message = "标签编码不能为空")
    private String tagCode;

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;
}
