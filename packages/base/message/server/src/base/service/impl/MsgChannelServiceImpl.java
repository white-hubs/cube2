package com.guhecloud.cube.base.message2.server.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.message2.server.base.dao.MsgChannelMapper;
import com.guhecloud.cube.base.message2.server.base.entity.MsgChannel;
import com.guhecloud.cube.base.message2.server.base.param.MsgChannelModifyParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgChannelQueryParam;
import com.guhecloud.cube.base.message2.server.base.service.MsgChannelService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 消息通道操作实现
 *
 * @author lanchun
 * @date 2021/11/9 19:22
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MsgChannelServiceImpl extends ServiceImpl<MsgChannelMapper, MsgChannel> implements MsgChannelService {

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull MsgChannelModifyParam param) {
        MsgChannel msgChannel = this.getByCode(param.getCode());
        Assert.isConflict(Objects.nonNull(msgChannel), "通道编号已存在");
        msgChannel = ObjectUtils.copying(param, MsgChannel.class);
        this.baseMapper.insert(msgChannel);
        return msgChannel.getId();
    }

    @Override
    public MsgChannel getByCode(@NonNull String code) {
        return this.baseMapper.selectOne(Wrappers.<MsgChannel>lambdaQuery().eq(MsgChannel::getCode, code));
    }

    @Override
    public List<MsgChannel> listMsgChannels(MsgChannelQueryParam param) {
        return this.lambdaQuery()
                .and(StringUtils.isNotEmpty(param.getKeyword()), q -> q
                        .like(MsgChannel::getName, param.getKeyword())
                        .or()
                        .like(MsgChannel::getCode, param.getKeyword()))
                .list();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull String code) {
        this.baseMapper.delete(Wrappers.<MsgChannel>lambdaQuery().eq(MsgChannel::getCode, code));
    }
}
