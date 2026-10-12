package com.guhecloud.cube.srv.gen.cabbage.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.cabbage.server.entity.DbcStoreTypePO;
import com.guhecloud.cube.srv.gen.cabbage.server.vo.dto.DbcStoreTypeDTO;
import com.guhecloud.platform.base.model.ActorTypeEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 大白菜挡位类型服务
 */
public interface DbcStoreTypeService extends IService<DbcStoreTypePO> {
    Map<String, DbcStoreTypePO> dbcStoreTypePull(Integer marketId, Integer tenant, LocalDateTime newTime, String username,
                                                 String userId,
                                                 ActorTypeEnum userType,
                                                 List<DbcStoreTypeDTO> storeTypeDTOList
                                                );
}
