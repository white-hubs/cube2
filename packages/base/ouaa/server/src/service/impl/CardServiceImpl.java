package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.FileRefClient;
import com.guhecloud.cube.base.file.client.param.FileRefCreateParam;
import com.guhecloud.cube.biz.ouaa.metadata.enumeration.OuaaFileSceneCodeEnum;
import com.guhecloud.cube.biz.ouaa.server.dao.CardMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.Card;
import com.guhecloud.cube.biz.ouaa.server.param.CardModifyParam;
import com.guhecloud.cube.biz.ouaa.server.param.CardQueryParam;
import com.guhecloud.cube.biz.ouaa.server.service.CardService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
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
 * 卡片管理操作实现
 *
 * @author lanchun
 * @date 2022/5/23 15:53
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CardServiceImpl extends ServiceImpl<CardMapper, Card> implements CardService {

    private final FileRefClient fileRefClient;

    /**
     * 卡片保存
     *
     * @param param
     *
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull CardModifyParam param) {
        Card card = ObjectUtils.copying(param, Card.class);
        this.baseMapper.insert(card);
        if (Objects.nonNull(param.getFileId())) {
            FileRefCreateParam fileRefCreateParam = new FileRefCreateParam();
            fileRefCreateParam.setBusinessKey(card.getId());
            fileRefCreateParam.setFileId(param.getFileId());
            fileRefCreateParam.setSceneCode(OuaaFileSceneCodeEnum.OUAA_APP_CARD.getCode());
            fileRefClient.refInduce(fileRefCreateParam);
        }
        return card.getId();
    }

    /**
     * 卡片修改
     *
     * @param id
     * @param param
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull CardModifyParam param) {
        Card card = this.getById(id);
        Assert.isNotFound(card, "卡片不存在");
        ObjectUtils.copying(param, card);
        card.setSort(Objects.isNull(param.getSort()) ? 0 : param.getSort());
        if (Objects.nonNull(param.getFileId())) {
            FileRefCreateParam fileRefCreateParam = new FileRefCreateParam();
            fileRefCreateParam.setBusinessKey(card.getId());
            fileRefCreateParam.setFileId(param.getFileId());
            fileRefCreateParam.setSceneCode(OuaaFileSceneCodeEnum.OUAA_APP_CARD.getCode());
            fileRefClient.refInduce(fileRefCreateParam);
        }
        this.baseMapper.updateById(card);
    }

    /**
     * 卡片详情
     *
     * @param id
     *
     * @return
     */
    @Override
    public Card getById(@NonNull Serializable id) {
        return this.baseMapper.selectById(id);
    }

    /**
     * 卡片列表
     *
     * @param param
     *
     * @return
     */
    @Override
    public List<Card> listCards(CardQueryParam param) {
        return this.lambdaQuery()
                .eq(Objects.nonNull(param.getTerminalId()), Card::getTerminalId, param.getTerminalId())
                .eq(Objects.nonNull(param.getApplicationId()), Card::getApplicationId, param.getApplicationId())
                .eq(StringUtils.isNotEmpty(param.getApplicationCode()), Card::getApplicationCode, param.getApplicationCode())
                .and(StringUtils.isNotEmpty(param.getKeyword()), q -> q
                        .like(Card::getCode, param.getKeyword())
                        .or()
                        .like(Card::getName, param.getKeyword())
                    )
                .orderByAsc(Card::getSort)
                .list();
    }

    @Override
    public List<Card> listByTerminalApplicationIds(@NonNull Long terminalId, String keyword,
                                                   @NonNull Collection<? extends Serializable> applicationIds
                                                  ) {
        return CollectionUtils.isEmpty(applicationIds) ? Collections.emptyList() : this.lambdaQuery()
                .eq(Card::getTerminalId, terminalId)
                .like(StringUtils.isNotEmpty(keyword), Card::getName, keyword)
                .and(q -> q
                        .in(Card::getApplicationId, applicationIds)
                        .or()
                        .isNull(Card::getApplicationCode)
                    )
                .list();
    }

    @Override
    public List<Card> listBasicByTerminalApplicationIds(@NonNull Long terminalId, @NonNull String keyword) {
        return this.lambdaQuery()
                .eq(Card::getTerminalId, terminalId)
                .like(StringUtils.isNotEmpty(keyword), Card::getName, keyword)
                .isNull(Card::getApplicationCode)
                .list();
    }

    /**
     * 根据主键集合查询卡片列表
     *
     * @param idList
     *
     * @return
     */
    @Override
    public List<Card> listByIds(Collection<? extends Serializable> idList) {
        return CollectionUtils.isEmpty(idList) ? Collections.emptyList() : this.lambdaQuery().in(Card::getId, idList).list();
    }

    /**
     * 根据终端应用查询卡片列表
     *
     * @param terminalId
     * @param applicationId
     *
     * @return
     */
    @Override
    public List<Card> listByTerminalApplication(@NonNull Long terminalId, @NonNull Long applicationId) {
        return this.lambdaQuery()
                .eq(Card::getTerminalId, terminalId)
                .eq(Card::getApplicationId, applicationId)
                .orderByAsc(Card::getSort)
                .list();
    }

    /**
     * 卡片删除
     *
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public void remove(@NonNull Long terminalId, @NonNull Long applicationId, @NonNull String code) {
        Card card = this.baseMapper.selectOne(Wrappers.<Card>lambdaQuery().eq(Card::getTerminalId, terminalId)
                                                      .eq(Card::getApplicationId, applicationId).eq(Card::getCode, code));
        if (Objects.nonNull(card)) {
            DatabaseContextHolder.softDelete(this, card);
        }
    }
}
