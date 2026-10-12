package com.guhecloud.cube.biz.ouaa.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.RoleMenuFunction;
import com.guhecloud.cube.biz.ouaa.server.vo.MenuFunctionVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Collection;
import java.util.List;

/**
 * 角色菜单功能接口
 *
 * @author lanchun
 * @date 2021/12/24 14:50
 */
@Mapper
public interface RoleMenuFunctionMapper extends BaseMapper<RoleMenuFunction> {

    @Select("select DISTINCT mf.`menu_id` as `menuId`,f.`id` as `functionId`,f.`name` as `functionName`,f.`code` as `functionCode`,f.`enabled`\n" +
            "from `ouaa_role_menu_function` mf\n" +
            "left join `ouaa_function` f on mf.`function_id` = f.`id`\n" +
            "where mf.`terminal_id` = #{terminalId} and mf.`application_id` = #{applicationId} " +
            "and mf.`menu_id` = #{menuId} and mf.`role_id` in (${roleIds})"
    )
    List<MenuFunctionVO> getRoleMenuFunctions(@Param("roleIds") String roleIds,
                                              @Param("terminalId") Long terminalId,
                                              @Param("applicationId") Long applicationId,
                                              @Param("menuId") Long menuId
                                             );

    @Select("SELECT"
            + "  ormdf.`id`,"
            + "  ormdf.`role_id`,"
            + "  ormdf.`terminal_id`,"
            + "  ormdf.`application_id`,"
            + "  ormdf.`menu_id`,"
            + "  ormdf.`function_id`"
            + "  FROM `role` r JOIN `ouaa_role_menu_function` ormdf ON r.`id` = ormdf.`role_id`"
            + "  WHERE  r.`tenant` = #{tenantId}"
    )
    List<RoleMenuFunction> listByTenant(Long tenantId);

    void removeByRoleMenuIds(@Param("roleMenuIds") Collection<Long> roleMenuIds);
}
