package com.guhecloud.cube.srv.gen.business.info.sync.server.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.business.info.sync.server.entity.Organization;
import com.guhecloud.cube.srv.gen.business.info.sync.server.rocketmq.message.organization.OperationOrganizationMessage;
import com.guhecloud.cube.srv.gen.business.info.sync.server.service.SyncOrganizationService;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2024/12/18 10:07
 * @Description:
 */

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SyncOrganizationInfoOperationHandler {
    private final SyncOrganizationService organizationService;

    @Transactional(rollbackFor = Exception.class)
    public Long insert(OperationOrganizationMessage message) {
        Organization organization = organizationService
                .lambdaQuery().eq(Organization::getId, message.getId())
                .eq(Organization::getDeleted, 0)
                .one();
        if (organization != null) {
            this.update(message);
        } else {
            Organization organization1 = ObjectUtils.copying(message, Organization.class);
            List<Long> orgIds = Arrays.stream(message.getOrgPathId().split("-"))
                    .map(Long::valueOf).distinct().collect(Collectors.toList());

            orgIds.remove(orgIds.size() - 1);
            if (!CollectionUtils.isEmpty(orgIds)) {
                Map<Long, String> orgMap = organizationService
                        .lambdaQuery()
                        .select(Organization::getName, Organization::getId)
                        .in(Organization::getId, orgIds)
                        .list()
                        .stream().collect(Collectors.toMap(Organization::getId, Organization::getName, (k1, k2) -> k1));
                StringBuilder orgPathName = new StringBuilder();
                for (Long orgId : orgIds) {
                    if (StringUtils.isNotBlank(orgPathName)) {
                        orgPathName.append("-");
                    }
                    if (orgMap.containsKey(orgId)) {
                        orgPathName.append(orgMap.get(orgId));
                    }
                }
                orgPathName.append("-").append(message.getName());
                organization1.setOrgPathName(orgPathName.toString());
            } else {
                organization1.setOrgPathName(message.getName());
            }
            organizationService.save(organization1);
        }
        return message.getId();
    }

    @Transactional(rollbackFor = Exception.class)
    public Long update(OperationOrganizationMessage message) {
        Organization organization = organizationService
                .lambdaQuery().eq(Organization::getId, message.getId())
                .eq(Organization::getDeleted, 0)
                .one();
        if (Objects.isNull(organization)) {
            log.error("organization not found, id: {}", message.getId());
            this.insert(message);
            return message.getId();
        }
        organizationService.lambdaUpdate()
                .eq(Organization::getId, message.getId())
                .set(StringUtils.isNotBlank(message.getName()), Organization::getName, message.getName())
                .set(StringUtils.isNotBlank(message.getRemark()), Organization::getRemark, message.getRemark())
                .set(Objects.nonNull(message.getEnabled()), Organization::getEnabled, message.getEnabled())
                .update();
        if (Objects.nonNull(message.getEnabled()) && !message.getEnabled()) {
            //修改子级部门为禁用
            organizationService.update(Wrappers.<Organization>lambdaUpdate()
                                               .set(Organization::getEnabled, Boolean.FALSE)
                                               .eq(Organization::getParentId, message.getId())
                                      );
        }
        //修改path_name
        List<Organization> organizationList = organizationService.lambdaQuery()
                .like(Organization::getOrgPathId, message.getId())
                .list();
        if (!CollectionUtils.isEmpty(organizationList)) {
            for (Organization organization1 : organizationList) {
                String orgPathId = organization1.getOrgPathId();
                String[] split = orgPathId.split("-");
                String[] orgName = organization1.getOrgPathName().split("-");
                for (int i = 0; i < split.length; i++) {
                    if (Objects.equals(message.getId(), Long.valueOf(split[i]))) {
                        orgName[i] = message.getName();
                    }
                }
                organization1.setOrgPathName(String.join("-", orgName));
            }
            organizationService.updateBatchById(organizationList);
        }
        return message.getId();

    }

    @Transactional(rollbackFor = Exception.class)
    public Long delete(OperationOrganizationMessage message) {
        long timeMillis = System.currentTimeMillis();
        organizationService.lambdaUpdate()
                .eq(Organization::getId, message.getId())
                .set(Organization::getDeleted, timeMillis)
                .update();
        //查询子级部门
        List<Organization> organizations = this.organizationService.lambdaQuery()
                .eq(Organization::getDeleted, 0)
                .eq(Organization::getParentId, message.getId()).list();
        if (CollectionUtils.isEmpty(organizations)) {
            return message.getId();
        }

        organizationService.lambdaUpdate()
                .eq(Organization::getParentId, message.getId())
                .set(Organization::getDeleted, timeMillis)
                .update();
        return message.getId();
    }
}
