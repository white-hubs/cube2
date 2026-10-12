package com.guhecloud.cube.base.message2.server.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.message2.server.base.dao.MsgRecordTargetMapper;
import com.guhecloud.cube.base.message2.server.base.entity.MsgRecordTarget;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.service.MsgRecordTargetService;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 消息记录对象操作实现
 *
 * @author lanchun
 * @date 2021/11/9 19:24
 */
@Service
public class MsgRecordTargetServiceImpl extends ServiceImpl<MsgRecordTargetMapper, MsgRecordTarget> implements MsgRecordTargetService {

    @Override
    public MsgRecordTarget getByRecordId(@NonNull Long msgRecordId, @NonNull Long userId) {
        return this.baseMapper.selectOne(Wrappers.<MsgRecordTarget>lambdaQuery().eq(MsgRecordTarget::getMsgRecordId, msgRecordId)
                                                 .eq(MsgRecordTarget::getUserId, userId));
    }

    @Override
    public List<MsgRecordTarget> failList(@NonNull ChannelEnum channelType) {
        return this.baseMapper.failList(channelType);
    }

    @Override
    public List<MsgRecordTarget> listByMsgRecordIds(@NonNull Collection<Long> msgRecordIds) {
        return CollectionUtils.isEmpty(msgRecordIds) ? Collections.emptyList() :
               this.lambdaQuery()
                       .in(MsgRecordTarget::getMsgRecordId, msgRecordIds)
                       .orderByDesc(MsgRecordTarget::getSendTime)
                       .list();
    }

    @Override
    public Long unReadTotal(@NonNull ChannelEnum channelType) {
        return this.baseMapper.selectCount(Wrappers.<MsgRecordTarget>lambdaQuery()
                                                   .eq(MsgRecordTarget::getChannelCode, channelType)
                                                   .eq(MsgRecordTarget::getUserId, SecurityContextHolder.getPrincipal().getId())
                                                   .eq(MsgRecordTarget::getSendStatus, true)
                                                   .eq(MsgRecordTarget::getHasRead, false));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void readAll(@NonNull ChannelEnum ChannelEnum) {
        //当前用户
        Long userId = SecurityContextHolder.getPrincipal().getId();
        this.baseMapper.readAll(userId, ChannelEnum, null);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void readAllMsgType(@NonNull ChannelEnum ChannelEnum, @NonNull String messageTypeEnum) {
        //当前用户
        Long userId = SecurityContextHolder.getPrincipal().getId();
        this.baseMapper.readAllMsgType(userId, ChannelEnum, messageTypeEnum);
    }

    @Override
    public void readAllApplicationMsg(@NonNull ChannelEnum ChannelEnum, @NonNull String applicationCode) {
        //当前用户
        Long userId = SecurityContextHolder.getPrincipal().getId();
        this.baseMapper.readAllApplicationMsg(userId, ChannelEnum, applicationCode);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void readBatch(@NonNull List<Long> msgRecordIds) {
        if (!CollectionUtils.isEmpty(msgRecordIds)) {
            //当前用户
            Long userId = SecurityContextHolder.getPrincipal().getId();
            this.baseMapper.readBatch(userId, msgRecordIds);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long msgRecordId) {
        //当前用户
        Long userId = SecurityContextHolder.getPrincipal().getId();
        MsgRecordTarget msgRecordTarget = this.lambdaQuery()
                .eq(MsgRecordTarget::getMsgRecordId, msgRecordId)
                .eq(MsgRecordTarget::getUserId, userId)
                .eq(MsgRecordTarget::getSendStatus, true)
                .one();
        if (Objects.nonNull(msgRecordTarget)) {
            DatabaseContextHolder.softDelete(this, msgRecordTarget);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatch(@NonNull List<Long> msgRecordIds) {
        if (!CollectionUtils.isEmpty(msgRecordIds)) {
            //当前用户
            Long userId = SecurityContextHolder.getPrincipal().getId();
            List<MsgRecordTarget> msgRecordTargets = this.lambdaQuery()
                    .in(MsgRecordTarget::getMsgRecordId, msgRecordIds)
                    .eq(MsgRecordTarget::getUserId, userId)
                    .eq(MsgRecordTarget::getSendStatus, true)
                    .list();
            if (!CollectionUtils.isEmpty(msgRecordTargets)) {
                DatabaseContextHolder.softDelete(this, msgRecordTargets);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void removeBatchByBusinessIds(@NonNull Set<String> businessIds) {

    }
}
