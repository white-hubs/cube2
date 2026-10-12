package com.guhecloud.cube.biz.ouaa.server.controller.authenticated;

import com.guhecloud.cube.biz.ouaa.server.converters.OrganizationConverterForOrgVO;
import com.guhecloud.cube.biz.ouaa.server.dao.OrganizationMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.Organization;
import com.guhecloud.cube.biz.ouaa.server.param.OrganizationQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.OrganizationService;
import com.guhecloud.cube.biz.ouaa.server.vo.OrganizationVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.TreeUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 组织机构管理接口
 *
 * @author Woody
 * @date 2021/7/16
 */
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@PreAuthorize("isAuthenticated()")
public class AuthenticatedOrganizationController {
    private final OrganizationService organizationService;
    private final OrganizationMapper organizationMapper;

    /**
     * 查询组织机构列表
     *
     * @param param 查询参数
     *
     * @return 应用列表
     */
    @GetMapping("/authenticated/organizations/list")
    public List<OrganizationVO> getOrganizations(@Valid OrganizationQueryParam param) {
        // 获取组织机构列表
        List<Organization> organizations = this.organizationService.listIntegralOrganizations(param);
        if (CollectionUtils.isEmpty(organizations)) {
            return Collections.emptyList();
        }
        return ObjectUtils.copying(organizations, OrganizationVO.class);
    }

    /**
     * 获取机构信息
     *
     * @param depts 部门集合
     *
     * @return 机构集合
     */
    @GetMapping("/authenticated/organizations/depts/list")
    List<OrganizationVO> listOrganizationByDepts(@RequestParam("depts") Collection<String> depts) {
        List<Organization> organizations = this.organizationService.listOrganizationByDepts(depts);
        return ObjectUtils.copying(organizations, OrganizationVO.class);
    }

    /**
     * 查询组织机构
     *
     * @param param 查询参数
     *
     * @return 应用列表
     */
    @GetMapping("/authenticated/organizations")
    public List<OrganizationVO> listOrganizations(@Valid OrganizationQueryParam param) {
        // 获取组织机构列表
        var organizations = this.organizationService.listIntegralOrganizations(param);
        if (CollectionUtils.isEmpty(organizations)) {
            return Collections.emptyList();
        }
        return TreeUtils.assembleTrees(OrganizationConverterForOrgVO.toOrganizationVOS(organizations));
    }

    /**
     * 通过机构IDS 查询机构
     *
     * @param ids 查询参数
     *
     * @return 应用列表
     */
    @GetMapping("/authenticated/organizations/ids/list")
    public List<OrganizationVO> getOrganizationsByIds(@RequestParam(value = "ids") List<Long> ids) {
        // 获取组织机构列表
        List<Organization> organizations = this.organizationService.lambdaQuery().in(Organization::getId, ids).list();
        if (CollectionUtils.isEmpty(organizations)) {
            return Collections.emptyList();
        }
        return ObjectUtils.copying(organizations, OrganizationVO.class);
    }

    /**
     * 通过ID 获取组织机构
     *
     * @param id
     *
     * @return
     */
    @GetMapping("/authenticated/organizations/{id}")
    public OrganizationVO getOrganizationsById(@PathVariable("id") Long id) {
        // 获取组织机构列表
        Organization organizations = this.organizationService.getById(id);
        return ObjectUtils.copying(organizations, OrganizationVO.class);
    }

    @GetMapping("/authenticated/organizations/parent/list/{id}")
    public LinkedList<OrganizationVO> getParentListById(@PathVariable("id") Long id) {
        return organizationMapper.parentListById(id);
    }

    /**
     * 通过名称查询部门
     *
     * @param name
     *
     * @return
     */
    @GetMapping("/authenticated/organizations/name")
    public OrganizationVO getOrganizationsByName(@RequestParam(value = "name") String name) {
        Organization organization = organizationService.lambdaQuery().eq(Organization::getName, name).one();
        return ObjectUtils.copying(organization, OrganizationVO.class);
    }

    @PostMapping("/authenticated/organizations/map/ids")
    public Map<Long, OrganizationVO> postOrganizationsMapByIds(@RequestBody OrganizationQueryParam param) {
        Map<Long, OrganizationVO> result = new HashMap<>();
        var list = this.getOrganizationsListByIds(param);
        Optional.ofNullable(list).orElse(Collections.emptyList()).forEach(o -> result.put(o.getId(), o));
        return result;
    }

    @PostMapping("/authenticated/organizations/list/ids")
    List<OrganizationVO> getOrganizationsListByIds(@RequestBody OrganizationQueryParam param) {
        List<Organization> organizations = this.organizationService.lambdaQuery().in(Organization::getId, param.getIds()).list();
        if (CollectionUtils.isEmpty(organizations)) {
            return Collections.emptyList();
        }
        return ObjectUtils.copying(organizations, OrganizationVO.class);
    }

}
