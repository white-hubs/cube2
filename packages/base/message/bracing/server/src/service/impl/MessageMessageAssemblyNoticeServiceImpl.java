package com.guhecloud.cube.base.bracing.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.bracing.dao.MessageAssemblyNoticeMapper;
import com.guhecloud.cube.base.bracing.dto.AssemblyNoticeWayDTO;
import com.guhecloud.cube.base.bracing.dto.MessageConfigDTO;
import com.guhecloud.cube.base.bracing.dto.SceneConfigDTO;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendConfig;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendParam;
import com.guhecloud.cube.base.bracing.dto.data.MessageSendSceneQueryPram;
import com.guhecloud.cube.base.bracing.dto.request.AssemblyNoticeObjDTO;
import com.guhecloud.cube.base.bracing.po.MessageAssemblyNotice;
import com.guhecloud.cube.base.bracing.po.MessageAssemblyNoticeObj;
import com.guhecloud.cube.base.bracing.po.MessageAssemblyNoticeWay;
import com.guhecloud.cube.base.bracing.service.MessageAssemblyNoticeObjService;
import com.guhecloud.cube.base.bracing.service.MessageAssemblyNoticeService;
import com.guhecloud.cube.base.bracing.service.MessageAssemblyNoticeWayService;
import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageChannelEnum;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageNotifyTargetEnum;
import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;
import com.guhecloud.cube.base.message.sdk.api.request.MessageChannelRequest;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSpecifyTmpSendRequest;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.cube.base.message2.client.SatMsgSceneReceiveClient;
import com.guhecloud.cube.base.message2.client.dto.SatMsgSceneReceiveDTO;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.BeanUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wql
 * @describe 消息配置
 * @date 2023/9/1 17:52
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class MessageMessageAssemblyNoticeServiceImpl extends ServiceImpl<MessageAssemblyNoticeMapper, MessageAssemblyNotice> implements MessageAssemblyNoticeService {

    private final MessageAssemblyNoticeWayService messageAssemblyNoticeWayService;
    private final MessageAssemblyNoticeObjService messageAssemblyNoticeObjService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addMessageConfig(MessageConfigDTO messageConfig) {
        String businessType = messageConfig.getBusinessType();
        Assert.parameterIsNull(businessType, "业务类型不能为空");
        List<SceneConfigDTO> sceneConfigDTOS = messageConfig.getSceneConfigDTOS();
        if (ObjectUtils.isNotEmpty(sceneConfigDTOS)) {
            Set<String> sceneCodes = sceneConfigDTOS.stream().map(SceneConfigDTO::getSceneCode).collect(Collectors.toSet());
            deleteMessageConfig(businessType, sceneCodes);
            for (SceneConfigDTO config : sceneConfigDTOS) {
                //先删除再新增
                MessageAssemblyNotice notice = MessageAssemblyNotice.builder()
                        .businessType(businessType)
                        .sceneCode(config.getSceneCode())
                        .label(config.getLabel())
                        .value(config.getValue())
                        .config(config.getConfig())
                        .build();
                this.save(notice);
                if (ObjectUtils.isNotEmpty(config.getNoticeWays())) {
                    List<AssemblyNoticeWayDTO> noticeWays = config.getNoticeWays();
                    List<MessageAssemblyNoticeWay> saveNoticeWay = new ArrayList<>();
                    noticeWays.forEach(way -> {
                        MessageAssemblyNoticeWay messageAssemblyNoticeWay = new MessageAssemblyNoticeWay();
                        BeanUtils.copyProperties(way, messageAssemblyNoticeWay);
                        messageAssemblyNoticeWay.setNoticeId(notice.getId());
                        saveNoticeWay.add(messageAssemblyNoticeWay);
                    });
                    messageAssemblyNoticeWayService.saveBatch(saveNoticeWay);
                }
                if (ObjectUtils.isNotEmpty(config.getNoticeObjs())) {
                    List<AssemblyNoticeObjDTO> noticeObjs = config.getNoticeObjs();
                    List<MessageAssemblyNoticeObj> saveNoticeObj = new ArrayList<>();
                    noticeObjs.forEach(obj -> {
                        MessageAssemblyNoticeObj noticeObj = new MessageAssemblyNoticeObj();
                        BeanUtils.copyProperties(obj, noticeObj);
                        noticeObj.setNoticeId(notice.getId());
                        saveNoticeObj.add(noticeObj);
                    });
                    messageAssemblyNoticeObjService.saveBatch(saveNoticeObj);
                }
            }
        }
    }

    @Override
    public List<SceneConfigDTO> getMessageConfigInfo(String businessType, String label) {
        List<MessageAssemblyNotice> list = this.lambdaQuery()
                .eq(MessageAssemblyNotice::getBusinessType, businessType)
                .eq(StringUtils.isNotBlank(label), MessageAssemblyNotice::getLabel, label).list();
        List<SceneConfigDTO> configDTOS = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(list)) {
            list.forEach(notice -> {
                SceneConfigDTO configDTO = new SceneConfigDTO();
                BeanUtils.copyProperties(notice, configDTO);
                List<MessageAssemblyNoticeWay> noticeWays = messageAssemblyNoticeWayService.list(new LambdaQueryWrapper<MessageAssemblyNoticeWay>()
                                                                                                         .eq(MessageAssemblyNoticeWay::getNoticeId, notice.getId()));
                if (ObjectUtils.isNotEmpty(noticeWays)) {
                    List<AssemblyNoticeWayDTO> wayDTO = new ArrayList<>();
                    noticeWays.forEach(way -> {
                        AssemblyNoticeWayDTO dto = new AssemblyNoticeWayDTO();
                        BeanUtils.copyProperties(way, dto);
                        wayDTO.add(dto);
                    });
                    configDTO.setNoticeWays(wayDTO);
                }
                List<MessageAssemblyNoticeObj> noticeObjs = messageAssemblyNoticeObjService.list(new LambdaQueryWrapper<MessageAssemblyNoticeObj>()
                                                                                                         .eq(MessageAssemblyNoticeObj::getNoticeId, notice.getId()));
                if (ObjectUtils.isNotEmpty(noticeObjs)) {
                    List<AssemblyNoticeObjDTO> objDTO = new ArrayList<>();
                    noticeObjs.forEach(obj -> {
                        AssemblyNoticeObjDTO dto = new AssemblyNoticeObjDTO();
                        BeanUtils.copyProperties(obj, dto);
                        objDTO.add(dto);
                    });
                    configDTO.setNoticeObjs(objDTO);
                }
                configDTOS.add(configDTO);
            });
        }
        return configDTOS;
    }

    @Override
    public MessageSendResponse sendMsg(MessageSendParam sendParam) {
        MessageAssemblyNotice notice = this.getById(sendParam.getNotifyId());
        Assert.parameterIsNull(notice, "没有找到对应的消息配置");
        List<MessageNotifyTarget> notifyTargets = getMsgNotifyUsers(sendParam.getNotifyUsers(), notice.getId());
        List<MessageChannelRequest> channels = getMsgNotifyChannels(notice);
        MessageSpecifyTmpSendRequest message = MessageSpecifyTmpSendRequest.builder()
                .tenant(SessionContextHolder.getRequester().getTenant())
                .applicationCode(
                        StringUtils.isNotBlank(sendParam.getApplicationCode()) ? sendParam.getApplicationCode() : notice.getBusinessType())
                .applicationName(sendParam.getApplicationName())
                .msgSceneCode(StringUtils.isNotBlank(sendParam.getMsgSceneCode()) ? sendParam.getMsgSceneCode() : notice.getSceneCode())
                .businessId(sendParam.getBusinessId())
                .tmpParams(sendParam.getTempParams())
                .messageNotifyTargets(notifyTargets)
                .channel(channels)
                .businessPrincipal(sendParam.getBusinessPrincipal())
                .build();
        MessageSendClient sendClient = ApplicationContextHolder.getBean(MessageSendClient.class);
        MessageSendResponse messageSendResponse = null;
        try {
            messageSendResponse = sendClient.sceneSpecifyTmpSend(message);
            log.info("消息发送结果,{}", JSON.toJSONString(messageSendResponse));
        } catch (Exception e) {
            log.error("消息发送失败:{}", e.getMessage());
        }
        return messageSendResponse;
    }

    private List<MessageChannelRequest> getMsgNotifyChannels(MessageAssemblyNotice notice) {
        SatMsgSceneReceiveClient receiveClient = ApplicationContextHolder.getBean(SatMsgSceneReceiveClient.class);
        Set<String> set = new HashSet<>();
        SatMsgSceneReceiveDTO receiveByCode = receiveClient.getSatMsgSceneReceiveByCode(notice.getSceneCode());
        if (ObjectUtils.isNotEmpty(receiveByCode) && ObjectUtils.isNotEmpty(receiveByCode.getChannelCodes())) {
            receiveByCode.getChannelCodes().stream().map(Enum::name).forEach(set::add);
            log.info("消息2.0配置的消息通道:{}", JSON.toJSONString(set));
        }
        List<MessageChannelRequest> channels = new ArrayList<>();
        List<MessageAssemblyNoticeWay> noticeWays = messageAssemblyNoticeWayService.list(new LambdaQueryWrapper<MessageAssemblyNoticeWay>()
                                                                                                 .eq(MessageAssemblyNoticeWay::getNoticeId, notice.getId()));
        if (ObjectUtils.isNotEmpty(noticeWays)) {
            noticeWays.forEach(noticeWay -> {
                if (set.contains(noticeWay.getNoticeWay())) {
                    channels.add(MessageChannelRequest.builder()
                                         .channelCode(MessageChannelEnum.valueOf(noticeWay.getNoticeWay()))
                                         .tenant(noticeWay.getTenant())
                                         .tmpId(noticeWay.getTemplateId())
                                         .build());
                }
            });
        }
        return channels;
    }

    private List<MessageNotifyTarget> getMsgNotifyUsers(Set<Long> notifyUsers, Long noticeId) {
        List<MessageNotifyTarget> notifyTargets = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(notifyUsers)) {
            MessageNotifyTarget notifyTarget = new MessageNotifyTarget();
            notifyTarget.setNotifyTargetType(MessageNotifyTargetEnum.USER);
            notifyTarget.setIds(notifyUsers);
            notifyTargets.add(notifyTarget);
        } else {
            List<MessageAssemblyNoticeObj> noticeObjs = messageAssemblyNoticeObjService.list(new LambdaQueryWrapper<MessageAssemblyNoticeObj>()
                                                                                                     .eq(MessageAssemblyNoticeObj::getNoticeId, noticeId));
            if (ObjectUtils.isNotEmpty(noticeObjs)) {
                for (MessageAssemblyNoticeObj noticeObj : noticeObjs) {
                    MessageNotifyTarget notifyTarget = new MessageNotifyTarget();
                    notifyTarget.setNotifyTargetType(MessageNotifyTargetEnum.valueOf(noticeObj.getObjType()));
                    String objId = noticeObj.getObjId();
                    if (StringUtils.isNotBlank(objId)) {
                        String[] split = objId.split(",");
                        Set<Long> set = new HashSet<>();
                        Arrays.stream(split).mapToLong(Long::parseLong).forEach(set::add);
                        notifyTarget.setIds(set);
                    }
                    notifyTargets.add(notifyTarget);
                }
            }
        }
        return notifyTargets;
    }

    @Override
    public List<MessageSendConfig> getSendMsgConfig(MessageSendSceneQueryPram queryPram) {
        List<String> sceneCodes = queryPram.getSceneCodes();
        Set<String> businessTypeList = Arrays.stream(queryPram.getBusinessType().split(",")).collect(Collectors.toSet());
        List<MessageAssemblyNotice> list = this.list(new LambdaQueryWrapper<MessageAssemblyNotice>()
                                                             .in(MessageAssemblyNotice::getBusinessType, businessTypeList)
                                                             .in(ObjectUtils.isNotEmpty(sceneCodes), MessageAssemblyNotice::getSceneCode, sceneCodes));
        List<MessageSendConfig> configs = new ArrayList<>();
        if (ObjectUtils.isNotEmpty(list)) {
            configs = list.stream().map(notice -> {
                        MessageSendConfig build = MessageSendConfig.builder()
                                .businessType(notice.getBusinessType())
                                .notifyId(notice.getId())
                                .sceneCode(notice.getSceneCode())
                                .value(notice.getValue())
                                .config(notice.getConfig())
                                .build();
                        List<MessageAssemblyNoticeObj> noticeObjs = messageAssemblyNoticeObjService.lambdaQuery()
                                .eq(MessageAssemblyNoticeObj::getNoticeId, notice.getId()).list();
                        List<MessageAssemblyNoticeWay> noticeWay = messageAssemblyNoticeWayService.lambdaQuery()
                                .eq(MessageAssemblyNoticeWay::getNoticeId, notice.getId()).list();
                        build.setNoticeObjList(ObjectUtils.copying(noticeObjs, AssemblyNoticeObjDTO.class));
                        build.setNoticeWayDTOS(ObjectUtils.copying(noticeWay, com.guhecloud.cube.base.bracing.dto.request.AssemblyNoticeWayDTO.class));
                        return build;
                    })
                    .collect(Collectors.toList());
        }
        return configs;
    }

    /**
     * @Author wql
     * @Description 按业务加标签 删除消息配置
     * @Date 10:43 2023/9/4
     **/
    private void deleteMessageConfig(String businessType, Set<String> sceneCodes) {
        List<MessageAssemblyNotice> list = this.lambdaQuery()
                .eq(MessageAssemblyNotice::getBusinessType, businessType)
                .in(ObjectUtils.isNotEmpty(sceneCodes), MessageAssemblyNotice::getSceneCode, sceneCodes).list();
        if (ObjectUtils.isNotEmpty(list)) {
            List<Long> ids = list.stream().map(MessageAssemblyNotice::getId).collect(Collectors.toList());
            this.removeByIds(ids);
            messageAssemblyNoticeWayService.remove(new LambdaQueryWrapper<MessageAssemblyNoticeWay>()
                                                           .in(MessageAssemblyNoticeWay::getNoticeId, ids));
            messageAssemblyNoticeObjService.remove(new LambdaQueryWrapper<MessageAssemblyNoticeObj>()
                                                           .in(MessageAssemblyNoticeObj::getNoticeId, ids));
        }
    }
}
