package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.Tenant;
import com.guhecloud.cube.biz.ouaa.server.param.TenantModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.TenantProductModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.TenantQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.TenantUpdateParam;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 租户业务操作接口
 *
 * @author Woody
 * @date 2021/7/1
 */
public interface TenantService extends IService<Tenant> {

    Tenant addTenant(TenantModifyParam param);

    /**
     * 删除租户并返回是否删除成功
     *
     * @param id 租户主键
     *
     * @return true/false
     */
    boolean deleteTenant(Long id);

    Tenant updateTenant(Long id, TenantModifyParam param);

    /**
     * 保存租户应用
     *
     * @param tenant
     * @param param
     */
    void updateProductTenant(Integer tenant, TenantProductModifyParam param);

    /**
     * 企业管理端管理员修改租户
     *
     * @param param 更新参数
     */
    void update(TenantUpdateParam param);

    /**
     * 通过编号查询租户
     *
     * @param code
     *
     * @return
     */
    Tenant getByCode(Integer code);

    List<Tenant> listByCodes(Collection<? extends Serializable> codes);

    /**
     * 获取租户列表
     *
     * @param param 查询参数
     *
     * @return 租户列表分页
     */
    Page<Tenant> listTenants(TenantQueryParam param);

    /**
     * 查询只包含市场租户的数据
     *
     * @param param
     *
     * @return
     */
    Page<Tenant> listTenantsWithMarket(TenantQueryParam param);

    /**
     * 获取租户列表
     *
     * @return 租户列表
     */
    List<Tenant> listTenants(String keyword, String category);

    /**
     * 根据名字或编号获取租户列表
     *
     * @param name 名称
     * @param code 编号
     *
     * @return 租户列表
     */
    List<Tenant> listTenantsByNameOrCode(String name, Integer code);

    /**
     * 根据租户主键获取租户列表
     *
     * @param ids 租户主键集合
     *
     * @return 租户列表
     */
    List<Tenant> listTenants(List<Long> ids);

    /**
     * 根据租户编号获取租户列表
     *
     * @param tenants 租户编号集合
     *
     * @return 租户列表
     */
    List<Tenant> listTenantsByCode(List<Integer> tenants);

    /**
     * 更新用户最近登录租户信息
     *
     * @param tenant 租户编号
     */
    void updateRecentLoginTenant(Integer tenant);

    /**
     * 更新用户租户主企业信息
     *
     * @param tenant 租户编号
     */
    void updateMainEnterpriseTenant(Integer tenant);

    String getComplaintPhone(Integer tenant);
}
