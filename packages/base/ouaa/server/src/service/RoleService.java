package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.Role;
import com.guhecloud.cube.biz.ouaa.server.param.RoleModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.RoleQueryParam;
import com.guhecloud.platform.base.model.Paging;

import java.util.List;

/**
 * 角色业务操作接口
 *
 * @author Woody
 * @date 2021/6/30
 */
public interface RoleService extends IService<Role> {
    /**
     * 删除角色并返回是否删除成功
     *
     * @param id 角色主键
     *
     * @return true/false
     */
    boolean deleteRole(Long id);

    /**
     * 新增角色
     *
     * @param param 新增参数
     *
     * @return 角色实例
     */
    Role addRole(RoleModifyParam param);

    /**
     * 修改角色
     *
     * @param id    角色主键
     * @param param 修改参数
     *
     * @return 角色实例
     */
    Role updateRole(Long id, RoleModifyParam param);

    /**
     * 查询角色
     *
     * @param param 查询参数
     *
     * @return 角色列表分页
     */
    Page<Role> listRoles(RoleQueryParam param);

    /**
     * 获取产品拥有的角色列表
     *
     * @param productId 产品主键
     *
     * @return 角色列表
     */
    List<Role> listProductRoles();

    /**
     * 获取产品拥有的角色分页列表
     *
     * @param productId 产品主键
     * @param paging    分页参数
     *
     * @return 角色列表
     */
    Page<Role> pageProductRoles(Paging paging);
}
