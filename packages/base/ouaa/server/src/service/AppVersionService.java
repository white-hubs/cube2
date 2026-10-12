package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.AppVersion;
import com.guhecloud.cube.biz.ouaa.server.param.AppVersionModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.AppVersionQueryParam;

import java.io.Serializable;

public interface AppVersionService extends IService<AppVersion> {

    Long save(AppVersionModifyParam param);

    void update(Long id, AppVersionModifyParam param);

    AppVersion getById(Serializable id);

    AppVersion getNewAppVersion(String appType);

    Page<AppVersion> getPage(AppVersionQueryParam param);

    void removeById(Long id);
}
