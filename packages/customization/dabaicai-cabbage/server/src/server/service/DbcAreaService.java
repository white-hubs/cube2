package com.guhecloud.cube.srv.gen.cabbage.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.cabbage.server.entity.DbcAreaPO;
import com.guhecloud.cube.srv.gen.cabbage.server.vo.dto.DbcAreaDTO;
import com.guhecloud.platform.base.model.ActorTypeEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 大白菜区域服务接口
 */
public interface DbcAreaService extends IService<DbcAreaPO> {
    Map<String, DbcAreaPO> dbcAreaPull(Integer marketId, Integer tenant, LocalDateTime newTime, String username,
                                       String userId,
                                       ActorTypeEnum userType,
                                       List<DbcAreaDTO> areaDTOList
                                      );
}
