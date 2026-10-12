package com.guhecloud.cube.srv.gen.cabbage.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.cabbage.server.entity.DbcDeptPO;
import com.guhecloud.cube.srv.gen.cabbage.server.vo.dto.DbcDeptDTO;
import com.guhecloud.platform.base.model.ActorTypeEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 大白菜部门服务
 */
public interface DbcDeptService extends IService<DbcDeptPO> {
    Map<String, DbcDeptPO> dbcDeptPull(Integer marketId, Integer tenant, LocalDateTime newTime, String username,
                                       String userId,
                                       ActorTypeEnum userType,
                                       List<DbcDeptDTO> deptDTOList
                                      );
}
