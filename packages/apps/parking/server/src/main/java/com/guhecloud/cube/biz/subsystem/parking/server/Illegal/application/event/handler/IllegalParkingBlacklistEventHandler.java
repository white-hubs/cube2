package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.application.event.handler;

import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.aggregates.IllegalParkingBlacklistInfo;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.event.IllegalParkingBlacklistEvent;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.subsystem.requester.sdk.client.ParkingClient;
import com.guhecloud.subsystem.requester.sdk.param.parking.ParkingBlackParam;
import com.guhecloud.subsystem.requester.sdk.vo.parking.ParkingBlackResultVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 车辆加入黑名单事件处理器
 *
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/7 10:08
 * @Description:
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IllegalParkingBlacklistEventHandler {

    private final ParkingClient parkingClient;

    /**
     * 处理车辆加入黑名单事件
     *
     * @param event 车场配置变更事件
     */
    @EventListener
    public void handleParkingChangeEvent(final IllegalParkingBlacklistEvent event) {
        List<IllegalParkingBlacklistInfo> illegalParkingBlacklistInfoList =
                event.getIllegalParkingBlacklistInfoList();
        if (CollectionUtils.isEmpty(illegalParkingBlacklistInfoList)) {
            log.info("车辆加入黑名单事件为空");
        }
        for (IllegalParkingBlacklistInfo illegalParkingBlacklistInfo : illegalParkingBlacklistInfoList) {
            ParkingBlackParam parkingBlackParam = new ParkingBlackParam();
            parkingBlackParam.setCarNo(illegalParkingBlacklistInfo.getPlateNumber());
            parkingBlackParam.setCarNoColor(illegalParkingBlacklistInfo.getOwner());
            parkingBlackParam.setParkCode(illegalParkingBlacklistInfo.getParkingLot());
            parkingBlackParam.setType(1);
            parkingBlackParam.setIsDeleted(!illegalParkingBlacklistInfo.getType());
            parkingBlackParam.setBeginTime(illegalParkingBlacklistInfo.getStartTime());
            parkingBlackParam.setEndTime(illegalParkingBlacklistInfo.getEndTime());
            try {
                ParkingBlackResultVO parkingBlackResultVO = parkingClient.addParkingBlack(parkingBlackParam);
                if (parkingBlackResultVO != null && Boolean.TRUE.equals(parkingBlackResultVO.getSuccess())) {
                    log.info("黑名单操作成功");
                } else {
                    throw new ServiceException(parkingBlackResultVO.getMsg());
                }
            } catch (Exception e) {
                log.error("黑名单操作失败", e);
            }
        }
    }
}
