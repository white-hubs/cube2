package com.guhecloud.cube.biz.ouaa.server.controller.tenant;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.ouaa.server.dao.UserManagedOrganizationMapper;
import com.guhecloud.cube.biz.ouaa.server.dao.UserTenantMapper;
import com.guhecloud.cube.biz.ouaa.server.dto.KeyValueDTO;
import com.guhecloud.cube.biz.ouaa.server.dto.UserTenantDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.UserTenant;
import com.guhecloud.cube.biz.ouaa.server.enumeration.TenantUserWorkStatusEnum;
import com.guhecloud.cube.biz.ouaa.server.param.RenewWorkStatusBatchParam;
import com.guhecloud.cube.biz.ouaa.server.param.RenewWorkStatusParam;
import com.guhecloud.cube.biz.ouaa.server.param.RenewUserTenantEnabledBatchParam;
import com.guhecloud.cube.biz.ouaa.server.param.WorkStatusQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.UserTenantService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户管理接口
 *
 * @author Woody
 * @date 2021/7/4
 */
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserTenantController {
    private static final int BATCH_UPDATE_SIZE = 1000;
    private final UserManagedOrganizationMapper userManagedOrganizationMapper;
    private final UserTenantMapper userTenantMapper;
    private final UserTenantService userTenantService;

    /**
     * 查询管理的部门+职位的用户
     */
    @GetMapping("/tenant/managed/dept/user/list")
    public Set<Long> getManagedDeptList(@RequestParam("departmentId") Long departmentId, @RequestParam("postIds") Set<Long> postIds) {
        return userManagedOrganizationMapper.selectManagedDeptIds(departmentId, postIds);
    }

    /**
     * 查询指定部门和职位的用户
     */
    @GetMapping("/tenant/managed/dept/user/listByAppointment")
    public Set<Long> getManagedDeptListByAppointment(@RequestParam("departmentId") Long departmentId,
                                                     @RequestParam("appointmentIds") Set<Long> appointmentIds
                                                    ) {
        return userManagedOrganizationMapper.getManagedDeptListByAppointment(departmentId, appointmentIds);
    }

    /**
     * 查询指定部门和岗位的用户
     */
    @GetMapping("/tenant/dept/user/listByPost")
    public Set<Long> getDeptListByPost(@RequestParam("departmentId") Long departmentId, @RequestParam("postIds") Set<Long> postIds) {
        return userManagedOrganizationMapper.getDeptListByPost(departmentId, postIds);
    }

    /**
     * 获取人员工作状态
     *
     * @param tenant  租户
     * @param userIds 人员id集合
     *
     * @return 租户人员工作状态
     */
    @GetMapping("/tenant/user/work/status")
    public List<UserTenantDTO> getTenantUserWorkStatus(@RequestParam("tenant") Integer tenant, @RequestParam("userIds") Set<Long> userIds) {

        return userTenantService.getTenantUserWorkStatus(tenant, userIds);
    }

    /**
     * 获取人员工作状态Post
     *
     * @param param 条件
     *
     * @return 租户人员工作状态
     */
    @PostMapping("/tenant/user/work/status/post")
    public List<UserTenantDTO> getTenantUserWorkStatusPost(@RequestBody @Valid WorkStatusQueryParam param) {

        return userTenantService.getTenantUserWorkStatus(param.getTenant(), param.getUserIdList());
    }

    /**
     * 更新租户人员工作状态
     *
     * @param param 更新参数
     */
    @PostMapping("/tenant/renew/work/status")
    public void updateUserTenantWorkStatus(@RequestBody RenewWorkStatusParam param) {
        if (param == null || param.getTenant() == null || param.getUserId() == null) {
            return;
        }
        userTenantMapper.update(null, Wrappers.<UserTenant>lambdaUpdate()
                                        .eq(UserTenant::getTenant, param.getTenant())
                                        .eq(UserTenant::getUserId, param.getUserId())
                                        .set(UserTenant::getWorkStatus, param.getWorkStatus())
                               );
    }

    /**
     * 批量更新租户人员启停状态
     *
     * @param param 更新参数
     */
    @PostMapping("/tenant/renew/user/enabled/batch")
    public void batchUpdateUserTenantEnabled(@RequestBody RenewUserTenantEnabledBatchParam param) {
        if (param == null || param.getTenant() == null) {
            return;
        }
        Set<Long> enableUserIds = cleanUserIds(param.getEnableUserIds());
        Set<Long> disableUserIds = cleanUserIds(param.getDisableUserIds());
        disableUserIds.removeAll(enableUserIds);
        batchUpdateUserTenantEnabled(param.getTenant(), enableUserIds, Boolean.TRUE);
        batchUpdateUserTenantEnabled(param.getTenant(), disableUserIds, Boolean.FALSE);
    }

    /**
     * 批量更新租户人员工作状态
     *
     * @param param
     */
    @PostMapping("/tenant/renew/work/status/batch")
    public void batchUpdateTenantUserWorkStatus(@RequestBody RenewWorkStatusBatchParam param) {
        userTenantService.batchUpdateTenantUserWorkStatus(param);
    }

    /**
     * 查询在岗、离岗人数
     *
     * @return 在岗、离岗人数
     */
    @GetMapping("/tenant/work/status/getTotal")
    public List<KeyValueDTO> getStateTotal() {
        List<KeyValueDTO> list = userTenantMapper.getStateTotal(SessionContextHolder.getRequester().getTenant());
        return TenantUserWorkStatusEnum.getAllUserTotal(list);
    }

    private Set<Long> cleanUserIds(Collection<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return new LinkedHashSet<>();
        }
        Set<Long> result = new LinkedHashSet<>();
        for (Long userId : userIds) {
            if (userId != null) {
                result.add(userId);
            }
        }
        return result;
    }

    private void batchUpdateUserTenantEnabled(Integer tenant, Set<Long> userIds, Boolean enabled) {
        if (CollectionUtils.isEmpty(userIds)) {
            return;
        }
        List<Long> userIdList = new ArrayList<>(userIds);
        for (int from = 0; from < userIdList.size(); from += BATCH_UPDATE_SIZE) {
            List<Long> batch = userIdList.subList(from, Math.min(from + BATCH_UPDATE_SIZE, userIdList.size()));
            if (Boolean.TRUE.equals(enabled)) {
                userTenantMapper.update(null, Wrappers.<UserTenant>lambdaUpdate()
                                                .eq(UserTenant::getTenant, tenant)
                                                .in(UserTenant::getUserId, batch)
                                                .eq(UserTenant::getEnabled, Boolean.FALSE)
                                                .set(UserTenant::getEnabled, Boolean.TRUE));
            } else {
                userTenantMapper.update(null, Wrappers.<UserTenant>lambdaUpdate()
                                                .eq(UserTenant::getTenant, tenant)
                                                .in(UserTenant::getUserId, batch)
                                                .and(wrapper -> wrapper.eq(UserTenant::getEnabled, Boolean.TRUE)
                                                                      .or()
                                                                      .isNull(UserTenant::getEnabled))
                                                .set(UserTenant::getEnabled, Boolean.FALSE));
            }
        }
    }

}
