package com.guhecloud.cube.base.device.center.metadata.std.param;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;
import lombok.EqualsAndHashCode;

import jakarta.validation.constraints.Size;

@EqualsAndHashCode(callSuper = true)
@Data
public class CenterGroupQueryPageParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;
}
