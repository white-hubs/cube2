package com.guhecloud.cube.biz.ouaa.client;

import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserBelongsInstitutionParam;
import com.guhecloud.cube.biz.ouaa.client.param.UserModifyParam;
import com.guhecloud.cube.biz.ouaa.client.param.UserQueryParam;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserMapQuery;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.cube.biz.ouaa.metadata.param.UserRuleConditionParam;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 用户接口
 *
 * @author Woody
 * @date 2021/7/9
 */
@FeignClient(name = "ouaa")
public interface UserClient {
    /**
     * 获取当前租户下的管理员
     *
     * @return 用户信息
     */
    @GetMapping("/user/admin")
    UserDTO getAdmin();

    /**
     * 用户新增存在则返回用户信息
     *
     * @param param 新增参数
     *
     * @return 用户信息
     */
    @PostMapping("/authenticated/user/save")
    UserDTO save(@RequestBody @Valid UserModifyParam param);

    /**
     * 获取用户信息
     *
     * @param id 用户主键
     *
     * @return 用户信息
     */
    @GetMapping("/authenticated/user/{id}")
    UserDTO getUser(@PathVariable("id") Long id);

    /**
     * 通过账号 获取用户信息
     *
     * @param username 用户主键
     *
     * @return 用户信息
     */
    @GetMapping("/authenticated/user-under-tenant/username/{username}")
    UserDTO getUserByUserName(@PathVariable("username") String username);

    /**
     * 通过手机号 获取用户信息
     *
     * @param mobile 用户主键
     *
     * @return 用户信息
     */
    @GetMapping("/authenticated/user-under-tenant/mobile/{mobile}")
    UserDTO getUserByMobile(@PathVariable("mobile") String mobile);

    /**
     * 通过手机号 获取在租户下的用户信息
     *
     * @param mobile 用户主键
     * @param tenant 租户
     *
     * @return 用户信息
     */
    @GetMapping("/authenticated/user-under-tenant/{tenant}/mobile/{mobile}")
    public UserDTO getUserByMobile(@PathVariable("mobile") String mobile, @PathVariable("tenant") Integer tenant);

    @GetMapping("/authenticated/user-under-tenant/tenant/{tenant}")
    UserDTO getTenantAdminByTenant(@PathVariable("tenant") Integer tenant);

    /**
     * 查询用户信息
     *
     * @param param 查询参数
     *
     * @return 用户列表分页
     */
    @PostMapping("/authenticated/user/page/post")
    Pagination<UserDTO> postByPage(@RequestBody @Valid UserQueryParam param);

    /**
     * 查询用户信息
     *
     * @param param 查询参数
     *
     * @return 用户列表分页
     */
    @GetMapping("/authenticated/user/page")
    Pagination<UserDTO> listUsers(@SpringQueryMap UserQueryParam param);

    @GetMapping("/authenticated/user/list")
    List<UserDTO> listUsers(@RequestParam("keyword") String keyword);

    /**
     * 岗位下用户
     *
     * @param positionId 岗位主键
     *
     * @return
     */
    @GetMapping("/authenticated/position-users/{positionId}")
    List<UserDTO> positionUsers(@PathVariable("positionId") Long positionId);

    /**
     * 企业管理端用户新增接口
     *
     * @param param
     */
    @PostMapping("/user-under-tenant")
    Long saveByTenant(@RequestBody @Valid UserModifyParam param);

    @PostMapping("/authenticated/user-under-tenant")
    Long saveOfNormalByTenant(@RequestBody @Valid UserModifyParam param);

    @PutMapping("/authenticated/user-under-tenant/{id}")
    void updateOfNormalByTenant(@PathVariable("id") Long id, @RequestBody @Valid UserModifyParam param);

    /**
     * 企业管理端用户修改接口
     *
     * @param id
     * @param param
     */
    @PutMapping("/user-under-tenant/{id}")
    void updateByTenant(@PathVariable("id") Long id, @RequestBody @Valid UserModifyParam param);

    /**
     * 组织架构下用户(内部调用)
     *
     * @param organizationId 组织架构主键
     *
     * @return
     */
    @GetMapping("/authenticated/organization-users/{organizationId}")
    List<UserDTO> organizationUsers(@PathVariable("organizationId") Long organizationId);

    /**
     * 组织架构下启用用户(内部调用)
     *
     * @param organizationId 组织架构主键
     *
     * @return
     */
    @GetMapping("/authenticated/organization-enable-users/{organizationId}")
    List<UserDTO> organizationEnableUsers(@PathVariable("organizationId") Long organizationId);

    /**
     * 组织架构下岗位的用户列表(内部调用)
     *
     * @param organizationId
     * @param postId
     *
     * @return
     */
    @GetMapping("/authenticated/organization-users/{organizationId}/{postId}")
    List<UserDTO> organizationAndPostUsers(@PathVariable("organizationId") Long organizationId,
                                           @PathVariable("postId") Long postId
                                          );

    /**
     * 组织架构下岗位启用的用户列表(内部调用)
     *
     * @param organizationId
     * @param postId
     *
     * @return
     */
    @GetMapping("/authenticated/organization-users/enable/{organizationId}/{postId}")
    List<UserDTO> organizationAndPostEnabledUsers(@PathVariable("organizationId") Long organizationId,
                                                  @PathVariable("postId") Long postId
                                                 );

    /**
     * 通过部门和岗位ID 查询启用用户ID列表
     *
     * @param orgIds  组织机构ID
     * @param postIds 岗位ID
     *
     * @return 用户ID列表
     */
    @GetMapping("/authenticated/enable-user-under-tenant/orgAndPost")
    List<Long> listEnableUserByOrgAndPost(@RequestParam(value = "orgIds", required = false) List<Long> orgIds,
                                          @RequestParam(value = "postIds", required = false) List<Long> postIds
                                         );

    /**
     * 通过部门和岗位ID 查询用户ID列表
     *
     * @param orgIds  组织机构ID
     * @param postIds 岗位ID
     *
     * @return 用户ID列表
     */
    @GetMapping("/authenticated/user-under-tenant/orgAndPost")
    List<Long> listUserByOrgAndPost(@RequestParam(value = "orgIds", required = false) List<Long> orgIds
            , @RequestParam(value = "postIds", required = false) List<Long> postIds
                                   );

    @GetMapping("/authenticated/user-under-tenant/ids/list")
    List<UserDTO> listUserByIds(@RequestParam(value = "ids") Collection<Long> ids);

    @GetMapping("/authenticated/user-under-tenant/ids/enable/list")
    List<UserDTO> listEnableUserByIds(@RequestParam(value = "ids") Collection<Long> ids);

    @GetMapping("/authenticated/user-under-tenant/{tenant}/tenant")
    Pagination<UserDTO> listUserByTenant(@PathVariable("tenant") Integer tenant, @SpringQueryMap UserQueryParam queryParam);

    @GetMapping("/authenticated/user-under-tenant/list")
    List<UserDTO> listAllUsers(@SpringQueryMap UserQueryParam param);

    /**
     * 角色下面的用户
     *
     * @param roleIds
     *
     * @return
     */
    @GetMapping("/authenticated/role/user/list")
    List<UserDTO> listUsersByRoles(@RequestParam("roleIds") Set<Long> roleIds);

    /**
     * 查询用户列表
     *
     * @param userQuery
     *
     * @return
     */
    @GetMapping("/tenant/user/list")
    List<UserDTO> list(@SpringQueryMap UserQuery userQuery);

    /**
     * 查询用户列表
     *
     * @param userQuery
     *
     * @return
     */
    @PostMapping("/tenant/user/list")
    List<UserDTO> postList(@RequestBody @Valid UserQuery userQuery);

    /**
     * 职位下用户
     *
     * @param appointmentIds 职位主键
     *
     * @return
     */
    @GetMapping("/authenticated/appointment/user/list")
    List<UserDTO> getUserListByAppointmentIds(@RequestParam("appointmentIds") String appointmentIds);

    /**
     * 查询用户分页列表
     *
     * @param param
     *
     * @return
     */
    @PostMapping("/authenticated/user-under-tenant/page")
    Pagination<UserDTO> postListUsers(@Valid @RequestBody UserQueryParam param);

    /**
     * 逻辑删除用户
     *
     * @param mobile
     */
    @PutMapping("/authenticated/del/user/{mobile}")
    void deleteUser(@PathVariable("mobile") String mobile);

    /**
     * 逻辑删除用户
     *
     * @param id 主键
     */
    @PutMapping("/authenticated/remove/user/{id}")
    void removeUser(@PathVariable("id") Long id);

    /**
     * 判断用户是否属于某个部门、角色、岗位、职位
     *
     * @param userBelongsInstitutionParam
     */
    @GetMapping("/authenticated/user/belongs/institution")
    Boolean userBelongsInstitution(@SpringQueryMap UserBelongsInstitutionParam userBelongsInstitutionParam);

    @PostMapping("/tenant/user/map")
    Map<String, UserDTO> getTenantUserMap(@RequestBody @Valid UserMapQuery userQuery);

    /**
     * 获取租户系统内的用户列表
     *
     * @param userQuery
     *
     * @return
     */
    @PostMapping("/tenant/system/user/list")
    List<UserDTO> postSystemList(@RequestBody @Valid UserQuery userQuery);

    /**
     * 根据指定规则获取用户信息
     */
    @PostMapping("/tenant/user/find/rule")
    Map<Long, Set<Long>> findUserByRule(@RequestBody List<UserRuleConditionParam> ruleParam);
}
