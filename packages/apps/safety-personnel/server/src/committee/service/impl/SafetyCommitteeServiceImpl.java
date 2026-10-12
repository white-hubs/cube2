package com.guhecloud.cube.srv.gen.safety.personnel.server.committee.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.vo.FileVO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.domain.CommitteeChangeLog;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.domain.CommitteeMember;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.domain.SafetyCommittee;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.mapper.SafetyCommitteeMapper;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.param.CommitteeModifyParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.param.CommitteeQueryParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.service.CommitteeChangeLogService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.service.CommitteeMemberService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.service.SafetyCommitteeService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.vo.CommitteeChangeLogVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.vo.CommitteeDetailVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.vo.CommitteeVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.SafetyPersonnelConstants;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.CommitteeChangeTypeEnum;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.facade.SafetyPersonnelFileFacadeService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.facade.SafetyPersonnelOuaaFacadeService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.facade.model.OuaaUserSnapshot;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 安委会服务实现。
 */
@Service("safetyPersonnelSafetyCommitteeService")
@RequiredArgsConstructor
public class SafetyCommitteeServiceImpl extends ServiceImpl<SafetyCommitteeMapper, SafetyCommittee> implements SafetyCommitteeService {

    private final CommitteeMemberService memberService;
    private final CommitteeChangeLogService changeLogService;
    private final SafetyPersonnelOuaaFacadeService ouaaFacade;
    private final SafetyPersonnelFileFacadeService fileFacadeService;

    @Override
    public Pagination<CommitteeVO> pageCommittees(CommitteeQueryParam param) {
        Page<SafetyCommittee> page = lambdaQuery()
                .like(StringUtils.isNotBlank(param.getKeyword()), SafetyCommittee::getCommitteeName, param.getKeyword())
                .orderByDesc(SafetyCommittee::getCreateTime)
                .page(PagingUtils.convert(param));
        List<CommitteeVO> records = page.getRecords().stream().map(this::toCommitteeVO).toList();
        return PagingUtils.convert(page, records);
    }

    @Override
    public CommitteeDetailVO detail(Long id) {
        SafetyCommittee committee = getRequired(id);
        return toDetail(committee);
    }

    @Override
    @Transactional
    public CommitteeDetailVO create(CommitteeModifyParam param) {
        String name = normalizeName(param.getCommitteeName());
        assertNameNotExists(name, null);
        SafetyCommittee committee = new SafetyCommittee();
        committee.setId(IdWorker.getId());
        fill(committee, param, name);
        save(committee);
        saveMembers(committee.getId(), param.getUserIds());
        saveAttachments(committee.getId(), param.getFileIds());
        changeLogService.log(committee.getId(), CommitteeChangeTypeEnum.CREATE.getCode(), JSON.toJSONString(committee));
        return toDetail(committee);
    }

    @Override
    @Transactional
    public CommitteeDetailVO updateCommittee(Long id, CommitteeModifyParam param) {
        SafetyCommittee committee = getRequired(id);
        String name = normalizeName(param.getCommitteeName());
        assertNameNotExists(name, id);
        fill(committee, param, name);
        updateById(committee);
        softDeleteMembers(id);
        saveMembers(id, param.getUserIds());
        saveAttachments(id, param.getFileIds());
        changeLogService.log(committee.getId(), CommitteeChangeTypeEnum.UPDATE.getCode(), JSON.toJSONString(committee));
        return toDetail(committee);
    }

    @Override
    @Transactional
    public void deleteCommittee(Long id) {
        SafetyCommittee committee = getRequired(id);
        softDeleteMembers(id);
        fileFacadeService.removeAllByBusinessKeyAndScene(id, SafetyPersonnelConstants.SCENE_COMMITTEE_ATTACHMENT);
        DatabaseContextHolder.softDelete(this, committee);
        changeLogService.log(id, CommitteeChangeTypeEnum.DELETE.getCode(), JSON.toJSONString(committee));
    }

    @Override
    public List<CommitteeChangeLogVO> changeLogs(Long id) {
        getRequired(id);
        return changeLogService.lambdaQuery()
                .eq(CommitteeChangeLog::getCommitteeId, id)
                .orderByDesc(CommitteeChangeLog::getChangeTime)
                .list()
                .stream()
                .map(CommitteeChangeLogVO::of)
                .toList();
    }

    private void fill(SafetyCommittee committee, CommitteeModifyParam param, String name) {
        var organization = ouaaFacade.getOrganization(param.getDeptId());
        Assert.isNotFound(organization, "负责单位/部门不存在");
        committee.setCommitteeName(name);
        committee.setDeptId(param.getDeptId());
        committee.setDeptName(organization.getName());
        committee.setContactPhone(param.getContactPhone());
        committee.setIntroduction(param.getIntroduction());
        committee.setMemberCount((int) param.getUserIds().stream().filter(Objects::nonNull).distinct().count());
    }

    private CommitteeVO toCommitteeVO(SafetyCommittee committee) {
        List<FileVO> attachments = fileFacadeService.listByBusinessKey(committee.getId());
        String firstAttachmentName = attachments.isEmpty() ? null : attachments.get(0).getName();
        return CommitteeVO.of(committee, attachments.size(), firstAttachmentName);
    }

    private void saveMembers(Long committeeId, List<Long> userIds) {
        List<Long> distinctUserIds = userIds.stream().filter(Objects::nonNull).distinct().toList();
        Assert.isParameterError(distinctUserIds.isEmpty(), "成员不能为空");
        List<UserDTO> users = ouaaFacade.listUsersByIds(distinctUserIds);
        Map<Long, UserDTO> userMap = users.stream().collect(Collectors.toMap(UserDTO::getId, Function.identity(), (left, right) -> left));
        Assert.isNotFound(userMap.size() != distinctUserIds.size(), "存在无效成员");
        List<CommitteeMember> members = distinctUserIds.stream().map(userMap::get).map(user -> toMember(committeeId, user)).toList();
        memberService.saveBatch(members);
    }

    private void softDeleteMembers(Long committeeId) {
        List<CommitteeMember> members = memberService.lambdaQuery()
                .eq(CommitteeMember::getCommitteeId, committeeId)
                .list();
        DatabaseContextHolder.softDelete(memberService, members);
    }

    private void saveAttachments(Long committeeId, List<Long> attachmentFileIds) {
        fileFacadeService.replaceAssociations(
                SafetyPersonnelConstants.SCENE_COMMITTEE_ATTACHMENT,
                committeeId,
                attachmentFileIds);
    }

    private CommitteeMember toMember(Long committeeId, UserDTO user) {
        OuaaUserSnapshot snapshot = ouaaFacade.buildSnapshot(user);
        CommitteeMember member = new CommitteeMember();
        member.setId(IdWorker.getId());
        member.setCommitteeId(committeeId);
        member.setUserId(user.getId());
        member.setUserName(snapshot.getUserName());
        member.setPhone(snapshot.getPhone());
        member.setDeptName(snapshot.getDeptName());
        member.setPostName(snapshot.getPostName());
        member.setJoinTime(LocalDateTime.now());
        member.setActive(Boolean.TRUE);
        return member;
    }

    private CommitteeDetailVO toDetail(SafetyCommittee committee) {
        List<CommitteeMember> members = memberService.lambdaQuery()
                .eq(CommitteeMember::getCommitteeId, committee.getId())
                .orderByAsc(CommitteeMember::getId)
                .list();
        List<FileVO> attachments = fileFacadeService.listByBusinessKey(committee.getId());
        return CommitteeDetailVO.of(committee, members, attachments);
    }

    private SafetyCommittee getRequired(Long id) {
        SafetyCommittee committee = getById(id);
        Assert.isNotFound(committee, "安委会不存在");
        return committee;
    }

    private void assertNameNotExists(String name, Long excludeId) {
        boolean exists = lambdaQuery()
                .eq(SafetyCommittee::getCommitteeName, name)
                .ne(Objects.nonNull(excludeId), SafetyCommittee::getId, excludeId)
                .exists();
        Assert.isConflict(exists, "安委会名称已存在");
    }

    private String normalizeName(String name) {
        String normalized = name == null ? null : name.trim();
        Assert.isParameterError(normalized == null || normalized.isBlank(), "安委会名称不能为空");
        return normalized;
    }

}
