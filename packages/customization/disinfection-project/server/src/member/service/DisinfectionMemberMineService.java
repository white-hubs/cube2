package com.guhecloud.cube.srv.gen.disinfection.server.member.service;

import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MyMemberParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MemberGroupResult;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MyMemberResult;

import java.util.List;

public interface DisinfectionMemberMineService {

    List<MemberGroupResult> getUserGroups(String keyword);

    List<MyMemberResult> getMyMembers(MyMemberParam param);

    MemberGroupResult userSelectGroup(Long userId);

    Boolean setUserSelectGroup(Long userId, Long selectGroupId);

    Long userDefaultGroup(Long userId, String account);

    Boolean exitGroup(Long userId, Long groupId);
}
