package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.HomeApplicationMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.HomeApplication;
import com.guhecloud.cube.biz.ouaa.server.param.HomeApplicationModifyParam;
import com.guhecloud.cube.biz.ouaa.server.service.HomeApplicationService;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 首页应用
 *
 * @author lanchun
 * @date 2022/5/13 11:23
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HomeApplicationServiceImpl extends ServiceImpl<HomeApplicationMapper, HomeApplication> implements HomeApplicationService {

    /**
     * 首页应用新增
     *
     * @param params
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull List<HomeApplicationModifyParam> params) {
        List<HomeApplication> homeApplications = this.listHomeApplications();
        if (!CollectionUtils.isEmpty(homeApplications)) {
            DatabaseContextHolder.softDelete(this, homeApplications);
        }
        if (!CollectionUtils.isEmpty(params)) {
            List<HomeApplication> addHomeApplications = ObjectUtils.copying(params, HomeApplication.class);
            addHomeApplications.stream().forEach(homeApplication -> {
                homeApplication.setTerminal(SessionContextHolder.getRequester().getTerminal());
                homeApplication.setUserId(SecurityContextHolder.getPrincipal().getId());
            });
            this.saveBatch(addHomeApplications);
        }
    }

    /**
     * 首页应用列表
     *
     * @return
     */
    @Override
    public List<HomeApplication> listHomeApplications() {
        return this.lambdaQuery()
                .eq(HomeApplication::getTerminal, SessionContextHolder.getRequester().getTerminal())
                .eq(HomeApplication::getUserId, SecurityContextHolder.getPrincipal().getId())
                .orderByAsc(HomeApplication::getSort)
                .list();
    }

    @Override
    public List<HomeApplication> listHomeApplications(@NonNull Collection<? extends Serializable> applicationIds) {
        return CollectionUtils.isEmpty(applicationIds) ? Collections.emptyList() : this.lambdaQuery()
                .eq(HomeApplication::getTerminal, SessionContextHolder.getRequester().getTerminal())
                .eq(HomeApplication::getUserId, SecurityContextHolder.getPrincipal().getId())
                .in(HomeApplication::getApplicationId, applicationIds)
                .orderByAsc(HomeApplication::getSort)
                .list();
    }

    /**
     * 首页应用删除
     *
     * @param applicationId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByApplicationId(@NonNull Long applicationId) {
        HomeApplication homeApplication = this.baseMapper.selectOne(Wrappers.<HomeApplication>lambdaQuery()
                                                                            .eq(HomeApplication::getTerminal, SessionContextHolder
                                                                                    .getRequester().getTerminal())
                                                                            .eq(HomeApplication::getUserId, SecurityContextHolder
                                                                                    .getPrincipal().getId())
                                                                            .eq(HomeApplication::getApplicationId, applicationId)
                                                                   );
        if (Objects.nonNull(homeApplication)) {
            DatabaseContextHolder.softDelete(this, homeApplication);
        }
    }
}
