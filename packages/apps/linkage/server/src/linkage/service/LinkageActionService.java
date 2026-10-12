package com.guhecloud.cube.biz.linkage.server.linkage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.LinkageAction;

import java.util.Collection;
import java.util.List;

public interface LinkageActionService extends IService<LinkageAction> {

    List<LinkageAction> listByLinkageIds(Collection<Long> linkageIds);

    List<LinkageAction> listEnabledByLinkageIds(Collection<Long> linkageIds);

    void removeByLinkageId(Long linkageId);
}
