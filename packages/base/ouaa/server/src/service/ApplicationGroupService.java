package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.ApplicationGroup;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationGroupModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.ApplicationGroupQueryParam;
import com.guhecloud.cube.biz.ouaa.server.vo.BaseInfo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

public interface ApplicationGroupService extends IService<ApplicationGroup> {

    Long save(ApplicationGroupModifyParam param);

    void update(Long id, ApplicationGroupModifyParam param);

    void remove(Long id);

    ApplicationGroup getById(Serializable id);

    List<ApplicationGroup> listByIds(Collection<? extends Serializable> ids);

    Page<ApplicationGroup> listByPage(ApplicationGroupQueryParam param);

    List<BaseInfo> listForSelection(ApplicationGroupQueryParam param);
}
