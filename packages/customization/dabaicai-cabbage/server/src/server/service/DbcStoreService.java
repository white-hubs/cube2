package com.guhecloud.cube.srv.gen.cabbage.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.cabbage.server.entity.DbcAreaPO;
import com.guhecloud.cube.srv.gen.cabbage.server.entity.DbcDeptPO;
import com.guhecloud.cube.srv.gen.cabbage.server.entity.DbcStorePO;
import com.guhecloud.cube.srv.gen.cabbage.server.entity.DbcStoreTypePO;
import com.guhecloud.cube.srv.gen.cabbage.server.vo.dto.DbcStoreDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 大白菜挡位服务
 */
public interface DbcStoreService extends IService<DbcStorePO> {
    void dbcStorePull(Integer marketId, Integer tenant, LocalDateTime newTime, String username, List<DbcStoreDTO> storeDTOList,
                      Map<String, DbcDeptPO> oldDeptMap, Map<String, DbcAreaPO> oldAreaMap, Map<String, DbcStoreTypePO> oldStoreTypeMap
                     );
}
