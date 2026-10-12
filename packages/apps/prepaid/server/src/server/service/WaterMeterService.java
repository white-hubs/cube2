package com.guhecloud.cube.srv.prepaid.server.service;

import com.guhecloud.cube.srv.prepaid.common.vo.WaterMeterDTO;
import com.guhecloud.cube.srv.prepaid.server.param.WaterMeterPageParam;
import com.guhecloud.cube.srv.prepaid.server.result.WaterMeterPageResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.ActorTypeEnum;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 水表
 */
public interface WaterMeterService {
    Pagination<WaterMeterPageResult> getWaterMeterPage(WaterMeterPageParam param);

    void wholePull(List<WaterMeterDTO> dtoList, Integer tenant, LocalDateTime nowTime, String username, String userId,
                   ActorTypeEnum userType
                  );
}
