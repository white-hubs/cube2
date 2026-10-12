package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.dto.UserTenantDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTenant;
import com.guhecloud.cube.biz.ouaa.server.param.RenewWorkStatusBatchParam;
import com.guhecloud.cube.biz.ouaa.server.param.UserTenantQueryParam;
import com.guhecloud.platform.base.model.Pagination;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 用户租户关联业务操作接口
 *
 * @author Woody
 * @date 2021/7/13
 */
public interface UserTenantService extends IService<UserTenant> {

    /**
     * 新增用户租户关联关系
     *
     * @param tenant 租户编号
     */
    void addUserTenant(Integer tenant, Long userId);

    void addUserTenantBatch(Integer tenant, List<Long> userIds);

    /**
     * 删除用户租户关联关系
     *
     * @param tenant 租户编号
     */
    void removeUserTenant(Integer tenant, Long userId);

    /**
     * 新增用户租户关联关系
     *
     * @param userId  用户主键
     * @param enabled 是否启用
     */
    void addUserTenantByUserId(Long userId, Boolean enabled);

    /**
     * 获取用户租户关联信息
     *
     * @param tenant 租户编号
     *
     * @return 用户租户关联信息
     */
    UserTenant getUserTenantAdmin(Integer tenant);

    /**
     * 获取用户租户关联信息列表
     *
     * @param tenant 租户编号
     *
     * @return 用户租户关联信息
     */
    List<UserTenant> listUserTenants(Integer tenant);

    Pagination<UserTenant> pageUserTenants(Integer tenant, UserTenantQueryParam param);

    /**
     * 根据用户主键集合获取用户租户关联关系
     *
     * @param userIds 用户主键
     * @param tenant  市场租户编号
     *
     * @return 用户租户关联关系
     */
    List<UserTenant> listUserTenantByUserIds(Collection<? extends Serializable> userIds, Integer tenant);

    /**
     * 获取用户租户关联信息
     *
     * @param userId 用户主键
     *
     * @return 用户租户关联信息
     */
    List<UserTenant> getByUserId(Long userId);

    /**
     * 获取用户租户关联信息
     *
     * @param userId 用户主键
     * @param tenant 租户编号
     *
     * @return 用户租户关联信息
     */
    UserTenant getUserTenantByUserIdAndTenant(Long userId, Integer tenant);

    /**
     * 获取用户租户关联信息列表
     *
     * @param userId 用户主键集合
     *
     * @return 用户租户关联信息列表
     */
    List<UserTenant> listUserTenants(Long userId);

    /**
     * 获取租户人员工作状态
     *
     * @param tenant  租户id
     * @param userIds 人员ids
     *
     * @return 集合
     */
    List<UserTenantDTO> getTenantUserWorkStatus(Integer tenant, Set<Long> userIds);

    void batchUpdateTenantUserWorkStatus(RenewWorkStatusBatchParam param);

    /**
     * @deprecated 接口设计不合理，已废弃
     */
    @Deprecated
    void updateUserTenantEnable(long userId, Boolean userTenantEnable);

    void updateUserTenantEnable(Integer tenant, long userId, Boolean userTenantEnable);
}
