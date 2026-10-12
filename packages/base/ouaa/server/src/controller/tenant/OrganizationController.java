package com.guhecloud.cube.biz.ouaa.server.controller.tenant;

import com.guhecloud.cube.biz.ouaa.server.converters.OrganizationConverterForOrgVO;
import com.guhecloud.cube.biz.ouaa.server.entity.Organization;
import com.guhecloud.cube.biz.ouaa.server.entity.UserOrganization;
import com.guhecloud.cube.biz.ouaa.server.param.OrganizationModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.OrganizationQueryParam;
import com.guhecloud.cube.biz.ouaa.server.rocketmq.message.OperationOrganizationMessage;
import com.guhecloud.cube.biz.ouaa.server.service.EnterpriseExportService;
import com.guhecloud.cube.biz.ouaa.server.service.OrganizationService;
import com.guhecloud.cube.biz.ouaa.server.service.UserOrganizationService;
import com.guhecloud.cube.biz.ouaa.server.vo.OrganizationPostAndPositionVO;
import com.guhecloud.cube.biz.ouaa.server.vo.OrganizationVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.TreeUtils;
import com.guhecloud.platform.cloud.logger.constant.OperationType;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * 组织机构管理接口
 *
 * @author Woody
 * @date 2021/7/16
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrganizationController {
    private final OrganizationService organizationService;
    private final UserOrganizationService userOrganizationService;
    private final EnterpriseExportService enterpriseExportService;

    /**
     * 新增组织机构
     *
     * @param param 新增参数
     */
    @PostMapping("/organization")
    public void addOrganization(@RequestBody @Valid OrganizationModifyParam param) {
        Organization organization = this.organizationService.addOrganization(param);
        sendMessage(organization.getId(), OperationType.ADD);
    }

    private void sendMessage(Long orgId, String operation) {
        try {
            Organization organization = Organization.builder().id(orgId).build();
            if (!OperationType.DELETE.equals(operation)) {
                organization = organizationService.getById(orgId);
            }
            RocketMQContextHolder.send(OperationOrganizationMessage.TOPIC,
                                       OperationOrganizationMessage.builder()
                                               .id(organization.getId())
                                               .name(organization.getName())
                                               .remark(organization.getRemark())
                                               .enabled(organization.getEnabled())
                                               .level(organization.getLevel())
                                               .operationType(operation)
                                               .parentId(organization.getParentId())
                                               .orgPathId(organization.getSequence())
                                               .tenant(SessionContextHolder.getRequester().getTenant())
                                               .build()
                                      );

        } catch (Exception exception) {
            log.error("发送组织机构消息失败", exception);
        }
    }

    /**
     * 修改组织机构
     *
     * @param id    组织机构主键
     * @param param 更新参数
     */
    @PutMapping("/organization/{id}")
    public void updateOrganization(@PathVariable("id") @Min(value = 1, message = "组织机构主键不能小于1") Long id,
                                   @RequestBody @Valid OrganizationModifyParam param
                                  ) {
        Organization organization = this.organizationService.updateOrganization(id, param);
        sendMessage(organization.getId(), OperationType.UPDATE);
    }

    /**
     * 删除组织机构
     *
     * @param id 组织机构主键
     */
    @DeleteMapping("/organization/{id}")
    public void deleteOrganization(@PathVariable("id") Long id) {
        this.organizationService.deleteOrganization(id);
        sendMessage(id, OperationType.DELETE);
    }

    /**
     * 查询组织机构
     *
     * @param param 查询参数
     *
     * @return 应用列表
     */
    @GetMapping("/organizations")
    public List<OrganizationVO> listOrganizations(@Valid OrganizationQueryParam param) {
        // 获取组织机构列表
        var organizations = this.organizationService.listIntegralOrganizations(param);
        if (CollectionUtils.isEmpty(organizations)) {
            return Collections.emptyList();
        }
        // 统计组织机构人数
        var organizationMembers = this.userOrganizationService.listOrganizationUsers(
                        organizations.stream().map(Organization::getId).collect(Collectors.toSet())
                                                                                    ).stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new TreeSet<>(
                                Comparator.comparing(userOrganization -> userOrganization.getOrganizationId() + "-" +
                                                                         userOrganization.getUserId()))), ArrayList::new
                                                     )).stream()
                .collect(Collectors.groupingBy(UserOrganization::getOrganizationId, Collectors.counting()));
        var trees = TreeUtils.assembleTrees(OrganizationConverterForOrgVO.toOrganizationVOS(organizations));
        this.summaryOrganizationMembers(trees, organizationMembers);
        return trees;
    }

    /**
     * 查询组织机构列表
     *
     * @param param 查询参数
     *
     * @return 应用列表
     */
    @GetMapping("/organizations/list")
    public List<OrganizationVO> getOrganizations(@Valid OrganizationQueryParam param) {
        // 获取组织机构列表
        List<Organization> organizations = this.organizationService.listIntegralOrganizations(param);
        if (CollectionUtils.isEmpty(organizations)) {
            return Collections.emptyList();
        }
        return ObjectUtils.copying(organizations, OrganizationVO.class);
    }

    /**
     * 通过机构IDS 查询机构
     *
     * @param ids 查询参数
     *
     * @return 应用列表
     */
    @GetMapping("/organizations/ids/list")
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
    @GetMapping("/organizations/{id}")
    public OrganizationVO getOrganizationsById(@PathVariable("id") Long id) {
        // 获取组织机构列表
        Organization organizations = this.organizationService.getById(id);
        return ObjectUtils.copying(organizations, OrganizationVO.class);
    }

    /**
     * 汇总组织机构人数
     *
     * @param trees    组织机构树列表
     * @param mappings 组织机构/人数映射表
     */
    private void summaryOrganizationMembers(Collection<OrganizationVO> trees, Map<Long, Long> mappings) {
        if (!CollectionUtils.isEmpty(trees) && MapUtils.isNotEmpty(mappings)) {
            trees.forEach(organization -> {
                int members = ObjectUtils.ifNull(mappings.get(organization.getId()), 0L).intValue();
                organization.setMembers(members);
                this.summaryOrganizationMembers(organization.getChildren(), mappings);
                if (!CollectionUtils.isEmpty(organization.getChildren())) {
                    organization.setMembers(members +
                                            organization.getChildren().stream().mapToInt(OrganizationVO::getMembers).sum());
                }
            });
        }
    }

    /**
     * 根据部门id查询部门岗位和职位信息
     *
     * @param id 部门id
     *
     * @return 部门关联的岗位和职位
     */
    @GetMapping("/organizations/postAndPosition/{id}")
    public OrganizationPostAndPositionVO getPostAndPositionByOrganization(@PathVariable("id") Long id) {
        return organizationService.getPostAndPositionByOrganization(id);
    }

    /**
     * 部门导出
     *
     * @param param 筛选条件
     */
    @GetMapping("/organizations/export")
    public void export(@SpringQueryMap @Valid OrganizationQueryParam param) {
        enterpriseExportService.exportOrganizations(param);
    }
}
