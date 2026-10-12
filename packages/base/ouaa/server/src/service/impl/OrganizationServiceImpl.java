package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.OrganizationMapper;
import com.guhecloud.cube.biz.ouaa.server.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.server.entity.Organization;
import com.guhecloud.cube.biz.ouaa.server.entity.UserOrganization;
import com.guhecloud.cube.biz.ouaa.server.param.OrganizationModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.OrganizationQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.AppointmentQueryService;
import com.guhecloud.cube.biz.ouaa.server.service.OrganizationService;
import com.guhecloud.cube.biz.ouaa.server.service.PositionService;
import com.guhecloud.cube.biz.ouaa.server.service.UserOrganizationService;
import com.guhecloud.cube.biz.ouaa.server.vo.OrganizationPostAndPositionVO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.EntityUtils;
import jakarta.annotation.Resource;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * 组织机构业务操作实现
 *
 * @author Woody
 * @date 2021/7/1
 */
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization> implements OrganizationService {

    @Resource
    private UserOrganizationService userOrganizationService;
    @Resource
    private PositionService positionService;
    @Resource
    private AppointmentQueryService appointmentQueryService;

    @Override
    public boolean deleteOrganization(@NonNull Long id) {
        //查询子级部门
        List<Organization> organizations = this.baseMapper.selectList(Wrappers.<Organization>lambdaQuery()
                                                                              .like(Organization::getSequence, id.toString())
                                                                     );
        if (CollectionUtils.isEmpty(organizations)) {
            return true;
        }
        List<Long> orgIds = organizations.stream().map(Organization::getId).collect(Collectors.toList());
        Integer userCount = this.userOrganizationService.countUserOrganizations(orgIds);
        Assert.isNotAcceptable(0 != userCount, "删除失败,该部门下存在员工");
        return DatabaseContextHolder.softDelete(this, organizations);
    }

    @Override
    public Organization addOrganization(@NonNull OrganizationModifyParam param) {
        Assert.isParameterError(StringUtils.isBlank(param.getName()), "部门名称不能为空!");
        List<Organization> organizations = this.lambdaQuery().eq(Organization::getName, param.getName()).list();
        Assert.isConflict(CollectionUtils.isNotEmpty(organizations), "存在部门相同名称!");
        Organization organization = ObjectUtils.copying(param, Organization.class);
        organization.setSort(Optional.ofNullable(param.getSort()).orElse(0F));
        this.baseMapper.insert(organization);
        return organization;
    }

    @Override
    public Organization updateOrganization(@NonNull Long id, @NonNull OrganizationModifyParam param) {
        if (param.getParentId() != null && param.getParentId() == 0) {
            param.setParentId(null);
        }
        Organization organization = this.getById(id);
        param.setSort(organization.getSort());
        Assert.isNotFound(organization, "组织机构不存在");
        organization.setName(param.getName());
        organization.setSort(param.getSort());
        organization.setParentId(param.getParentId());
        organization.setEnabled(param.getEnabled());
        organization.setRemark(param.getRemark());
        this.updateById(organization);
        if (Objects.equals(Boolean.FALSE, param.getEnabled())) {
            //修改子级部门为禁用
            this.update(Wrappers.<Organization>lambdaUpdate()
                                .set(Organization::getEnabled, Boolean.FALSE)
                                .likeRight(Organization::getSequence, organization.getSequence())
                       );
        } else {
            if (Objects.nonNull(organization.getParentId())) {
                Organization parentOrganization = this.getById(organization.getParentId());
                if (Objects.nonNull(parentOrganization)) {
                    Assert.isNotAcceptable(Boolean.FALSE.equals(parentOrganization.getEnabled()), "父级部门为禁用状态,修改状态失败");
                }
            }
        }
        return organization;
    }

    @Override
    public List<Organization> listOrganizations(@NonNull Collection<String> sequences) {
        return sequences.isEmpty() ? Collections.emptyList() :
               this.lambdaQuery().in(Organization::getSequence, sequences).orderByAsc(Organization::getSort)
                       .orderByDesc(Organization::getId).list();
    }

    @Override
    public List<Organization> listOrganizations(@NonNull OrganizationQueryParam param) {
        List<Organization> organizations = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(param.getIds())) {
            param.getIds().forEach(id -> {
                List<Organization> organizationList = this.lambdaQuery()
                        .like(!StringUtils.isEmpty(param.getName()), Organization::getName, param.getName())
                        .eq(Objects.nonNull(param.getEnabled()), Organization::getEnabled, param.getEnabled())
                        .like(Objects.nonNull(param.getIds()), Organization::getSequence, id)
                        .orderByAsc(Organization::getSort).orderByDesc(Organization::getId).list();
                organizations.addAll(organizationList);
            });
        } else {
            return this.lambdaQuery()
                    .like(!StringUtils.isEmpty(param.getName()), Organization::getName, param.getName())
                    .eq(Objects.nonNull(param.getEnabled()), Organization::getEnabled, param.getEnabled())
                    .orderByAsc(Organization::getSort).orderByDesc(Organization::getId).list();
        }
        return organizations;
    }

    @Override
    public List<Organization> listIntegralOrganizations(@NonNull OrganizationQueryParam param) {
        if (CollectionUtils.isEmpty(param.getIds())) {
            if ((StringUtils.isEmpty(param.getName()) && param.getEnabled() == null)) {
                return this.lambdaQuery().eq(Objects.nonNull(param.getEnabled()), Organization::getEnabled, param.getEnabled())
                        .orderByAsc(Organization::getSort).orderByDesc(Organization::getId).list();
            }
        }
        List<Organization> organizations = this.listOrganizations(param);
        List<String> sequences = EntityUtils.getAllSequences(organizations);
        return this.listOrganizations(sequences);
    }

    @Override
    public List<Organization> listOrganizations(@NonNull List<Long> ids) {
        return ids.isEmpty() ? Collections.emptyList() :
               this.lambdaQuery().in(Organization::getId, ids).orderByAsc(Organization::getLevel).list();
    }

    @Override
    public List<Organization> listOrganizationByDepts(@NonNull Collection<String> depts) {
        return CollectionUtils.isEmpty(depts) ? Collections.emptyList() :
               this.baseMapper.listOrganizations(depts);
    }

    @Override
    public Set<Long> getAllSubOrganization(Long organizationId) {
        List<Organization> list = recursionQuerySubOrganization(organizationId);
        return list.stream().map(Organization::getId).collect(Collectors.toSet());
    }

    /**
     * 递归所有子部门
     *
     * @param organizationId 部门id
     *
     * @return 子部门id集合
     */
    public List<Organization> recursionQuerySubOrganization(Long organizationId) {
        List<Organization> list = new CopyOnWriteArrayList<>(this.lambdaQuery().eq(Organization::getParentId, organizationId).list());
        if (ObjectUtils.isEmpty(list)) {
            return Collections.emptyList();
        } else {
            for (Organization organization : list) {
                list.addAll(recursionQuerySubOrganization(organization.getId()));
            }
        }
        return list;
    }

    @Override
    public List<UserDTO.Organization> getAllParentOrganization(UserDTO.Organization organization) {
        List<Organization> list = new CopyOnWriteArrayList<>(this.lambdaQuery().eq(Organization::getId, organization.getId()).list());
        list.addAll(recursionQueryParentOrganization(organization.getParentId()));
        Collections.reverse(list);
        return ObjectUtils.copying(list, UserDTO.Organization.class);
    }

    /**
     * 递归查所有父部门
     *
     * @param parentId 父级部门id
     *
     * @return 所有父级部门信息
     */
    private List<Organization> recursionQueryParentOrganization(Long parentId) {
        List<Organization> list = parentId == null ? Collections.emptyList()
                                                   : new CopyOnWriteArrayList<>(this.lambdaQuery().eq(Organization::getId, parentId)
                                                                                        .list());
        if (ObjectUtils.isNotNull(parentId)) {
            for (Organization organization : list) {
                list.addAll(recursionQueryParentOrganization(organization.getParentId()));
            }
        }
        return list;
    }

    @Override
    public OrganizationPostAndPositionVO getPostAndPositionByOrganization(Long id) {
        OrganizationPostAndPositionVO vo = new OrganizationPostAndPositionVO();
        List<UserOrganization> organizations = userOrganizationService.listOrganizationUsers(id);
        List<Long> positions = organizations.stream().map(UserOrganization::getPositionId).distinct().collect(Collectors.toList());
        List<Long> appointments = organizations.stream().map(UserOrganization::getAppointmentId).distinct().collect(Collectors.toList());
        vo.setPositionList(positionService.listPositions(positions));
        vo.setPostList(appointmentQueryService.listAppointment(appointments));
        return vo;
    }

    @Override
    public List<Organization> getAllOrganization() {
        return this.list();
    }
}
