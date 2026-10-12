package com.guhecloud.cube.biz.ouaa.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

@Mapper
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

    List<RoleMenu> listByRoleTerminalApplication(@Param("roleId") Long roleId, @Param("terminalId") Long terminalId,
                                                 @Param("applicationId") Long applicationId
                                                );

    List<RoleMenu> listByRoleTerminal(@Param("roleId") Long roleId, @Param("terminalId") Long terminalId);

    List<RoleMenu> listByTenant(@Param("tenantId") Long tenantId);

    List<RoleMenu> listByRoleTerminalApplicationBatch(@Param("roleIds") Collection<Long> roleIds,
                                                      @Param("terminalIdList") Collection<Long> terminalIdList,
                                                      @Param("applicationIdList") Collection<Long> applicationIdList
                                                     );
}
