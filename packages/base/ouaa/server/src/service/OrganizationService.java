package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.Organization;
import com.guhecloud.cube.biz.ouaa.server.param.OrganizationModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.OrganizationQueryParam;
import com.guhecloud.cube.biz.ouaa.server.vo.OrganizationPostAndPositionVO;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 组织机构业务操作接口
 *
 * @author Woody
 * @date 2021/7/1
 */
public interface OrganizationService extends IService<Organization> {
    /**
     * 删除组织机构并返回是否删除成功
     *
     * @param id 组织机构主键
     *
     * @return true/false
     */
    boolean deleteOrganization(Long id);

    /**
     * 新增组织机构
     *
     * @param param 新增参数
     *
     * @return 组织机构实例
     */
    Organization addOrganization(OrganizationModifyParam param);

    /**
     * 修改组织机构
     *
     * @param id    组织机构主键
     * @param param 修改参数
     *
     * @return 组织机构实例
     */
    Organization updateOrganization(Long id, OrganizationModifyParam param);

    /**
     * 查询组织机构列表
     *
     * @param sequences 序列号集合
     *
     * @return 组织机构列表
     */
    List<Organization> listOrganizations(Collection<String> sequences);

    /**
     * 查询组织机构列表
     *
     * @param param 查询参数
     *
     * @return 组织机构列表
     */
    List<Organization> listOrganizations(OrganizationQueryParam param);

    /**
     * 获取完整的组织机构列表（完整的层级关系）
     *
     * @param param 查询参数
     *
     * @return 组织机构列表
     */
    List<Organization> listIntegralOrganizations(OrganizationQueryParam param);

    /**
     * 根据主键获取组织机构信息
     *
     * @param ids 主键集合
     *
     * @return 组织机构列表
     */
    List<Organization> listOrganizations(List<Long> ids);

    List<Organization> listOrganizationByDepts(Collection<String> depts);

    /**
     * 查询部门的所有子部门id
     *
     * @param organizationId 部门id
     *
     * @return 子部门id集合
     */
    Set<Long> getAllSubOrganization(Long organizationId);

    /**
     * 查询部门
     *
     * @param organization 部门信息
     *
     * @return 部门及其父级部门信息
     */
    List<UserDTO.Organization> getAllParentOrganization(UserDTO.Organization organization);

    /**
     * 根据部门id查询部门岗位和职位信息
     *
     * @param id 部门id
     *
     * @return 部门关联的岗位和职位
     */
    OrganizationPostAndPositionVO getPostAndPositionByOrganization(Long id);

    List<Organization> getAllOrganization();
}
