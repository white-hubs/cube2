package com.guhecloud.cube.base.message2.server.sat.process.pipeline.handler;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.MsgSendStatusEnum;
import com.guhecloud.cube.base.message2.server.base.param.MsgSendTargetParam;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgChannel;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgManufacturer;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgTmp;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgTmpManufacturer;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgChannelSendParam;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgSendParam;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.BusinessProcess;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.ProcessContext;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageRecordInfo;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageRecordTargetInfo;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.handler.context.MsgSendContext;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgChannelService;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgManufacturerService;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgTmpManufacturerService;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgTmpService;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AssembleMsgInfoAction implements BusinessProcess<MsgSendContext> {

    private final SatMsgTmpManufacturerService satMsgTmpManufacturerService;
    private final SatMsgChannelService satMsgChannelService;
    private final SatMsgManufacturerService satMsgManufacturerService;
    private final SatMsgTmpService satMsgTmpService;

    @Override
    public void process(ProcessContext<MsgSendContext> context) {
        SatMsgSendParam satMsgSendParam = context.getProcessModel().getSatMsgSendParam();
        Set<ChannelEnum> ChannelEnums = getChannelEnums(satMsgSendParam);
        List<SatMsgChannel> satMsgChannels = satMsgChannelService.getEnableConfigByCodes(ChannelEnums);
        if (CollectionUtils.isEmpty(satMsgChannels)) {
            context.setNeedBreak(true);
            return;
        }
        List<MessageInfo> messageInfos = new ArrayList<>();
        List<MessageRecordInfo> messageRecordInfos = new ArrayList<>();
        for (SatMsgChannel satMsgChannel : satMsgChannels) {
            //获取通道下的模板
            List<SatMsgTmp> channelTmpList = getChannelTmpList(satMsgSendParam, satMsgChannel);
            if (CollectionUtils.isEmpty(channelTmpList)) {
                continue;
            }
            //获取通道发送参数
            SatMsgChannelSendParam satMsgChannelSendParam = this.getSatMsgChannelSendParam(satMsgChannel.getChannelCode(), satMsgSendParam);
            if (Objects.isNull(satMsgChannelSendParam)) {
                continue;
            }
            //获取模板
            SatMsgTmp satMsgTmp = getSatMsgTmp(channelTmpList, satMsgChannelSendParam);
            if (Objects.nonNull(satMsgTmp)) {
                //获取通道厂家信息
                SatMsgManufacturer satMsgManufacturer = satMsgManufacturerService.getById(satMsgChannel.getSatMsgManufacturerId());
                //通道下对应厂家的模板配置
                SatMsgTmpManufacturer satMsgTmpManufacturer = satMsgTmpManufacturerService.getSatMsgTmpManufacturer(
                        satMsgTmp.getId(),
                        satMsgChannel.getChannelCode(),
                        satMsgManufacturer.getId());
                Long sendId = IdWorker.getId();
                String msgContent = replaceMsgParams(satMsgTmp.getContent(), satMsgSendParam.getMsgParams());
                Map<String, String> msgParams = getMsgParams(satMsgTmp.getContent(), satMsgSendParam.getMsgParams());
                MessageRecordInfo messageRecordInfo = buildMessageRecordInfo(satMsgSendParam, satMsgTmp, sendId,
                                                                             msgContent, satMsgTmpManufacturer, satMsgManufacturer, satMsgChannelSendParam);
                messageRecordInfos.add(messageRecordInfo);
                MessageInfo messageInfo = buildSendMessageInfo(satMsgSendParam.getTenant(), satMsgTmp, sendId,
                                                               msgContent, msgParams, messageRecordInfo, satMsgTmpManufacturer, satMsgManufacturer, satMsgChannelSendParam);
                messageInfos.add(messageInfo);
            }
        }
        if (CollectionUtils.isEmpty(messageInfos)) {
            context.setNeedBreak(true);
            return;
        }
        context.getProcessModel().setMessageInfos(messageInfos);
        context.getProcessModel().setMessageRecordInfos(messageRecordInfos);
    }

    /**
     * 获取发送参数里的通道
     *
     * @param satMsgSendParam
     *
     * @return
     */
    private Set<ChannelEnum> getChannelEnums(SatMsgSendParam satMsgSendParam) {
        return satMsgSendParam.getSatMsgChannelSendParams()
                .stream()
                .map(SatMsgChannelSendParam::getChannelCode)
                .collect(Collectors.toSet());
    }

    /**
     * 获取通道下的模板
     *
     * @param satMsgSendParam
     * @param satMsgChannel
     *
     * @return
     */
    private List<SatMsgTmp> getChannelTmpList(SatMsgSendParam satMsgSendParam, SatMsgChannel satMsgChannel) {
        return satMsgTmpService.listSatMsgTmp(satMsgSendParam.getMsgSceneCode(), Collections.singleton(satMsgChannel.getChannelCode()));
    }

    /**
     * 选择使用的模板
     *
     * @param channelTmpList
     * @param msgChannelSendParam
     *
     * @return
     */
    private SatMsgTmp getSatMsgTmp(List<SatMsgTmp> channelTmpList, SatMsgChannelSendParam msgChannelSendParam) {
        return msgChannelSendParam != null && msgChannelSendParam.getTmpId() != null ?
               channelTmpList.stream()
                       .filter(tmp -> tmp.getId().equals(msgChannelSendParam.getTmpId()))
                       .findFirst()
                       .orElse(null) :
               channelTmpList.get(0);
    }

    /**
     * 获取通道发送参数
     *
     * @param channelCode
     * @param satMsgSendParam
     *
     * @return
     */
    private SatMsgChannelSendParam getSatMsgChannelSendParam(ChannelEnum channelCode, SatMsgSendParam satMsgSendParam) {
        return satMsgSendParam.getSatMsgChannelSendParams()
                .stream()
                .filter(satMsgChannelSendParam -> satMsgChannelSendParam.getChannelCode() == channelCode)
                .findFirst()
                .orElse(null);
    }

    /**
     * 替换模板内容
     *
     * @param content
     * @param msgParams
     *
     * @return
     */
    private String replaceMsgParams(String content, Map<String, String> msgParams) {
        return StringUtils.replaceTemplate(content, msgParams);
    }

    /**
     * 过滤无用变量
     *
     * @param content
     * @param msgParams
     *
     * @return
     */
    private Map<String, String> getMsgParams(String content, Map<String, String> msgParams) {
        return msgParams.entrySet().stream()
                .filter(entry -> content.contains("${" + entry.getKey() + "}"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    private MessageRecordInfo buildMessageRecordInfo(SatMsgSendParam satMsgSendParam,
                                                     SatMsgTmp satMsgTmp,
                                                     Long sendId,
                                                     String msgContent,
                                                     SatMsgTmpManufacturer satMsgTmpManufacturer,
                                                     SatMsgManufacturer satMsgManufacturer,
                                                     SatMsgChannelSendParam satMsgChannelSendParam
                                                    ) {
        List<MessageRecordTargetInfo> messageRecordTargetInfos = new ArrayList<>();
        for (MsgSendTargetParam sendTargetParam : satMsgChannelSendParam.getSendTargetParams()) {
            MessageRecordTargetInfo messageRecordTargetInfo = ObjectUtils.copying(sendTargetParam, MessageRecordTargetInfo.class);
            messageRecordTargetInfo.setId(IdWorker.getId());
            messageRecordTargetInfo.setSourceTenant(satMsgSendParam.getSourceTenant());
            messageRecordTargetInfos.add(messageRecordTargetInfo);
        }
        return MessageRecordInfo.builder()
                .id(sendId)
                .msgSceneCode(satMsgTmp.getMsgSceneCode())
                .msgSceneName(satMsgTmp.getMsgSceneName())
                .msgContent(msgContent)
                .msgTmpId(satMsgTmp.getId())
                .msgType(satMsgTmp.getMsgType())
                .tmpConfig(StringUtils.isNotBlank(satMsgChannelSendParam.getExtendConfig()) ? satMsgChannelSendParam.getExtendConfig()
                                                                                            : satMsgTmpManufacturer.getManufacturerTmpConfig())
                .msgParams(satMsgSendParam.getMsgParams())
                .applicationCode(StringUtils.isEmpty(satMsgSendParam.getApplicationCode()) ? satMsgTmp.getApplicationCode()
                                                                                           : satMsgSendParam.getApplicationCode())
                .applicationName(StringUtils.isEmpty(satMsgSendParam.getApplicationName()) ? satMsgTmp.getApplicationName()
                                                                                           : satMsgSendParam.getApplicationName())
                .businessId(satMsgSendParam.getBusinessId())
                .businessPrincipal(satMsgSendParam.getBusinessPrincipal() != null
                                   ? JsonUtils.serialize(satMsgSendParam.getBusinessPrincipal()) : null)
                .channelCode(satMsgTmp.getChannelCode())
                .manufacturerCode(satMsgManufacturer.getManufacturerCode())
                .messageRecordTargetInfos(messageRecordTargetInfos)
                .sendStatus(MsgSendStatusEnum.SENDING)
                .tenant(Objects.isNull(satMsgChannelSendParam.getTenant()) ? satMsgSendParam.getTenant()
                                                                           : satMsgChannelSendParam.getTenant())
                .build();
    }

    private MessageInfo buildSendMessageInfo(Integer tenant,
                                             SatMsgTmp satMsgTmp,
                                             Long sendId,
                                             String msgContent,
                                             Map<String, String> msgParams,
                                             MessageRecordInfo messageRecordInfo,
                                             SatMsgTmpManufacturer satMsgTmpManufacturer,
                                             SatMsgManufacturer satMsgManufacturer,
                                             SatMsgChannelSendParam satMsgChannelSendParam
                                            ) {
        return MessageInfo.builder()
                .id(sendId)
                .title(satMsgTmp.getMsgSceneName())
                .receiver(satMsgChannelSendParam.getSendTargetParams().stream().map(MsgSendTargetParam::getReceiver)
                                  .collect(Collectors.toSet()))
                .channelCode(satMsgTmp.getChannelCode())
                .manufacturerCode(satMsgManufacturer.getManufacturerCode())
                .templateContent(satMsgTmp.getContent())
                .templateContentParams(msgParams)
                .content(msgContent)
                .msgType(satMsgTmp.getMsgType())
                .sendAccountInfo(satMsgManufacturer.getConfig())
                .businessPrincipal(messageRecordInfo)
                .tmpConfig(StringUtils.isNotBlank(satMsgChannelSendParam.getExtendConfig()) ? satMsgChannelSendParam.getExtendConfig()
                                                                                            : satMsgTmpManufacturer.getManufacturerTmpConfig())
                .tenant(Objects.nonNull(satMsgChannelSendParam.getTenant()) ? satMsgChannelSendParam.getTenant() :
                        Objects.nonNull(tenant) ? tenant : SessionContextHolder.getRequester().getTenant())
                .configTenant(satMsgTmp.getTenant())
                .build();
    }
}
