package com.guhecloud.cube.base.message2.server.base.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.message2.server.base.dao.MsgTmpMapper;
import com.guhecloud.cube.base.message2.server.base.entity.MsgScene;
import com.guhecloud.cube.base.message2.server.base.entity.MsgTmp;
import com.guhecloud.cube.base.message2.server.base.param.MsgTmpCopyParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgTmpModifyParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgTmpQueryParam;
import com.guhecloud.cube.base.message2.server.base.param.MsgTmpUpdateParam;
import com.guhecloud.cube.base.message2.server.base.service.MsgSceneService;
import com.guhecloud.cube.base.message2.server.base.service.MsgTmpService;
import com.guhecloud.cube.base.message2.server.base.vo.MsgTmpPageVO;
import com.guhecloud.cube.base.message2.server.initialization.rocketmq.message.MsgTmpAddMessage;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 消息模板管理
 *
 * @author lanchun
 * @date 2023/6/25 15:51
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MsgTmpServiceImpl extends ServiceImpl<MsgTmpMapper, MsgTmp> implements MsgTmpService {

    private final MsgSceneService msgSceneService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long save(@NonNull MsgTmpModifyParam param) {
        MsgScene msgScene = this.msgSceneService.getByCode(param.getMsgSceneCode());
        MsgTmp msgTmp = ObjectUtils.copying(param, MsgTmp.class);
        msgTmp.setMsgType(msgScene.getMsgType());
        this.baseMapper.insert(msgTmp);
        RocketMQContextHolder.sendTransactionalMessage(MsgTmpAddMessage.TOPIC,
                                                       MsgTmpAddMessage.builder()
                                                               .id(msgTmp.getId())
                                                               .name(msgTmp.getName())
                                                               .content(msgTmp.getContent())
                                                               .channelCode(msgTmp.getChannelCode())
                                                               .msgType(msgTmp.getMsgType())
                                                               .enabled(msgTmp.getEnabled())
                                                               .msgSceneCode(msgTmp.getMsgSceneCode())
                                                               .msgSceneName(msgScene.getName())
                                                               .applicationCode(msgTmp.getApplicationCode())
                                                               .applicationName(msgTmp.getApplicationName())
                                                               .build(), () -> RocketMQLocalTransactionState.COMMIT);
        return msgTmp.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull MsgTmpUpdateParam param) {
        MsgTmp msgTmp = this.getById(id);
        Assert.isNotFound(msgTmp, "模板不存在");
        //不包含的通道进行删除
        ObjectUtils.copying(param, msgTmp);
        this.baseMapper.updateById(msgTmp);
    }

    @Override
    public Pagination<MsgTmpPageVO> getMsgTmpPage(MsgTmpQueryParam param) {
        Page<MsgTmp> msgTmpPage = this.baseMapper.selectPage(PagingUtils.convert(param), Wrappers.<MsgTmp>lambdaQuery()
                .eq(Objects.nonNull(param.getChannelCode()), MsgTmp::getChannelCode, param.getChannelCode())
                .eq(StringUtils.isNotEmpty(param.getApplicationCode()), MsgTmp::getApplicationCode, param.getApplicationCode())
                .like(StringUtils.isNotEmpty(param.getKeyword()), MsgTmp::getName, param.getKeyword())
                .orderByDesc(MsgTmp::getCreateTime));
        List<MsgTmpPageVO> msgTmpPageVOS = ObjectUtils.copying(msgTmpPage.getRecords(), MsgTmpPageVO.class);
        getMsgTmpSceneName(msgTmpPageVOS);
        return PagingUtils.convert(msgTmpPage, msgTmpPageVOS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void copyChannelMsgTmp(MsgTmpCopyParam param) {
        List<MsgTmp> msgTmpList = this.baseMapper.selectList(Wrappers.<MsgTmp>lambdaQuery()
                                                                     .eq(MsgTmp::getChannelCode, param.getFromChannel()));
        if (CollectionUtils.isEmpty(msgTmpList)) {
            return;
        }
        List<MsgTmp> newChannelMsgTmpList = new ArrayList<>();
        msgTmpList.forEach(msgTmp -> {
            MsgTmp newChannelMsgTmp = new MsgTmp();
            newChannelMsgTmp.setMsgType(msgTmp.getMsgType());
            newChannelMsgTmp.setChannelCode(param.getToChannel());
            newChannelMsgTmp.setEnabled(msgTmp.getEnabled());
            newChannelMsgTmp.setMsgSceneCode(msgTmp.getMsgSceneCode());
            newChannelMsgTmp.setContent(msgTmp.getContent());
            newChannelMsgTmp.setName(msgTmp.getName());
            newChannelMsgTmp.setApplicationCode(msgTmp.getApplicationCode());
            newChannelMsgTmp.setApplicationName(msgTmp.getApplicationName());
            newChannelMsgTmpList.add(newChannelMsgTmp);
        });
        this.saveBatch(newChannelMsgTmpList);
    }

    private void getMsgTmpSceneName(List<MsgTmpPageVO> msgTmpPageVOS) {
        Set<String> msgSceneCodes = msgTmpPageVOS.stream().map(MsgTmpPageVO::getMsgSceneCode).collect(Collectors.toSet());
        Map<String, MsgScene> msgSceneMap = this.msgSceneService.listMsgScenesByCodes(msgSceneCodes).stream()
                .collect(Collectors.toMap(MsgScene::getCode, obj -> obj, (o, n) -> n));
        msgTmpPageVOS.forEach(msgTmpPageVO -> {
            MsgScene msgScene = msgSceneMap.get(msgTmpPageVO.getMsgSceneCode());
            if (Objects.nonNull(msgScene)) {
                msgTmpPageVO.setMsgSceneName(msgScene.getName());
            }
        });
    }

    @Override
    public List<MsgTmpPageVO> getMsgTmpListByApplicationCodes(@NonNull Collection<String> applicationCodes) {
        List<MsgTmp> msgTmpList = CollectionUtils.isEmpty(applicationCodes) ? Collections.emptyList() : this.lambdaQuery()
                .in(MsgTmp::getApplicationCode, applicationCodes)
                .list();
        if (CollectionUtils.isEmpty(msgTmpList)) {
            return Collections.emptyList();
        }
        List<MsgTmpPageVO> msgTmpPageVOS = ObjectUtils.copying(msgTmpList, MsgTmpPageVO.class);
        getMsgTmpSceneName(msgTmpPageVOS);
        return msgTmpPageVOS;
    }

    @Override
    public MsgTmp getById(@NonNull Long id) {
        return this.baseMapper.selectById(id);
    }

    @Override
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }
}
