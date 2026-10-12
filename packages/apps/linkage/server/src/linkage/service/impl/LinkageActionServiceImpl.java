package com.guhecloud.cube.biz.linkage.server.linkage.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.linkage.server.linkage.dao.LinkageActionMapper;
import com.guhecloud.cube.biz.linkage.server.linkage.entity.LinkageAction;
import com.guhecloud.cube.biz.linkage.server.linkage.service.LinkageActionService;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LinkageActionServiceImpl extends ServiceImpl<LinkageActionMapper, LinkageAction> implements LinkageActionService {

    @Override
    public List<LinkageAction> listByLinkageIds(@NonNull Collection<Long> linkageIds) {
        return CollectionUtils.isEmpty(linkageIds) ? Collections.emptyList()
                                                   : this.lambdaQuery().in(LinkageAction::getLinkageId, linkageIds).list();
    }

    @Override
    public List<LinkageAction> listEnabledByLinkageIds(@NonNull Collection<Long> linkageIds) {
        return CollectionUtils.isEmpty(linkageIds) ? Collections.emptyList()
                                                   : this.lambdaQuery().in(LinkageAction::getLinkageId, linkageIds)
                       .eq(LinkageAction::getEnabled, true).list();
    }

    @Override
    public void removeByLinkageId(@NonNull Long linkageId) {
        List<LinkageAction> linkageActions = this.listByLinkageIds(Collections.singleton(linkageId));
        if (!CollectionUtils.isEmpty(linkageActions)) {
            DatabaseContextHolder.softDelete(this, linkageActions);
        }
    }
}
