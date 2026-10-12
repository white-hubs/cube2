package com.guhecloud.cube.sn.meattrade.plugin.utils;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.guhecloud.cube.sn.meattrade.plugin.common.entity.PorkArrange;
import com.guhecloud.cube.sn.meattrade.plugin.common.entity.PorkArrangeDetail;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.api.request.SetTallyDataRequest;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.rocketmq.message.PorkArrangeDetailMessage;
import com.guhecloud.cube.sn.meattrade.plugin.dbc.rocketmq.message.PorkArrangeMessage;
import com.guhecloud.platform.base.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 理货数据转换工具类
 *
 * @author lanchun
 * @date 2024/9/30 15:07
 */
public class PorkArrangeConvertUtils {

    private PorkArrangeConvertUtils() {}

    public static PorkArrange convertMessageTOPorkArrange(PorkArrangeMessage message) {
        return PorkArrange.builder()
                .id(message.getId())
                .guid(message.getGuid())
                .deviceId(message.getTerminalNumber())
                .tallyId(message.getPorkReportCode())
                .eventType(message.getTallyMachineEventType())
                .tenant(message.getTenant())
                .startTime(message.getStartTime())
                .endTime(message.getEndTime())
                .arrangeDetails(convertPorkArrangeDetail(message.getPorkArrangeDetailMessages()))
                .build();
    }

    public static List<PorkArrangeDetail> convertPorkArrangeDetail(List<PorkArrangeDetailMessage> porkArrangeDetailMessages) {
        return porkArrangeDetailMessages.stream().map(PorkArrangeConvertUtils::getBuild
                                                     ).collect(Collectors.toList());
    }

    public static PorkArrangeDetail getBuild(PorkArrangeDetailMessage detail) {
        return PorkArrangeDetail.builder()
                .code(detail.getCode())
                .supplierId(detail.getSupplierId())
                .porkReportId(detail.getPorkReportId())
                .porkReportCode(detail.getPorkReportCode())
                .label(detail.getLabel())
                .footMarkCode(detail.getFootMarkCode())
                .weight(detail.getWeight())
                .weightCompliant(detail.getWeightCompliant())
                .price(detail.getPrice())
                .place(detail.getTerminalNumber())
                .porkArrangeTime(detail.getPorkArrangeTime())
                .passed(detail.getPassed())
                .cheat(detail.getCheat())
                .tenant(detail.getTenant())
                .transferFiles(detail.getTransferFiles())
                .transferPaths(detail.getTransferPaths())
                .build();
    }

    public static SetTallyDataRequest convertPorkArrangeToDbcRequest(PorkArrange porkArrange) {
        SetTallyDataRequest setTallyDataRequest = new SetTallyDataRequest();
        setTallyDataRequest.setGuid(String.valueOf(IdWorker.getId()));
        setTallyDataRequest.setDeviceId(porkArrange.getDeviceId());
        setTallyDataRequest.setTallyId(porkArrange.getTallyId());
        setTallyDataRequest.setEventType(porkArrange.getEventType());
        setTallyDataRequest.setStartTime(porkArrange.getStartTime());
        setTallyDataRequest.setEndTime(porkArrange.getEndTime());
        setTallyDataRequest.setDatas(convertTallyDetailsToDbcRequest(porkArrange.getArrangeDetails()));
        return setTallyDataRequest;
    }

    private static List<SetTallyDataRequest.TallyDetails> convertTallyDetailsToDbcRequest(List<PorkArrangeDetail> arrangeDetails) {
        return arrangeDetails.stream()
                .map(porkArrangeDetail -> {
                    SetTallyDataRequest.TallyDetails tallyDetail = SetTallyDataRequest.TallyDetails.builder()
                            .rfid1(porkArrangeDetail.getLabel())
                            .rfid2(porkArrangeDetail.getFootMarkCode())
                            .weight(porkArrangeDetail.getWeight())
                            .time(porkArrangeDetail.getPorkArrangeTime())
                            .build();
                    if (StringUtils.isNotBlank(porkArrangeDetail.getTransferPaths())) {
                        String[] filePaths = porkArrangeDetail.getTransferPaths().split(",");
                        tallyDetail.setPicPath1(filePaths[0]);
                        tallyDetail.setPicPath2(filePaths[1]);
                    }
                    return tallyDetail;
                }).collect(Collectors.toList());
    }
}
