package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.HomeApplicationCardMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.HomeApplicationCard;
import com.guhecloud.cube.biz.ouaa.server.param.HomeApplicationCardBatchModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.HomeApplicationCardModifyParam;
import com.guhecloud.cube.biz.ouaa.server.service.HomeApplicationCardService;
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
 * 首页应用卡片
 *
 * @author lanchun
 * @date 2022/5/13 11:23
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HomeApplicationCardServiceImpl extends ServiceImpl<HomeApplicationCardMapper, HomeApplicationCard> implements HomeApplicationCardService {

    /**
     * 首页应用卡片新增
     *
     * @param param
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull HomeApplicationCardModifyParam param) {
        HomeApplicationCard homeApplicationCard = ObjectUtils.copying(param, HomeApplicationCard.class);
        homeApplicationCard.setTerminal(SessionContextHolder.getRequester().getTerminal());
        homeApplicationCard.setUserId(SecurityContextHolder.getPrincipal().getId());
        this.save(homeApplicationCard);
    }

    /**
     * 首页应用卡片批量新增
     *
     * @param param
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveBatch(@NonNull HomeApplicationCardBatchModifyParam param) {
        //删除用户配置的卡片
        List<HomeApplicationCard> delHomeApplicationCards = this.listHomeApplicationCards();
        if (!CollectionUtils.isEmpty(delHomeApplicationCards)) {
            DatabaseContextHolder.softDelete(this, delHomeApplicationCards);
        }
        if (!CollectionUtils.isEmpty(param.getCardModifyParams())) {
            List<HomeApplicationCard> homeApplicationCards = ObjectUtils.copying(param.getCardModifyParams(), HomeApplicationCard.class);
            homeApplicationCards.forEach(homeApplicationCard -> {
                homeApplicationCard.setTerminal(SessionContextHolder.getRequester().getTerminal());
                homeApplicationCard.setUserId(SecurityContextHolder.getPrincipal().getId());
            });
            this.saveBatch(homeApplicationCards);
        }
    }

    @Override
    public HomeApplicationCard getHomeApplicationCardByCode(@NonNull String code) {
        return this.baseMapper.selectOne(Wrappers.<HomeApplicationCard>lambdaQuery()
                                                 .eq(HomeApplicationCard::getCardCode, code)
                                                 .eq(HomeApplicationCard::getTerminal, SessionContextHolder.getRequester().getTerminal())
                                                 .eq(HomeApplicationCard::getUserId, SecurityContextHolder.getPrincipal().getId()));
    }

    /**
     * 首页应用列表
     *
     * @return
     */
    @Override
    public List<HomeApplicationCard> listHomeApplicationCards() {
        return this.lambdaQuery()
                .eq(HomeApplicationCard::getTerminal, SessionContextHolder.getRequester().getTerminal())
                .eq(HomeApplicationCard::getUserId, SecurityContextHolder.getPrincipal().getId())
                .orderByAsc(HomeApplicationCard::getSort)
                .list();
    }

    @Override
    public List<HomeApplicationCard> listHomeApplicationCards(@NonNull Collection<? extends Serializable> applicationIds) {
        return CollectionUtils.isEmpty(applicationIds) ? Collections.emptyList() : this.lambdaQuery()
                .eq(HomeApplicationCard::getTerminal, SessionContextHolder.getRequester().getTerminal())
                .eq(HomeApplicationCard::getUserId, SecurityContextHolder.getPrincipal().getId())
                .in(HomeApplicationCard::getApplicationId, applicationIds)
                .orderByAsc(HomeApplicationCard::getSort)
                .list();
    }

    /**
     * 首页应用删除
     *
     * @param cardCode
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeByCardCode(@NonNull String cardCode) {
        HomeApplicationCard homeApplicationCard = this.baseMapper.selectOne(Wrappers.<HomeApplicationCard>lambdaQuery()
                                                                                    .eq(HomeApplicationCard::getTerminal, SessionContextHolder
                                                                                            .getRequester().getTerminal())
                                                                                    .eq(HomeApplicationCard::getUserId, SecurityContextHolder
                                                                                            .getPrincipal().getId())
                                                                                    .eq(HomeApplicationCard::getCardCode, cardCode)
                                                                           );
        if (Objects.nonNull(homeApplicationCard)) {
            DatabaseContextHolder.softDelete(this, homeApplicationCard);
        }
    }
}
