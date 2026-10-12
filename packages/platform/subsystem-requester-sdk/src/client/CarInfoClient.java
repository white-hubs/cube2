package com.guhecloud.subsystem.requester.sdk.client;

import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.requester.sdk.config.DynamicConfiguration;
import com.guhecloud.subsystem.requester.sdk.param.parking.CarInfoPageQueryParam;
import com.guhecloud.subsystem.requester.sdk.vo.car.CarInfoVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * 车辆管理client
 *
 * @author lanchun
 * @date 2023/6/15 16:58
 */
@FeignClient(name = SubsystemTypeConstants.CAR_SERVER, configuration = DynamicConfiguration.class)
public interface CarInfoClient {

    /**
     * 车辆列表分页查询
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/car-info")
    Pagination<CarInfoVO> getCarInfoPage(@SpringQueryMap CarInfoPageQueryParam param);
}
