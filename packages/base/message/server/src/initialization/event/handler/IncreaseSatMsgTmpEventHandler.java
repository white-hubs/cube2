package com.guhecloud.cube.base.message2.server.initialization.event.handler;

import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.ManufacturerCodeEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.MsgTmpStatusEnum;
import com.guhecloud.cube.base.message2.server.handler.handler.tmp.MsgChannelManufacturerTmpHandler;
import com.guhecloud.cube.base.message2.server.handler.handler.tmp.MsgChannelManufacturerTmpHandlerHolder;
import com.guhecloud.cube.base.message2.server.handler.handler.tmp.site.dto.SiteTerminal;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.IncreaseMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.vo.TmpVO;
import com.guhecloud.cube.base.message2.server.initialization.event.IncreaseSatMsgTmpEvent;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgChannel;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgManufacturer;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgTmp;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgTmpManufacturer;
import com.guhecloud.cube.base.message2.server.sat.param.SatMsgManufacturerModifyParam;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgChannelService;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgManufacturerService;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgTmpManufacturerService;
import com.guhecloud.cube.biz.ouaa.client.TerminalClient;
import com.guhecloud.cube.biz.ouaa.client.dto.TerminalDTO;
import com.guhecloud.cube.biz.ouaa.client.param.TerminalQueryParam;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.model.Pagination;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IncreaseSatMsgTmpEventHandler {

    private final SatMsgChannelService satMsgChannelService;

    private final SatMsgManufacturerService satMsgManufacturerService;

    private final SatMsgTmpManufacturerService satMsgTmpManufacturerService;

    private final MsgChannelManufacturerTmpHandlerHolder msgChannelManufacturerTmpHandlerHolder;

    private final TerminalClient terminalClient;

    @EventListener
    @Transactional(rollbackFor = Exception.class)
    public void initMsgTmpManufacturer(final IncreaseSatMsgTmpEvent increaseSatMsgTmpEvent) {
        List<SatMsgChannel> satMsgChannelList = satMsgChannelService.listSatMsgChannels();
        if (CollectionUtils.isEmpty(satMsgChannelList)) {
            return;
        }
        Set<Long> satMsgChannelIds = satMsgChannelList.stream()
                .map(SatMsgChannel::getSatMsgManufacturerId)
                .collect(Collectors.toSet());
        List<SatMsgManufacturer> satMsgManufacturerList = this.satMsgManufacturerService
                .listSatMsgManufacturerByIds(satMsgChannelIds);
        if (satMsgChannelList.stream().noneMatch(satMsgChannel -> ChannelEnum.CHANNEL_SITE == satMsgChannel.getChannelCode())) {
            SatMsgManufacturerModifyParam param = new SatMsgManufacturerModifyParam();
            param.setChannelCode(ChannelEnum.CHANNEL_SITE);
            param.setManufacturerCode(ManufacturerCodeEnum.GUHE_YUN);
            //新增通道厂家配置
            Long satMsgManufacturerId = this.satMsgManufacturerService.save(param);
            SatMsgChannel satMsgChannel = SatMsgChannel.builder()
                    .channelCode(ChannelEnum.CHANNEL_SITE)
                    .channelName(ChannelEnum.CHANNEL_SITE.getDescription())
                    .enabled(true)
                    .satMsgManufacturerId(satMsgManufacturerId)
                    .build();
            this.satMsgChannelService.save(satMsgChannel);
            satMsgChannelList.add(satMsgChannel);
            TerminalQueryParam terminalQueryParam = new TerminalQueryParam();
            terminalQueryParam.setPageNumber(1);
            terminalQueryParam.setPageSize(1000);
            Pagination<TerminalDTO> terminalPage = terminalClient.listByPage(terminalQueryParam);
            String tmpConfig = null;
            if (!CollectionUtils.isEmpty(terminalPage.getRecords())) {
                SiteTerminal siteTerminal = new SiteTerminal();
                siteTerminal.setTerminals(terminalPage.getRecords().stream().map(TerminalDTO::getCode)
                                                  .collect(Collectors.toList()));
                tmpConfig = JsonUtils.serialize(siteTerminal);
            }
            satMsgManufacturerList.add(SatMsgManufacturer.builder()
                                               .id(satMsgManufacturerId)
                                               .channelCode(ChannelEnum.CHANNEL_SITE)
                                               .manufacturerCode(ManufacturerCodeEnum.GUHE_YUN)
                                               .config(tmpConfig)
                                               .build());
        }
        Map<ChannelEnum, List<SatMsgTmp>> channelTmpMap = increaseSatMsgTmpEvent.getSatMsgTmpList()
                .stream()
                .collect(Collectors.groupingBy(SatMsgTmp::getChannelCode));
        List<SatMsgTmpManufacturer> satMsgTmpManufacturers = new ArrayList<>();
        satMsgManufacturerList.forEach(satMsgManufacturer -> {
            List<SatMsgTmp> channelManufacturerMsgTmpList = channelTmpMap.get(satMsgManufacturer.getChannelCode());
            if (!CollectionUtils.isEmpty(channelManufacturerMsgTmpList)) {
                MsgChannelManufacturerTmpHandler manufacturerTmpHandler = this.msgChannelManufacturerTmpHandlerHolder.getHandler(satMsgManufacturer.getChannelCode(), satMsgManufacturer.getManufacturerCode());
                channelManufacturerMsgTmpList.forEach(channelManufacturerMsgTmp -> {
                    TmpVO tmpVO = getManufacturerHandlerTmpVO(satMsgManufacturer, manufacturerTmpHandler, channelManufacturerMsgTmp);
                    SatMsgTmpManufacturer satMsgTmpManufacturer = buildSatMsgTmpManufacturer(satMsgManufacturer, channelManufacturerMsgTmp, tmpVO);
                    satMsgTmpManufacturers.add(satMsgTmpManufacturer);
                });

            }
        });
        this.satMsgTmpManufacturerService.saveBatch(satMsgTmpManufacturers);
    }

    /**
     * 获取厂家模板信息
     *
     * @param satMsgManufacturer
     * @param manufacturerTmpHandler
     * @param channelManufacturerMsgTmp
     *
     * @return
     */
    private TmpVO getManufacturerHandlerTmpVO(SatMsgManufacturer satMsgManufacturer,
                                              MsgChannelManufacturerTmpHandler manufacturerTmpHandler, SatMsgTmp channelManufacturerMsgTmp
                                             ) {
        return manufacturerTmpHandler.increaseTmp(IncreaseMessageTmp.builder()
                                                          .channelCode(channelManufacturerMsgTmp.getChannelCode())
                                                          .manufacturerCode(satMsgManufacturer.getManufacturerCode())
                                                          .tmpName(channelManufacturerMsgTmp.getName())
                                                          .accountInfo(satMsgManufacturer.getConfig())
                                                          .tmpRule(null)
                                                          .content(channelManufacturerMsgTmp.getContent())
                                                          .tmpConfig(null)
                                                          .build());
    }

    /**
     * 构造厂家模板实体信息
     *
     * @param satMsgManufacturer
     * @param channelManufacturerMsgTmp
     * @param tmpVO
     *
     * @return
     */
    private SatMsgTmpManufacturer buildSatMsgTmpManufacturer(SatMsgManufacturer satMsgManufacturer, SatMsgTmp channelManufacturerMsgTmp,
                                                             TmpVO tmpVO
                                                            ) {
        return SatMsgTmpManufacturer
                .builder()
                .satMsgTmpId(channelManufacturerMsgTmp.getId())
                .channelCode(channelManufacturerMsgTmp.getChannelCode())
                .satMsgManufacturerId(satMsgManufacturer.getId())
                .manufacturerCode(satMsgManufacturer.getManufacturerCode().name())
                .manufacturerTmpConfig(Objects.nonNull(tmpVO) && Boolean.TRUE.equals(tmpVO.getSucceed()) ? tmpVO.getTmpConfig() : null)
                .status(Objects.nonNull(tmpVO) && Boolean.FALSE.equals(tmpVO.getSucceed()) ? MsgTmpStatusEnum.CREATE_FAIL.name()
                                                                                           : MsgTmpStatusEnum.CREATE_SUCCESS.name())
                .statusDesc(Objects.nonNull(tmpVO) ? tmpVO.getErrorMsg() : null)
                .build();
    }
}
