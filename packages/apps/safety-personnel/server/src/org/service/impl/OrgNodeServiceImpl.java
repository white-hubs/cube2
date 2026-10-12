package com.guhecloud.cube.srv.gen.safety.personnel.server.org.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.param.UserQueryParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.facade.SafetyPersonnelFileFacadeService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.facade.SafetyPersonnelOuaaFacadeService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.facade.model.OuaaUserSnapshot;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.domain.OrgNode;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.domain.OrgNodeMember;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.mapper.OrgNodeMapper;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.param.OrgNodeMemberSaveParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.param.OrgNodeModifyParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.param.OrgUserCandidateQueryParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.service.OrgNodeMemberService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.service.OrgNodeService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.vo.HomeOrgArchitectureVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.vo.OrgNodeDetailVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.vo.OrgNodeMemberVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.vo.OrgNodeTreeVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.vo.OrgUserCandidateVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.TreeUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 安全管理组织展示节点服务实现。
 */
@Service("safetyPersonnelOrgNodeService")
@RequiredArgsConstructor
public class OrgNodeServiceImpl extends ServiceImpl<OrgNodeMapper, OrgNode> implements OrgNodeService {

    private static final int DEFAULT_SORT_NO = 0;
    private static final int HOME_MEMBER_LIMIT = 5;

    private final OrgNodeMemberService orgNodeMemberService;
    private final SafetyPersonnelFileFacadeService fileFacade;
    private final SafetyPersonnelOuaaFacadeService ouaaFacade;

    @Override
    public List<OrgNodeTreeVO> tree() {
        List<OrgNode> nodes = listNodes();
        Map<Long, List<OrgNodeMember>> memberMap = listMembers(nodes).stream()
                .collect(Collectors.groupingBy(OrgNodeMember::getOrgNodeId));
        return buildTree(nodes, memberMap, false);
    }

    @Override
    public HomeOrgArchitectureVO homeArchitecture() {
        List<OrgNode> nodes = listNodes();
        List<OrgNodeMember> members = listMembers(nodes);
        Map<Long, List<OrgNodeMember>> memberMap = members.stream()
                .collect(Collectors.groupingBy(OrgNodeMember::getOrgNodeId));
        Map<Long, FileVO> photographMap = mapHomeMemberPhotographs(memberMap);
        List<OrgNodeTreeVO> roots = buildTree(nodes, memberMap, false, photographMap);
        OrgNodeTreeVO root = roots.isEmpty() ? null : roots.get(0);
        HomeOrgArchitectureVO vo = new HomeOrgArchitectureVO();
        vo.setRoot(root);
        return vo;
    }

    @Override
    public OrgNodeDetailVO detail(Long id) {
        OrgNode node = getRequiredNode(id);
        List<OrgNodeMember> members = orgNodeMemberService.lambdaQuery()
                .eq(OrgNodeMember::getOrgNodeId, id)
                .orderByAsc(OrgNodeMember::getId)
                .list();
        return OrgNodeDetailVO.of(node, members);
    }

    @Override
    @Transactional
    public OrgNodeDetailVO createRoot(OrgNodeModifyParam param) {
        OrgNode node = createNode(null, param);
        return OrgNodeDetailVO.of(node, List.of());
    }

    @Override
    @Transactional
    public OrgNodeDetailVO createChild(Long parentId, OrgNodeModifyParam param) {
        OrgNode parent = getRequiredNode(parentId);
        OrgNode node = createNode(parent.getId(), param);
        return OrgNodeDetailVO.of(node, List.of());
    }

    @Override
    @Transactional
    public OrgNodeDetailVO updateNode(Long id, OrgNodeModifyParam param) {
        OrgNode node = getRequiredNode(id);
        String name = normalizeName(param.getName());
        assertNameNotExists(node.getParentId(), name, id);
        node.setName(name);
        node.setSortNo(Objects.requireNonNullElse(param.getSortNo(), DEFAULT_SORT_NO));
        updateById(node);
        return detail(id);
    }

    @Override
    @Transactional
    public void deleteNode(Long id) {
        OrgNode node = getRequiredNode(id);
        boolean hasChild = lambdaQuery().eq(OrgNode::getParentId, node.getId()).exists();
        Assert.isConflict(hasChild, "存在子节点，不允许删除");
        softDeleteMembersByNodeId(node.getId());
        DatabaseContextHolder.softDelete(this, node);
    }

    @Override
    @Transactional
    public OrgNodeDetailVO saveMembers(Long id, OrgNodeMemberSaveParam param) {
        OrgNode node = getRequiredNode(id);
        List<Long> userIds = param.getUserIds().stream()
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        softDeleteMembersByNodeId(node.getId());
        if (userIds.isEmpty()) {
            return OrgNodeDetailVO.of(node, List.of());
        }
        List<UserDTO> users = ouaaFacade.listUsersByIds(userIds);
        Map<Long, UserDTO> userMap = users.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity(), (left, right) -> left));
        Assert.isNotFound(userMap.size() != userIds.size(), "存在无效用户");
        List<OrgNodeMember> members = userIds.stream()
                .map(userMap::get)
                .map(user -> toMember(node.getId(), user))
                .toList();
        orgNodeMemberService.saveBatch(members);
        return OrgNodeDetailVO.of(node, members);
    }

    @Override
    public Pagination<OrgUserCandidateVO> pageUserCandidates(OrgUserCandidateQueryParam param) {
        UserQueryParam query = new UserQueryParam();
        query.setPageNumber(param.getPageNumber());
        query.setPageSize(param.getPageSize());
        query.setKeyword(param.getKeyword());
        query.setOrganizationId(param.getOrganizationId());
        query.setEnabled(param.getEnabled());
        Pagination<UserDTO> page = ouaaFacade.pageUsers(query);
        List<OrgUserCandidateVO> records = page.getRecords().stream()
                .map(OrgUserCandidateVO::of)
                .toList();
        return Pagination.<OrgUserCandidateVO>builder()
                .total(page.getTotal())
                .pages(page.getPages())
                .records(records)
                .build();
    }

    private void softDeleteMembersByNodeId(Long nodeId) {
        List<OrgNodeMember> members = orgNodeMemberService.lambdaQuery()
                .eq(OrgNodeMember::getOrgNodeId, nodeId)
                .list();
        DatabaseContextHolder.softDelete(orgNodeMemberService, members);
    }

    private OrgNode createNode(Long parentId, OrgNodeModifyParam param) {
        String name = normalizeName(param.getName());
        assertNameNotExists(parentId, name, null);
        OrgNode node = new OrgNode();
        node.setId(IdWorker.getId());
        node.setParentId(parentId);
        node.setName(name);
        node.setSortNo(Objects.requireNonNullElse(param.getSortNo(), DEFAULT_SORT_NO));
        save(node);
        return node;
    }

    private OrgNode getRequiredNode(Long id) {
        OrgNode node = getById(id);
        Assert.isNotFound(node, "组织节点不存在");
        return node;
    }

    private void assertNameNotExists(Long parentId, String name, Long excludeId) {
        boolean exists = lambdaQuery()
                .isNull(Objects.isNull(parentId), OrgNode::getParentId)
                .eq(Objects.nonNull(parentId), OrgNode::getParentId, parentId)
                .eq(OrgNode::getName, name)
                .ne(Objects.nonNull(excludeId), OrgNode::getId, excludeId)
                .exists();
        Assert.isConflict(exists, "同级组织节点名称已存在");
    }

    private List<OrgNode> listNodes() {
        return lambdaQuery()
                .orderByAsc(OrgNode::getLevel)
                .orderByAsc(OrgNode::getSortNo)
                .orderByAsc(OrgNode::getId)
                .list();
    }

    private List<OrgNodeMember> listMembers(Collection<OrgNode> nodes) {
        if (nodes.isEmpty()) {
            return List.of();
        }
        List<Long> nodeIds = nodes.stream().map(OrgNode::getId).toList();
        return orgNodeMemberService.lambdaQuery()
                .in(OrgNodeMember::getOrgNodeId, nodeIds)
                .orderByAsc(OrgNodeMember::getId)
                .list();
    }

    private List<OrgNodeTreeVO> buildTree(List<OrgNode> nodes, Map<Long, List<OrgNodeMember>> memberMap, boolean maskMobile) {
        return buildTree(nodes, memberMap, maskMobile, Map.of());
    }

    private List<OrgNodeTreeVO> buildTree(List<OrgNode> nodes, Map<Long, List<OrgNodeMember>> memberMap, boolean maskMobile,
                                          Map<Long, FileVO> photographMap
                                         ) {
        List<OrgNodeTreeVO> treeNodes = nodes.stream()
                .map(node -> toTreeNode(node, memberMap.getOrDefault(node.getId(), List.of()), maskMobile, photographMap))
                .toList();
        List<OrgNodeTreeVO> roots = TreeUtils.assembleTrees(treeNodes);
        sortTree(roots);
        return roots;
    }

    private OrgNodeTreeVO toTreeNode(OrgNode node, List<OrgNodeMember> members, boolean maskMobile, Map<Long, FileVO> photographMap) {
        OrgNodeTreeVO vo = OrgNodeTreeVO.of(node);
        vo.setMemberCount(members.size());
        vo.setHasMore(members.size() > HOME_MEMBER_LIMIT);
        vo.setMembers(members.stream()
                              .limit(HOME_MEMBER_LIMIT)
                              .map(member -> OrgNodeMemberVO.of(member, maskMobile, ouaaFacade, photographMap.get(member.getUserId())))
                              .toList());
        return vo;
    }

    private Map<Long, FileVO> mapHomeMemberPhotographs(Map<Long, List<OrgNodeMember>> memberMap) {
        List<Long> displayedUserIds = memberMap.values().stream()
                .flatMap(members -> members.stream().limit(HOME_MEMBER_LIMIT))
                .map(OrgNodeMember::getUserId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (displayedUserIds.isEmpty()) {
            return Map.of();
        }
        List<UserDTO> users = ouaaFacade.listUsersByIds(displayedUserIds);
        if (CollectionUtils.isEmpty(users)) {
            return Map.of();
        }
        Map<Long, Long> userPhotographKeyMap = users.stream()
                .filter(user -> Objects.nonNull(user.getId()))
                .filter(user -> Objects.nonNull(user.getPhotographKey()) && user.getPhotographKey() > 0)
                .collect(Collectors.toMap(UserDTO::getId, UserDTO::getPhotographKey, (left, right) -> left));
        Map<Long, FileVO> photographFileMap = fileFacade.mapFirstByBusinessKeys(userPhotographKeyMap.values().stream().toList());
        return userPhotographKeyMap.entrySet().stream()
                .filter(entry -> photographFileMap.containsKey(entry.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> photographFileMap.get(entry.getValue()), (left, right) -> left));
    }

    private void sortTree(List<OrgNodeTreeVO> nodes) {
        nodes.sort(Comparator.comparing(OrgNodeTreeVO::getSortNo, Comparator.nullsLast(Integer::compareTo))
                           .thenComparing(OrgNodeTreeVO::getId));
        nodes.forEach(node -> {
            if (CollectionUtils.isNotEmpty(node.getChildren())) {
                sortTree(node.getChildren());
            }
        });
    }

    private OrgNodeMember toMember(Long nodeId, UserDTO user) {
        OuaaUserSnapshot snapshot = ouaaFacade.buildSnapshot(user);
        OrgNodeMember member = new OrgNodeMember();
        member.setId(IdWorker.getId());
        member.setOrgNodeId(nodeId);
        member.setUserId(user.getId());
        member.setUserName(snapshot.getUserName());
        member.setDeptName(snapshot.getDeptName());
        member.setMobile(snapshot.getPhone());
        return member;
    }

    private String normalizeName(String name) {
        String normalized = name == null ? null : name.trim();
        Assert.isParameterError(normalized == null || normalized.isBlank(), "节点名称不能为空");
        return normalized;
    }
}
