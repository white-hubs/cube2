package com.guhecloud.cube.biz.linkage.server.linkage.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.linkage.server.linkage.dao.LinkageActionBeforeMapper;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.LinkageActionBefore;
import com.guhecloud.cube.biz.linkage.server.linkage.service.LinkageActionBeforeService;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class LinkageActionBeforeServiceImpl extends ServiceImpl<LinkageActionBeforeMapper, LinkageActionBefore> implements LinkageActionBeforeService {

    @Override
    public List<LinkageActionBefore> listByLinkageIds(Collection<Long> linkageIds) {
        return CollectionUtils.isEmpty(linkageIds) ? CollectionUtils.emptyList() :
               this.list(Wrappers.<LinkageActionBefore>lambdaQuery()
                                 .in(LinkageActionBefore::getLinkageId, linkageIds));
    }

    @Override
    public List<LinkageActionBefore> listEnabledByLinkageIds(Collection<Long> linkageIds) {
        return CollectionUtils.isEmpty(linkageIds) ? CollectionUtils.emptyList() :
               this.list(Wrappers.<LinkageActionBefore>lambdaQuery()
                                 .eq(LinkageActionBefore::getEnabled, true)
                                 .in(LinkageActionBefore::getLinkageId, linkageIds));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByLinkageId(Long linkageId) {
        var linkageActionBeforeList = this.listByLinkageIds(Collections.singleton(linkageId));
        if (CollectionUtils.isNotEmpty(linkageActionBeforeList)) {
            DatabaseContextHolder.softDelete(this, linkageActionBeforeList);
        }
    }
}
