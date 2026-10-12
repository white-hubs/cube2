package com.guhecloud.cube.srv.gen.safety.personnel.server.org.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.domain.OrgNodeMember;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.mapper.OrgNodeMemberMapper;
import com.guhecloud.cube.srv.gen.safety.personnel.server.org.service.OrgNodeMemberService;
import org.springframework.stereotype.Service;

/**
 * 安全管理组织展示成员服务实现。
 */
@Service("safetyPersonnelOrgNodeMemberService")
public class OrgNodeMemberServiceImpl extends ServiceImpl<OrgNodeMemberMapper, OrgNodeMember> implements OrgNodeMemberService {
}
