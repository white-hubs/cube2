package com.guhecloud.cube.biz.vmonitor.server.auth.service;

import com.guhecloud.cube.biz.vmonitor.server.auth.param.commond.AuthGrantTargetModifyParam;
import com.guhecloud.cube.biz.vmonitor.server.auth.po.AuthGrantTargetPO;
import com.guhecloud.cube.biz.vmonitor.server.auth.vo.AuthGrantTargetVO;

import java.util.Collection;
import java.util.List;

public interface AuthGrantTargetService {

    void batchSaveAuthGrantTargets(Long authGroupId, List<AuthGrantTargetModifyParam> authGrantTargetModifyParams);

    List<AuthGrantTargetPO> getAuthGrantTargetsByAuthGroupId(Long authGroupId);

    List<AuthGrantTargetVO> getAuthGrantTargetVOSByAuthGroupIds(Collection<Long> authGroupIds);

    void removeByAuthGroupId(Long authGroupId);
}
