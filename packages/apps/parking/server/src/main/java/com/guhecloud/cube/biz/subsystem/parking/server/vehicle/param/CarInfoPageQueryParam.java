package com.guhecloud.cube.biz.subsystem.parking.server.vehicle.param;

import com.guhecloud.platform.base.model.Paging;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CarInfoPageQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字不能大于100")
    private String keyword;

}
