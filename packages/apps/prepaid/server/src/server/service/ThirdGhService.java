package com.guhecloud.cube.srv.prepaid.server.service;

import com.guhecloud.cube.srv.prepaid.common.vo.AmmeterDTO;
import com.guhecloud.platform.base.model.ActorTypeEnum;

import java.time.LocalDateTime;
import java.util.List;

public interface ThirdGhService {

    List<AmmeterDTO> wholePullAmmeter(Integer tenant, LocalDateTime nowTime, String username, String userId,
                                      ActorTypeEnum userType
                                     );

    void wholePullPowerRecharge(Integer tenant, LocalDateTime nowTime, String username, String userId,
                                ActorTypeEnum userType, LocalDateTime startDate, LocalDateTime endDate,
                                List<String> meterIdList
                               );

    void wholePullPowerUseDay(Integer tenant, LocalDateTime nowTime, String username, String userId,
                              ActorTypeEnum userType, LocalDateTime startDate, LocalDateTime endDate,
                              List<String> meterIdList
                             );

    void wholePullCollectRecords(Integer tenant, LocalDateTime nowTime, String username, String userId,
                                 ActorTypeEnum userType, List<String> meterIdList
                                );
}
