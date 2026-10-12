package com.guhecloud.cube.biz.linkage.server.linkage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.LinkageActionBefore;

import java.util.Collection;
import java.util.List;

public interface LinkageActionBeforeService extends IService<LinkageActionBefore> {

    List<LinkageActionBefore> listByLinkageIds(Collection<Long> linkageIds);

    List<LinkageActionBefore> listEnabledByLinkageIds(Collection<Long> linkageIds);

    void removeByLinkageId(Long linkageId);
}
