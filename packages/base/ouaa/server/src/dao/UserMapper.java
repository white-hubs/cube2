package com.guhecloud.cube.biz.ouaa.server.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.ouaa.server.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.User;
import com.guhecloud.cube.biz.ouaa.server.param.UserCenterQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.query.SysUserQuery;
import com.guhecloud.cube.biz.ouaa.server.param.query.UserQuery;
import com.guhecloud.cube.biz.ouaa.server.param.query.UsersByRolesQuery;
import com.guhecloud.cube.biz.ouaa.server.vo.UserVO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * 用户数据操作接口
 *
 * @author Woody
 * @date 2021/6/29
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
    /**
     * 获取当前租户下的管理员
     *
     * @return 用户
     */
    User getAdmin(@Param("tenant") Integer tenant);

    /**
     * 根据用户唯一标识（如账号、手机号等）查找用户
     *
     * @param key 用户唯一标识
     *
     * @return 用户
     */
    User lookupUser(@Param("key") String key);

    /**
     * 岗位下的用户列表
     *
     * @param positionId
     *
     * @return
     */
    List<UserDTO> positionUsers(@Param("positionId") Long positionId);

    /**
     * 组织架构下的用户列表
     *
     * @param organizationId
     *
     * @return
     */
    List<UserDTO> organizationUsers(@Param("organizationId") Long organizationId);

    /**
     * 组织架构下的岗位用户列表
     *
     * @param organizationId
     * @param positionId
     *
     * @return
     */
    List<UserDTO> organizationAndPostUsers(@Param("organizationId") Long organizationId, @Param("positionId") Long positionId);

    /**
     * 组织架构下的岗位启动的用户列表
     *
     * @param organizationId
     * @param positionId
     *
     * @return
     */
    List<UserDTO> organizationAndPostEnabledUsers(@Param("organizationId") Long organizationId, @Param("positionId") Long positionId);

    /**
     * 组织架构下的启用用户列表
     *
     * @param organizationId
     *
     * @return
     */
    List<UserDTO> organizationEnableUsers(@Param("organizationId") Long organizationId);

    /**
     * 注册或修改用户并返回是否注册成功
     *
     * @param user 用户信息
     *
     * @return true/false
     */
    boolean registerOrUpdate(User user);

    String getUserPostName(@Param("id") Long id);

    String getUserOrganizationName(@Param("id") Long id);

    /**
     * 查询角色关联的用户
     *
     * @param roleIds
     * @param keyword
     *
     * @return
     */
    List<UserDTO> selectUsersByRoles(@Param("tenant") Integer tenant, @Param("roleIds") @NotEmpty Set<Long> roleIds,
                                     @Param("keyword") String keyword
                                    );

    @InterceptorIgnore(tenantLine = "on")
    List<UserDTO> selectUsers(@Param("query") UserQuery userQuery, @Param("tenant") @NotNull Integer tenant);

    @InterceptorIgnore(tenantLine = "on")
    List<UserDTO> selectSystemUsers(@Param("query") UserQuery userQuery, @Param("tenant") @NotNull Integer tenant);

    List<UserDTO> getUserListByAppointmentIds(@Param("appointmentIds") List<Long> appointmentIds);

    /**
     * @Author wql
     * @Description 按用户角色id分页查用户信息
     * @Date 19:57 2023/11/30
     **/
    Page<UserDTO> pageUsersByRoles(Page<UserDTO> page, @Param("query") UsersByRolesQuery query);

    /**
     * 用户中心查询用户信息
     *
     * @param param 查询参数
     *
     * @return 用户分页信息
     */
    Page<UserVO> selectRegisterInfoPage(Page<?> page, @Param("param") UserCenterQueryParam param);

    /**
     * 查询系统用户分页列表
     *
     * @param page
     * @param query
     *
     * @return
     */
    Page<User> getSysUsers(Page<?> page, @Param("query") SysUserQuery query);

    /**
     * 根据身份证号查询当前租户下用户
     *
     * @param idCard
     * @param tenant
     *
     * @return
     */
    Long getUserByIdCard(@Param("idCard") String idCard, @Param("tenant") Integer tenant);

}
