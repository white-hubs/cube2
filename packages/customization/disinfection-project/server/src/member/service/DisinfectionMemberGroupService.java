package com.guhecloud.cube.srv.gen.disinfection.server.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberGroup;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberGroupCommandParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberGroupQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MemberGroupResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;

import java.util.List;

public interface DisinfectionMemberGroupService extends IService<MemberGroup> {
    Pagination<MemberGroupResult> getMemberGroupPage(MemberGroupQueryParam param);

    MemberGroupResult getMemberGroupById(Long id);

    Long addMemberGroup(MemberGroupCommandParam param);

    Boolean removeMemberGroup(Long id);

    Boolean editMemberGroup(MemberGroupCommandParam param);

    List<Long> getManageGroupIdsByUserId(Long userId);

    List<Long> getGroupIdsByName(String keyword);

    List<MemberGroup> queryByPeriod(Period period);
}
