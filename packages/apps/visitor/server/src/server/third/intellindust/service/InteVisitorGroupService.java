package com.guhecloud.cube.srv.gen.visitor.management.server.third.intellindust.service;

import com.guhecloud.cube.srv.gen.visitor.management.server.po.SystemLoginInfoPO;

public interface InteVisitorGroupService {
    Long addGroup(SystemLoginInfoPO loginInfoPO);

    Long getDefaultGroupId(SystemLoginInfoPO loginInfoPO);
}
