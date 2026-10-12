package com.guhecloud.cube.base.message2.server.initialization.event.handler;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.base.message.metadata.enums.ChannelEnum;
import com.guhecloud.cube.base.message2.server.base.enumeration.MsgTmpStatusEnum;
import com.guhecloud.cube.base.message2.server.base.vo.MsgTmpPageVO;
import com.guhecloud.cube.base.message2.server.handler.handler.tmp.MsgChannelManufacturerTmpHandler;
import com.guhecloud.cube.base.message2.server.handler.handler.tmp.MsgChannelManufacturerTmpHandlerHolder;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.IncreaseMessageTmp;
import com.guhecloud.cube.base.message2.server.handler.pending.tmp.vo.TmpVO;
import com.guhecloud.cube.base.message2.server.initialization.event.ChangeSatMsgTmpEvent;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgChannel;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgManufacturer;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgTmp;
import com.guhecloud.cube.base.message2.server.sat.entity.SatMsgTmpManufacturer;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgChannelService;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgManufacturerService;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgTmpManufacturerService;
import com.guhecloud.cube.base.message2.server.sat.service.SatMsgTmpService;
import com.guhecloud.platform.base.util.DateUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChangeSatMsgTmpEventHandler {

    private final SatMsgTmpService satMsgTmpService;

    private final SatMsgChannelService satMsgChannelService;

    private final SatMsgManufacturerService satMsgManufacturerService;

    private final SatMsgTmpManufacturerService satMsgTmpManufacturerService;

    private final MsgChannelManufacturerTmpHandlerHolder msgChannelManufacturerTmpHandlerHolder;

    @EventListener
    @Transactional(rollbackFor = Exception.class)
    public void changeSatMsgTmp(final ChangeSatMsgTmpEvent changeSatMsgTmpEvent) {
        List<MsgTmpPageVO> msgTmpList = changeSatMsgTmpEvent.getMsgTmpList();
        List<SatMsgChannel> satMsgChannelList = satMsgChannelService.listSatMsgChannels();
        if (CollectionUtils.isEmpty(msgTmpList) || CollectionUtils.isEmpty(satMsgChannelList)) {
            return;
        }
        List<Long> msgTmpIds = msgTmpList.stream().map(MsgTmpPageVO::getId).collect(Collectors.toList());
        List<SatMsgTmp> satMsgTmpList = this.satMsgTmpService.list(Wrappers.<SatMsgTmp>lambdaQuery()
                                                                           .in(SatMsgTmp::getMsgTmpId, msgTmpIds));
        if (CollectionUtils.isEmpty(satMsgTmpList)) {
            return;
        }
        Map<ChannelEnum, SatMsgManufacturer> satMsgManufacturerMap = this.satMsgManufacturerService
                .listSatMsgManufacturerByIds(satMsgChannelList.stream().map(SatMsgChannel::getSatMsgManufacturerId)
                                                     .collect(Collectors.toList()))
                .stream()
                .collect(Collectors.toMap(SatMsgManufacturer::getChannelCode, obj -> obj, (o, n) -> n));
        satMsgTmpList.stream().forEach(satMsgTmp -> {
            Optional<MsgTmpPageVO> optionalMsgTmp = msgTmpList.stream().filter(msgTmp -> satMsgTmp.getMsgTmpId().equals(msgTmp.getId()))
                    .findFirst();
            optionalMsgTmp.ifPresent(msgTmp -> {
                if (!msgTmp.getContent().equals(satMsgTmp.getContent())) {
                    satMsgTmp.setContent(msgTmp.getContent());
                    SatMsgManufacturer satMsgManufacturer = satMsgManufacturerMap.get(satMsgTmp.getChannelCode());
                    if (Objects.nonNull(satMsgManufacturer)) {
                        MsgChannelManufacturerTmpHandler manufacturerTmpHandler = this.msgChannelManufacturerTmpHandlerHolder.getHandler(satMsgManufacturer.getChannelCode(), satMsgManufacturer.getManufacturerCode());
                        TmpVO tmpVO = getManufacturerHandlerTmpVO(satMsgManufacturer, manufacturerTmpHandler, satMsgTmp);
                        SatMsgTmpManufacturer satMsgTmpManufacturer = buildSatMsgTmpManufacturer(satMsgManufacturer, satMsgTmp, tmpVO);
                        this.satMsgTmpManufacturerService.update(Wrappers.<SatMsgTmpManufacturer>lambdaUpdate()
                                                                         .set(ChannelEnum.CHANNEL_SITE !=
                                                                              satMsgTmp.getChannelCode(), SatMsgTmpManufacturer::getManufacturerTmpConfig, satMsgTmpManufacturer.getManufacturerTmpConfig())
                                                                         .set(SatMsgTmpManufacturer::getStatus, satMsgTmpManufacturer.getStatus())
                                                                         .set(SatMsgTmpManufacturer::getStatusDesc, satMsgTmpManufacturer.getStatus())
                                                                         .set(SatMsgTmpManufacturer::getUpdateTime, DateUtils.now())
                                                                         .eq(SatMsgTmpManufacturer::getSatMsgTmpId, satMsgTmpManufacturer.getSatMsgTmpId())
                                                                         .eq(SatMsgTmpManufacturer::getManufacturerCode, satMsgManufacturer.getManufacturerCode())
                                                                         .eq(SatMsgTmpManufacturer::getSatMsgManufacturerId, satMsgTmpManufacturer.getSatMsgManufacturerId()));
                    }
                }
            });
        });
        this.satMsgTmpService.updateBatchById(satMsgTmpList);
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
                .satMsgManufacturerId(satMsgManufacturer.getId())
                .manufacturerTmpConfig(Objects.nonNull(tmpVO) && Boolean.TRUE.equals(tmpVO.getSucceed()) ? tmpVO.getTmpConfig() : null)
                .status(Objects.nonNull(tmpVO) && Boolean.FALSE.equals(tmpVO.getSucceed()) ? MsgTmpStatusEnum.CREATE_FAIL.name()
                                                                                           : MsgTmpStatusEnum.CREATE_SUCCESS.name())
                .statusDesc(Objects.nonNull(tmpVO) ? tmpVO.getErrorMsg() : null)
                .build();
    }
}
