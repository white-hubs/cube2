package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.HomeApplication;
import com.guhecloud.cube.biz.ouaa.server.param.HomeApplicationModifyParam;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface HomeApplicationService extends IService<HomeApplication> {

    void save(List<HomeApplicationModifyParam> params);

    List<HomeApplication> listHomeApplications();

    List<HomeApplication> listHomeApplications(Collection<? extends Serializable> applicationIds);

    void removeByApplicationId(Long applicationId);
}
