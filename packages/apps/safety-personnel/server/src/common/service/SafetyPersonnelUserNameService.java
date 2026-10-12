package com.guhecloud.cube.srv.gen.safety.personnel.server.common.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.domain.CommitteeMember;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.mapper.CommitteeMemberMapper;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.facade.SafetyPersonnelOuaaFacadeService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.domain.OrgNodeMember;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.mapper.OrgNodeMemberMapper;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.domain.Person;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.mapper.PersonMapper;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 安全人员服务用户姓名解析。
 */
@Service("safetyPersonnelUserNameService")
@RequiredArgsConstructor
public class SafetyPersonnelUserNameService {

    private final PersonMapper personMapper;
    private final CommitteeMemberMapper committeeMemberMapper;
    private final OrgNodeMemberMapper orgNodeMemberMapper;
    private final SafetyPersonnelOuaaFacadeService ouaaFacade;

    /**
     * 解析用户姓名；以前端传入的 userId 为准，姓名只从本地快照或 OUAA 解析。
     *
     * @param userId        OUAA用户ID
     * @param preferredName 前端回填姓名，仅保留接口兼容，不作为可信数据源
     *
     * @return 用户姓名，可能为空
     */
    public String resolveUserName(Long userId, String preferredName) {
        return resolveUserName(userId);
    }

    /**
     * 解析用户姓名；优先本地快照，缺失时单次查询OUAA。
     *
     * @param userId OUAA用户ID
     *
     * @return 用户姓名，可能为空
     */
    public String resolveUserName(Long userId) {
        if (userId == null) {
            return null;
        }
        String localName = lookupLocalUserName(userId);
        if (StringUtils.isNotBlank(localName)) {
            return localName;
        }
        var snapshot = ouaaFacade.buildSnapshot(userId);
        return snapshot == null ? null : snapshot.getUserName();
    }

    private String lookupLocalUserName(Long userId) {
        Person person = personMapper.selectOne(
                Wrappers.<Person>lambdaQuery().eq(Person::getUserId, userId).last("limit 1"));
        if (person != null && StringUtils.isNotBlank(person.getUserName())) {
            return person.getUserName();
        }
        CommitteeMember committeeMember = committeeMemberMapper.selectOne(
                Wrappers.<CommitteeMember>lambdaQuery().eq(CommitteeMember::getUserId, userId).last("limit 1"));
        if (committeeMember != null && StringUtils.isNotBlank(committeeMember.getUserName())) {
            return committeeMember.getUserName();
        }
        OrgNodeMember orgNodeMember = orgNodeMemberMapper.selectOne(
                Wrappers.<OrgNodeMember>lambdaQuery().eq(OrgNodeMember::getUserId, userId).last("limit 1"));
        if (orgNodeMember != null && StringUtils.isNotBlank(orgNodeMember.getUserName())) {
            return orgNodeMember.getUserName();
        }
        return null;
    }
}
