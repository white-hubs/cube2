package com.guhecloud.cube.biz.ouaa.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleMenuDataPermission;
import com.guhecloud.cube.biz.ouaa.server.vo.DataPermissionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;

/**
 * 角色菜单数据权限功能接口
 *
 * @author lanchun
 * @date 2021/12/24 14:50
 */
@Mapper
public interface RoleMenuDataPermissionMapper extends BaseMapper<RoleMenuDataPermission> {

    @Select("select dp.`id`,dp.`menu_id` as `menuId`,dp.`permission` as `code`,dp.`sort_no`\n" +
            "from `ouaa_role_menu_data_permission` dp\n" +
            "where dp.`terminal_id` = #{terminalId} and dp.`application_id` = #{applicationId} and dp.`menu_id` = #{menuId} " +
            "and dp.`role_id` in (${roleIds})"
    )
    List<DataPermissionVO> getRoleMenuDataPermission(@Param("roleIds") String roleIds,
                                                     @Param("terminalId") Long terminalId,
                                                     @Param("applicationId") Long applicationId,
                                                     @Param("menuId") Long menuId
                                                    );

    List<RoleMenuDataPermission> listByTenant(@Param("tenantId") Long tenantId);

    void removeByRoleMenuIds(@Param("roleMenuIds") Collection<Long> roleMenuIds);
}
