package com.guhecloud.cube.biz.ouaa.server.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.ouaa.server.dto.UserOrganizationDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.UserOrganization;
import com.guhecloud.cube.biz.ouaa.server.param.UserQueryParam;
import com.guhecloud.cube.biz.ouaa.server.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Collection;
import java.util.List;

/**
 * 用户组织机构关联数据操作接口
 *
 * @author Woody
 * @date 2021/7/1
 */
@Mapper
public interface UserOrganizationMapper extends BaseMapper<UserOrganization> {

    /**
     * 根据用户主键获取相关的组织机构信息
     *
     * @param userIds 用户主键集合
     *
     * @return 组织机构列表
     */
    List<UserOrganizationDTO> listUserOrganizations(Collection<Long> userIds);

    /**
     * 根据组织机构主键获取用户组织关联信息
     *
     * @param organizationIds
     *
     * @return
     */
    List<UserOrganization> listOrganizationUsersByOrganizationIds(@Param("tenant") Integer tenant,
                                                                  @Param("organizationIds") Collection<Long> organizationIds
                                                                 );

    /**
     * 分页查询用户
     *
     * @param tenant 租户编号
     * @param param  查询参数
     *
     * @return 用户分页信息
     */
    Page<UserVO> listOrganizationUsers(Page<?> page, @Param("tenant") Integer tenant, @Param("param") UserQueryParam param);

    /**
     * 查询人员列表
     *
     * @param tenant
     * @param param
     *
     * @return
     */
    List<UserVO> listOrganizationUsers(@Param("tenant") Integer tenant, @Param("param") UserQueryParam param);

    /**
     * 通过部门和岗位ID 查询启用用户ID列表
     *
     * @param orgIds  组织机构ID
     * @param postIds 岗位ID
     *
     * @return 用户ID列表
     */
    List<Long> listEnableUserByOrgAndPost(@Param("orgIds") List<Long> orgIds, @Param("postIds") List<Long> postIds);

    /**
     * 通过部门和岗位ID 查询启用用户ID列表
     *
     * @param orgIds  组织机构ID
     * @param postIds 岗位ID
     *
     * @return 用户ID列表
     */
    List<Long> listUserByOrgAndPost(@Param("orgIds") List<Long> orgIds, @Param("postIds") List<Long> postIds,
                                    @Param("appointmentId") Long appointmentId
                                   );

    /**
     * 获取部门下用户总数
     *
     * @param orgIds 组织机构ID
     *
     * @return
     */
    Integer countUserOrganizations(@Param("orgIds") List<Long> orgIds, @Param("tenant") Integer tenant);

    /**
     * 获取岗位下用户总数
     *
     * @param appointmentIds 职位ID
     *
     * @return
     */
    Integer countUserAppointments(@Param("appointmentIds") List<Long> appointmentIds, @Param("tenant") Integer tenant);

    /**
     * 获取职位下用户总数
     *
     * @param positionIds
     * @param tenant
     *
     * @return
     */
    Integer countUserPositionIds(@Param("positionIds") List<Long> positionIds, @Param("tenant") Integer tenant);
}
