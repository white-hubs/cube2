package com.guhecloud.cube.srv.gen.disinfection.server.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.disinfection.server.member.entity.MemberLog;
import com.guhecloud.cube.srv.gen.disinfection.server.member.param.MemberLogQueryParam;
import com.guhecloud.cube.srv.gen.disinfection.server.member.vo.MemberLogResult;
import com.guhecloud.platform.base.model.Pagination;

public interface DisinfectionMemberLogService extends IService<MemberLog> {

    Pagination<MemberLogResult> getMemberLogPage(MemberLogQueryParam param);
}
