package com.guhecloud.cube.biz.collection.analysis.gm.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.biz.collection.analysis.gm.client.GmUserSyncApiClient;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.GmThirdDeptTreeDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.model.dto.GmThirdUserDTO;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.mapper.GmUserSyncConfigMapper;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.mapper.GmUserSyncFailedRecordMapper;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.GmUserSyncConfigPO;
import com.guhecloud.cube.biz.collection.analysis.gm.repository.po.GmUserSyncFailedRecordPO;
import com.guhecloud.cube.biz.collection.analysis.gm.service.GmUserSyncService;
import com.guhecloud.cube.biz.ouaa.client.OrganizationClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.UserTenantClient;
import com.guhecloud.cube.biz.ouaa.client.dto.OrganizationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.OrganizationModifyParam;
import com.guhecloud.cube.biz.ouaa.client.param.RenewUserTenantEnabledBatchParam;
import com.guhecloud.cube.biz.ouaa.client.param.UserModifyParam;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.Gender;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import com.guhecloud.platform.security.model.Principal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 光明人员同步服务实现
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GmUserSyncServiceImpl implements GmUserSyncService {
    private static final int MAX_DEPT_TREE_DEPTH = 10;
    private static final int MAX_ERROR_MESSAGE_LENGTH = 1000;
    private final GmUserSyncConfigMapper configMapper;
    private final GmUserSyncFailedRecordMapper failedRecordMapper;
    private final GmUserSyncApiClient gmUserSyncApiClient;
    private final UserClient userClient;
    private final UserTenantClient userTenantClient;
    private final OrganizationClient organizationClient;

    @Override
    public void sync() {
        Integer tenant = SessionContextHolder.getRequester().getTenant();
        Assert.isNotAcceptable(tenant == null, "光明人员同步租户不能为空");
        LocalDateTime syncEndTime = LocalDateTime.now().withNano(0);
        GmUserSyncConfigPO config = loadConfig(tenant);
        if (config == null) {
            log.warn("光明人员同步配置不存在或未启用，跳过 tenant={}", tenant);
            return;
        }
        SecurityContextHolder.setPrincipal(Principal.builder().id(1L).account("1").name("system").type(Principal.Type.CLIENT).build());
        Map<String, OrganizationDTO> organizationCache = new HashMap<>();
        DeptTreeIndex deptTreeIndex = new DeptTreeIndex();
        try {
            /* 同步部门 */
            deptTreeIndex = syncDepartments(config, syncEndTime, organizationCache);
        } catch (Exception e) {
            log.error("光明部门同步失败：", e);
        }
        /* 同步人员 */
        List<GmThirdUserDTO> remoteUsers = gmUserSyncApiClient.listAllUsers(config, syncEndTime);
        if (CollectionUtils.isEmpty(remoteUsers)) {
            log.info("光明人员同步三方人员为空，跳过 tenant={}", tenant);
            updateLastSyncTime(config, tenant, syncEndTime);
            return;
        }
        SyncStats stats = new SyncStats(remoteUsers.size());
        List<GmThirdUserDTO> candidates = filterCandidates(remoteUsers, stats);
        Map<String, UserDTO> localUserMap = loadLocalUsers(candidates);
        TenantEnabledCollector tenantEnabledCollector = new TenantEnabledCollector();
        for (GmThirdUserDTO remoteUser : candidates) {
            syncOne(remoteUser, localUserMap, organizationCache, deptTreeIndex, stats, tenantEnabledCollector, tenant,
                    config.getLastSyncTime(), syncEndTime);
        }
        batchUpdateTenantEnabled(tenant, tenantEnabledCollector);
        updateLastSyncTime(config, tenant, syncEndTime);
        log.info("光明人员同步完成 tenant={} total={} candidates={} created={} updated={} skipped={} failed={}",
                 tenant, stats.total, stats.candidates, stats.created, stats.updated, stats.skipped, stats.failed);
    }

    private GmUserSyncConfigPO loadConfig(Integer tenant) {
        return configMapper.selectOne(Wrappers.<GmUserSyncConfigPO>lambdaQuery()
                                              .eq(GmUserSyncConfigPO::getTenant, tenant)
                                              .eq(GmUserSyncConfigPO::getEnabled, Boolean.TRUE)
                                              .last("limit 1"));
    }

    private DeptTreeIndex syncDepartments(GmUserSyncConfigPO config, LocalDateTime syncEndTime,
                                          Map<String, OrganizationDTO> organizationCache
                                         ) {
        List<GmThirdDeptTreeDTO> changedDeptTree = gmUserSyncApiClient.listChangedDeptTree(config, syncEndTime);
        if (CollectionUtils.isEmpty(changedDeptTree)) {
            log.info("光明部门同步无变更，跳过部门更新 tenant={}", config.getTenant());
            return DeptTreeIndex.empty();
        }
        List<GmThirdDeptTreeDTO> fullDeptTree = gmUserSyncApiClient.listFullDeptTree(config);
        DeptTreeIndex deptTreeIndex = DeptTreeIndex.of(fullDeptTree);
        for (GmThirdDeptTreeDTO deptNode : deptTreeIndex.nodes()) {
            findOrCreateOrganizationByTreeNode(deptNode, organizationCache, deptTreeIndex, new HashSet<>(), 1);
        }
        log.info("光明部门同步完成 tenant={} changedRoots={} fullDeptCount={}", config.getTenant(), changedDeptTree.size(),
                 deptTreeIndex.size());
        return deptTreeIndex;
    }

    private void updateLastSyncTime(GmUserSyncConfigPO config, Integer tenant, LocalDateTime syncEndTime) {
        configMapper.update(null, Wrappers.<GmUserSyncConfigPO>lambdaUpdate()
                .eq(GmUserSyncConfigPO::getId, config.getId())
                .eq(GmUserSyncConfigPO::getTenant, tenant)
                .set(GmUserSyncConfigPO::getLastSyncTime, syncEndTime));
    }

    private List<GmThirdUserDTO> filterCandidates(List<GmThirdUserDTO> remoteUsers, SyncStats stats) {
        List<GmThirdUserDTO> candidates = new ArrayList<>();
        for (GmThirdUserDTO user : remoteUsers) {
            if (StringUtils.isBlank(readLeader(user))) {
                stats.skipped++;
                log.debug("光明人员同步跳过 leader 为空人员 userId={} mobile={}", user.getUserId(), user.getPhonenumber());
                continue;
            }
            if (StringUtils.isBlank(user.getPhonenumber())) {
                stats.skipped++;
                log.debug("光明人员同步跳过手机号为空人员 userId={}", user.getUserId());
                continue;
            }
            if (StringUtils.isBlank(readDeptName(user))) {
                stats.skipped++;
                log.debug("光明人员同步跳过部门为空人员 userId={} mobile={}", user.getUserId(), user.getPhonenumber());
                continue;
            }
            candidates.add(user);
        }
        stats.candidates = candidates.size();
        return candidates;
    }

    private Map<String, UserDTO> loadLocalUsers(List<GmThirdUserDTO> candidates) {
        Set<String> mobiles = candidates.stream()
                .map(GmThirdUserDTO::getPhonenumber)
                .filter(StringUtils::isNotBlank)
                .collect(Collectors.toCollection(HashSet::new));
        if (CollectionUtils.isEmpty(mobiles)) {
            return new HashMap<>();
        }
        List<UserDTO> localUsers = userClient.postList(UserQuery.builder().mobileList(mobiles).build());
        if (CollectionUtils.isEmpty(localUsers)) {
            return new HashMap<>();
        }
        return localUsers.stream()
                .filter(user -> StringUtils.isNotBlank(user.getMobile()))
                .collect(Collectors.toMap(UserDTO::getMobile, user -> user, (left, right) -> left));
    }

    private void syncOne(GmThirdUserDTO remoteUser, Map<String, UserDTO> localUserMap,
                         Map<String, OrganizationDTO> organizationCache, DeptTreeIndex deptTreeIndex, SyncStats stats,
                         TenantEnabledCollector tenantEnabledCollector, Integer tenant, LocalDateTime syncBeginTime,
                         LocalDateTime syncEndTime
                        ) {
        try {
            OrganizationDTO organization = findOrCreateOrganization(remoteUser, organizationCache, deptTreeIndex);
            if (organization == null || organization.getId() == null) {
                stats.failed++;
                String message = "部门创建或查询失败";
                log.warn("光明人员同步{} deptName={} userId={} mobile={}",
                         message, readDeptName(remoteUser), remoteUser.getUserId(), remoteUser.getPhonenumber());
                recordFailedUser(tenant, remoteUser, syncBeginTime, syncEndTime, message, null);
                return;
            }
            String name = readName(remoteUser);
            if (StringUtils.isBlank(name)) {
                stats.skipped++;
                log.debug("光明人员同步跳过姓名为空人员 userId={} mobile={}", remoteUser.getUserId(), remoteUser.getPhonenumber());
                return;
            }
            UserDTO localUser = localUserMap.get(remoteUser.getPhonenumber());
            if (localUser == null || localUser.getId() == null) {
                UserModifyParam param = buildUserCreateParam(remoteUser, name, organization.getId());
                Long userId = userClient.saveOfNormalByTenant(param);
                localUserMap.put(remoteUser.getPhonenumber(), buildCreatedUser(userId, param));
                tenantEnabledCollector.add(userId, readTenantEnabled(remoteUser));
                stats.created++;
                return;
            }
            if (!needsUpdate(localUser, remoteUser, name, organization.getId())) {
                tenantEnabledCollector.add(localUser.getId(), readTenantEnabled(remoteUser));
                stats.skipped++;
                return;
            }
            UserModifyParam param = buildUserModifyParam(localUser, remoteUser, name, organization.getId());
            userClient.updateOfNormalByTenant(localUser.getId(), param);
            tenantEnabledCollector.add(localUser.getId(), readTenantEnabled(remoteUser));
            stats.updated++;
        } catch (Exception e) {
            stats.failed++;
            log.error("光明人员同步单用户失败 userId={} mobile={}", remoteUser.getUserId(), remoteUser.getPhonenumber(), e);
            recordFailedUser(tenant, remoteUser, syncBeginTime, syncEndTime, e.getMessage(), e);
        }
    }

    private OrganizationDTO findOrCreateOrganization(GmThirdUserDTO remoteUser, Map<String, OrganizationDTO> organizationCache,
                                                     DeptTreeIndex deptTreeIndex
                                                    ) {
        Long thirdDeptId = readDeptId(remoteUser);
        GmThirdDeptTreeDTO deptNode = thirdDeptId == null ? null : deptTreeIndex.get(thirdDeptId);
        if (deptNode != null) {
            return findOrCreateOrganizationByTreeNode(deptNode, organizationCache, deptTreeIndex, new HashSet<>(), 1);
        }
        return findOrCreateOrganizationByName(readDeptName(remoteUser), null, organizationCache, false);
    }

    private OrganizationDTO findOrCreateOrganizationByTreeNode(GmThirdDeptTreeDTO deptNode,
                                                               Map<String, OrganizationDTO> organizationCache,
                                                               DeptTreeIndex deptTreeIndex,
                                                               Set<Long> visitingDeptIds,
                                                               int depth
                                                              ) {
        if (deptNode == null || StringUtils.isBlank(deptNode.getLabel())) {
            return null;
        }
        if (depth > MAX_DEPT_TREE_DEPTH) {
            log.warn("光明人员同步部门树层级超过上限，按部门名平铺创建 deptId={} deptName={} depth={}",
                     deptNode.getId(), deptNode.getLabel(), depth);
            return findOrCreateOrganizationByName(deptNode.getLabel(), null, organizationCache, false);
        }
        if (deptNode.getId() != null && !visitingDeptIds.add(deptNode.getId())) {
            log.warn("光明人员同步部门树存在循环引用，按部门名平铺创建 deptId={} deptName={}", deptNode.getId(), deptNode.getLabel());
            return findOrCreateOrganizationByName(deptNode.getLabel(), null, organizationCache, false);
        }
        Long parentOrganizationId = null;
        GmThirdDeptTreeDTO parent = deptTreeIndex.getParent(deptNode.getId());
        if (parent != null) {
            OrganizationDTO parentOrganization = findOrCreateOrganizationByTreeNode(parent, organizationCache, deptTreeIndex,
                                                                                    visitingDeptIds, depth + 1);
            if (parentOrganization == null || parentOrganization.getId() == null) {
                return findOrCreateOrganizationByName(deptNode.getLabel(), null, organizationCache, false);
            }
            parentOrganizationId = parentOrganization.getId();
        }
        return findOrCreateOrganizationByName(deptNode.getLabel(), parentOrganizationId, organizationCache, true);
    }

    private OrganizationDTO findOrCreateOrganizationByName(String deptName, Long parentId, Map<String, OrganizationDTO> organizationCache,
                                                           boolean updateExisting
                                                          ) {
        if (organizationCache.containsKey(deptName)) {
            return organizationCache.get(deptName);
        }
        OrganizationDTO organization = organizationClient.getOrganizationsByName(deptName);
        if (organization == null || organization.getId() == null) {
            OrganizationModifyParam param = new OrganizationModifyParam();
            param.setName(deptName);
            param.setParentId(parentId);
            param.setEnabled(Boolean.TRUE);
            param.setSort(0F);
            try {
                organizationClient.addOrganization(param);
            } catch (Exception e) {
                log.warn("光明人员同步创建部门失败，尝试重新查询 deptName={}", deptName, e);
            }
            organization = organizationClient.getOrganizationsByName(deptName);
        }
        if (updateExisting && organization != null && organization.getId() != null
            && (!Objects.equals(organization.getParentId(), parentId) || !Boolean.TRUE.equals(organization.getEnabled()))) {
            OrganizationModifyParam param = new OrganizationModifyParam();
            param.setName(deptName);
            param.setParentId(parentId);
            param.setEnabled(Boolean.TRUE);
            param.setSort(0F);
            organizationClient.updateOrganization(organization.getId(), param);
            organization = organizationClient.getOrganizationsByName(deptName);
        }
        organizationCache.put(deptName, organization);
        return organization;
    }

    private boolean needsUpdate(UserDTO localUser, GmThirdUserDTO remoteUser, String name, Long organizationId) {
        if (!Objects.equals(localUser.getName(), name)) {
            return true;
        }
        if (!Objects.equals(localUser.getGender(), readGender(remoteUser))) {
            return true;
        }
        if (!Objects.equals(localUser.getEnabled(), readUserEnabled(remoteUser))) {
            return true;
        }
        Set<Long> organizationIds = collectOrganizationIds(localUser);
        return !organizationIds.contains(organizationId) || organizationIds.size() != 1;
    }

    @SuppressWarnings("deprecation")
    private UserModifyParam buildUserModifyParam(UserDTO localUser, GmThirdUserDTO remoteUser, String name, Long organizationId) {
        UserModifyParam param = new UserModifyParam();
        ObjectUtils.copying(localUser, param);
        param.setName(name);
        param.setMobile(localUser.getMobile());
        param.setUsername(localUser.getUsername());
        param.setGender(readGender(remoteUser));
        param.setEnabled(readUserEnabled(remoteUser));
        param.setOrganizationIds(Collections.singletonList(organizationId));
        param.setPositionIds(collectPositionIds(localUser));
        param.setAppointmentIds(collectAppointmentIds(localUser));
        param.setRoleIds(collectRoleIds(localUser));
        param.setTenant(SessionContextHolder.getRequester().getTenant());
        param.setSendMessage(false);
        return param;
    }

    private UserModifyParam buildUserCreateParam(GmThirdUserDTO remoteUser, String name, Long organizationId) {
        UserModifyParam param = new UserModifyParam();
        param.setName(name);
        param.setMobile(remoteUser.getPhonenumber());
        param.setUsername(remoteUser.getPhonenumber());
        param.setGender(readGender(remoteUser));
        param.setEnabled(readUserEnabled(remoteUser));
        param.setOrganizationIds(Collections.singletonList(organizationId));
        param.setPositionIds(Collections.emptyList());
        param.setAppointmentIds(Collections.emptyList());
        param.setRoleIds(Collections.emptyList());
        param.setTenant(SessionContextHolder.getRequester().getTenant());
        param.setSendMessage(false);
        return param;
    }

    private UserDTO buildCreatedUser(Long userId, UserModifyParam param) {
        UserDTO user = new UserDTO();
        user.setId(userId);
        user.setName(param.getName());
        user.setMobile(param.getMobile());
        user.setUsername(param.getUsername());
        user.setEnabled(param.getEnabled());
        user.setGender(param.getGender());
        if (CollectionUtils.isNotEmpty(param.getOrganizationIds())) {
            UserDTO.Organization organization = new UserDTO.Organization();
            organization.setId(param.getOrganizationIds().get(0));
            user.setOrganization(organization);
        }
        return user;
    }

    private Set<Long> collectOrganizationIds(UserDTO user) {
        Set<Long> ids = new HashSet<>();
        if (user.getOrganization() != null && user.getOrganization().getId() != null) {
            ids.add(user.getOrganization().getId());
        }
        if (CollectionUtils.isNotEmpty(user.getOrganizations())) {
            ids.addAll(user.getOrganizations().stream()
                               .map(UserDTO.Organization::getId)
                               .filter(Objects::nonNull)
                               .collect(Collectors.toSet()));
        }
        return ids;
    }

    private List<Long> collectPositionIds(UserDTO user) {
        if (CollectionUtils.isEmpty(user.getPositions())) {
            return Collections.emptyList();
        }
        return user.getPositions().stream().map(UserDTO.Position::getId).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private List<Long> collectAppointmentIds(UserDTO user) {
        if (CollectionUtils.isEmpty(user.getAppointments())) {
            return Collections.emptyList();
        }
        return user.getAppointments().stream().map(UserDTO.Appointment::getId).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private List<Long> collectRoleIds(UserDTO user) {
        if (CollectionUtils.isEmpty(user.getRoles())) {
            return Collections.emptyList();
        }
        return user.getRoles().stream().map(UserDTO.Role::getId).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private String readLeader(GmThirdUserDTO user) {
        if (StringUtils.isNotBlank(user.getLeader())) {
            return user.getLeader();
        }
        return user.getDept() == null ? null : user.getDept().getLeader();
    }

    private String readDeptName(GmThirdUserDTO user) {
        return user.getDept() == null ? null : user.getDept().getDeptName();
    }

    private Long readDeptId(GmThirdUserDTO user) {
        if (user.getDeptId() != null) {
            return user.getDeptId();
        }
        return user.getDept() == null ? null : user.getDept().getDeptId();
    }

    private String readName(GmThirdUserDTO user) {
        return StringUtils.isNotBlank(user.getNickName()) ? user.getNickName() : user.getUserName();
    }

    private Boolean readUserEnabled(GmThirdUserDTO user) {
        if (StringUtils.isNotBlank(user.getDelFlag()) && !"0".equals(user.getDelFlag())) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private Boolean readTenantEnabled(GmThirdUserDTO user) {
        if (StringUtils.isNotBlank(user.getStatus()) && !"0".equals(user.getStatus())) {
            return Boolean.FALSE;
        }
        return Boolean.TRUE;
    }

    private void batchUpdateTenantEnabled(Integer tenant, TenantEnabledCollector collector) {
        if (collector.isEmpty()) {
            return;
        }
        RenewUserTenantEnabledBatchParam param = new RenewUserTenantEnabledBatchParam();
        param.setTenant(tenant);
        param.setEnableUserIds(new ArrayList<>(collector.enableUserIds));
        param.setDisableUserIds(new ArrayList<>(collector.disableUserIds));
        userTenantClient.batchUpdateUserTenantEnabled(param);
    }

    private void recordFailedUser(Integer tenant, GmThirdUserDTO remoteUser, LocalDateTime syncBeginTime,
                                  LocalDateTime syncEndTime, String errorMessage, Throwable throwable
                                 ) {
        GmUserSyncFailedRecordPO record = new GmUserSyncFailedRecordPO();
        record.setTenant(tenant);
        record.setThirdUserId(remoteUser.getUserId());
        record.setMobile(remoteUser.getPhonenumber());
        record.setUserName(readName(remoteUser));
        record.setUpdateTimeBegin(syncBeginTime);
        record.setUpdateTimeEnd(syncEndTime);
        record.setErrorMessage(truncate(errorMessage, MAX_ERROR_MESSAGE_LENGTH));
        record.setErrorStack(stackTraceToString(throwable));
        failedRecordMapper.insert(record);
    }

    private String truncate(String value, int maxLength) {
        if (value == null || value.length() <= maxLength) {
            return value;
        }
        return value.substring(0, maxLength);
    }

    private String stackTraceToString(Throwable throwable) {
        if (throwable == null) {
            return null;
        }
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    private Gender readGender(GmThirdUserDTO user) {
        if ("0".equals(user.getSex()) || "男".equals(user.getSex())) {
            return Gender.MALE;
        }
        if ("1".equals(user.getSex()) || "女".equals(user.getSex())) {
            return Gender.FEMALE;
        }
        return Gender.NONE;
    }

    private static class SyncStats {
        private final int total;
        private int candidates;
        private int created;
        private int updated;
        private int skipped;
        private int failed;

        private SyncStats(int total) {
            this.total = total;
        }
    }

    private static class TenantEnabledCollector {
        private final Set<Long> enableUserIds = new HashSet<>();
        private final Set<Long> disableUserIds = new HashSet<>();

        private void add(Long userId, Boolean enabled) {
            if (userId == null) {
                return;
            }
            if (Boolean.TRUE.equals(enabled)) {
                disableUserIds.remove(userId);
                enableUserIds.add(userId);
                return;
            }
            enableUserIds.remove(userId);
            disableUserIds.add(userId);
        }

        private boolean isEmpty() {
            return enableUserIds.isEmpty() && disableUserIds.isEmpty();
        }
    }

    private static class DeptTreeIndex {
        private final Map<Long, GmThirdDeptTreeDTO> deptById = new HashMap<>();
        private final Map<Long, Long> parentIdById = new HashMap<>();

        private static DeptTreeIndex empty() {
            return new DeptTreeIndex();
        }

        private static DeptTreeIndex of(List<GmThirdDeptTreeDTO> roots) {
            DeptTreeIndex index = new DeptTreeIndex();
            index.addAll(roots, null);
            return index;
        }

        private void addAll(List<GmThirdDeptTreeDTO> nodes, Long parentId) {
            if (CollectionUtils.isEmpty(nodes)) {
                return;
            }
            for (GmThirdDeptTreeDTO node : nodes) {
                if (node == null || node.getId() == null) {
                    continue;
                }
                deptById.put(node.getId(), node);
                if (parentId != null) {
                    parentIdById.put(node.getId(), parentId);
                }
                addAll(node.getChildren(), node.getId());
            }
        }

        private GmThirdDeptTreeDTO get(Long id) {
            return id == null ? null : deptById.get(id);
        }

        private GmThirdDeptTreeDTO getParent(Long id) {
            Long parentId = id == null ? null : parentIdById.get(id);
            return parentId == null ? null : deptById.get(parentId);
        }

        private List<GmThirdDeptTreeDTO> nodes() {
            return new ArrayList<>(deptById.values());
        }

        private int size() {
            return deptById.size();
        }
    }
}
