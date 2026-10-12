package com.guhecloud.cube.srv.gen.safety.personnel.server.committee.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.domain.CommitteeMember;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.mapper.CommitteeMemberMapper;
import com.guhecloud.cube.srv.gen.safety.personnel.server.committee.service.CommitteeMemberService;
import org.springframework.stereotype.Service;

/**
 * 安委会成员服务实现。
 */
@Service("safetyPersonnelCommitteeMemberService")
public class CommitteeMemberServiceImpl extends ServiceImpl<CommitteeMemberMapper, CommitteeMember> implements CommitteeMemberService {
}
