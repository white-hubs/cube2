package com.guhecloud.cube.srv.gen.cabbage.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.cabbage.server.entity.DbcVehiclePO;
import com.guhecloud.cube.srv.gen.cabbage.server.vo.dto.DbcVehicleDTO;
import com.guhecloud.platform.base.model.ActorTypeEnum;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 大白菜车辆service
 */
public interface DbcVehicleService extends IService<DbcVehiclePO> {
    void dbcVehiclePull(Integer marketId, Integer tenant, LocalDateTime newTime, String username,
                        String userId, ActorTypeEnum userType, List<DbcVehicleDTO> vehicleDTOList
                       );
}
