package com.guhecloud.subsystem.requester.sdk.param.parking;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

/**
 * 车辆信息分页查询参数
 *
 * @author lanchun
 * @date 2023/6/15 15:13
 */
@Data
public class CarInfoPageQueryParam extends Paging {

    private String keyword;
}
