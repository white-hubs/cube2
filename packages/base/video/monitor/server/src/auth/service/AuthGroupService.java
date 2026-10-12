package com.guhecloud.cube.biz.vmonitor.server.auth.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.biz.vmonitor.server.auth.param.commond.AuthGroupModifyParam;
import com.guhecloud.cube.biz.vmonitor.server.auth.param.query.AuthQueryParam;
import com.guhecloud.cube.biz.vmonitor.server.auth.po.AuthGroupPO;

public interface AuthGroupService {

    Long increaseAuthGroup(AuthGroupModifyParam param);

    void updateAuthGroup(Long id, AuthGroupModifyParam param);

    AuthGroupPO getAuthGroup(Long id);

    Page<AuthGroupPO> queryPage(AuthQueryParam param);

    void deleteAuthGroup(Long id);
}
