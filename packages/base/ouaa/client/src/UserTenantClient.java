package com.guhecloud.cube.biz.ouaa.client;

import com.guhecloud.cube.biz.ouaa.client.param.RenewUserTenantEnabledBatchParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Set;

/**
 * 租户用户关联接口
 *
 * @author lzy
 * @date 2021/10/12
 */
@FeignClient(name = "ouaa")
public interface UserTenantClient {

    /**
     * 新增用户租户关联关系
     *
     * @param tenant 租户编号
     */
    @PostMapping("/user-tenant/{tenant}/{userId}")
    public void addUserTenant(@PathVariable("tenant") Integer tenant, @PathVariable("userId") Long userId);

    /**
     * 删除用户租户关联关系
     *
     * @param tenant 租户编号
     * @param userId 用户主键
     */
    @DeleteMapping("/user-tenant/{tenant}/{userId}")
    public void removeUserTenant(@PathVariable("tenant") Integer tenant, @PathVariable("userId") Long userId);

    /**
     * 批量更新租户人员启停状态
     */
    @PostMapping("/tenant/renew/user/enabled/batch")
    void batchUpdateUserTenantEnabled(@RequestBody RenewUserTenantEnabledBatchParam param);

    @GetMapping("/tenant/managed/dept/user/list")
    Set<Long> getManagedDeptList(@RequestParam("departmentId") Long departmentId, @RequestParam("postIds") Set<Long> postIds);

    /**
     * 通过部门id+职位id查询管理的部门的用户
     *
     * @param departmentId   管理部门id
     * @param appointmentIds 职位id
     */
    @GetMapping("/tenant/managed/dept/user/listByAppointment")
    Set<Long> getManagedDeptListByAppointment(@RequestParam("departmentId") Long departmentId,
                                              @RequestParam("appointmentIds") Set<Long> appointmentIds
                                             );

    /**
     * 通过部门id+岗位id查询用户
     *
     * @param departmentId 部门id
     * @param postIds      岗位id
     *
     * @return 用户列表
     */
    @GetMapping("/tenant/dept/user/listByPost")
    Set<Long> getDeptListByPost(@RequestParam("departmentId") Long departmentId, @RequestParam("postIds") Set<Long> postIds);
}
