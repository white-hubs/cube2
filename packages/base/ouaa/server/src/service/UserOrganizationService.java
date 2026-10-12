package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.dto.UserOrganizationDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.UserOrganization;
import com.guhecloud.cube.biz.ouaa.server.param.UserQueryParam;
import com.guhecloud.cube.biz.ouaa.server.vo.UserVO;

import java.util.Collection;
import java.util.List;

/**
 * 用户组织机构关联业务操作接口
 *
 * @author Woody
 * @date 2021/7/1
 */
public interface UserOrganizationService extends IService<UserOrganization> {
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
     * @param organizationIds 组织机构主键集合
     *
     * @return 用户组织关联信息列表
     */
    List<UserOrganization> listOrganizationUsers(Collection<Long> organizationIds);

    /**
     * 根据组织机构主键获取用户组织关联信息
     *
     * @param organizationId 组织机构主键
     *
     * @return 用户组织关联信息列表
     */
    List<UserOrganization> listOrganizationUsers(Long organizationId);

    /**
     * 根据岗位主键获取用户岗位关联信息
     *
     * @param positionId 岗位主键
     *
     * @return 用户岗位关联信息列表
     */
    List<UserOrganization> listPositionUsers(Long positionId);

    /**
     * 分页查询用户
     *
     * @param param 查询参数
     *
     * @return 用户分页信息
     */
    Page<UserVO> listOrganizationUsers(UserQueryParam param);

    /**
     * 人员列表
     *
     * @param param
     *
     * @return
     */
    List<UserVO> userList(UserQueryParam param);

    /**
     * 通过部门和岗位ID 查询启用用户ID列表
     *
     * @param orgIds  组织机构ID
     * @param postIds 岗位ID
     *
     * @return 用户ID列表
     */
    List<Long> listEnableUserByOrgAndPost(List<Long> orgIds, List<Long> postIds);

    /**
     * 通过部门和岗位ID 查询启用用户ID列表
     *
     * @param orgIds  组织机构ID
     * @param postIds 岗位ID
     *
     * @return 用户ID列表
     */
    List<Long> listUserByOrgAndPost(List<Long> orgIds, List<Long> postIds, Long appointmentId);

    List<UserVO> encryptPhone(List<UserVO> users);

    /**
     * 获取部门下的用户数量
     *
     * @param orgIds 组织机构ID
     *
     * @return
     */
    Integer countUserOrganizations(List<Long> orgIds);

    /**
     * 获取岗位下的用户数量
     *
     * @param appointmentIds 职位ID
     *
     * @return
     */
    Integer countUserAppointments(List<Long> appointmentIds);

    /**
     * 获取职位下的用户数量
     *
     * @param positionIds
     *
     * @return
     */
    Integer countUserPositions(List<Long> positionIds);

}
