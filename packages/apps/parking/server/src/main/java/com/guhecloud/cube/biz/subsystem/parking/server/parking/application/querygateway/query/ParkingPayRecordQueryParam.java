package com.guhecloud.cube.biz.subsystem.parking.server.parking.application.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 车辆缴费记录分页查询参数
 *
 * @author lanchun
 * @date 2022/10/13 14:14
 */
@Data
public class ParkingPayRecordQueryParam extends Paging {

    /**
     * 关键字
     */
    @Size(max = 100, message = "关键字长度不能大于100")
    private String keyword;

    /**
     * 日期
     */
    private Period period;
}
