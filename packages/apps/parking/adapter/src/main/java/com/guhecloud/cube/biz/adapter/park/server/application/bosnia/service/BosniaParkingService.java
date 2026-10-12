package com.guhecloud.cube.biz.adapter.park.server.application.bosnia.service;

import com.guhecloud.cube.biz.adapter.park.server.application.AbstractParkingService;
import com.guhecloud.cube.biz.adapter.park.server.application.bosnia.param.ParkingInRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.application.bosnia.param.ParkingOutRecordReportParam;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ChargeTotalQueryEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingInRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingInfoEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingOutRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.ParkingPayRecordReportEvent;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.handler.ChargeTotalEventHandler;
import com.guhecloud.cube.biz.adapter.park.server.facade.event.handler.ParkingInfoEventHandler;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ChargeTotalRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingInRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingOutRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.request.ParkingPayRecordReportRequest;
import com.guhecloud.cube.biz.adapter.park.server.facade.response.BasicResponse;
import com.guhecloud.cube.biz.adapter.park.server.facade.response.ChargeTotalResponse;
import com.guhecloud.cube.biz.adapter.park.server.facade.response.ParkingInfoResponse;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingInRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingOutRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingPayRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingPayTotalQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInfoVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingOutRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingPayRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingPayTotalVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 博西尼停车场方法实现
 *
 * @author lanchun
 * @date 2022/9/29 11:05
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BosniaParkingService extends AbstractParkingService {

    private final ChargeTotalEventHandler chargeTotalEventHandler;
    private final ParkingInfoEventHandler parkingInfoEventHandler;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public List<ParkingInfoVO> getParkingInfo() {
        List<ParkingInfoResponse> parkingInfoResponses = this.parkingInfoEventHandler.getParkingInfoResponses(new ParkingInfoEvent(this));
        if (CollectionUtils.isEmpty(parkingInfoResponses)) {
            return Collections.emptyList();
        }
        List<ParkingInfoVO> parkingInfoVOS = new ArrayList<>();
        parkingInfoResponses.stream().forEach(parkingInfoResponse -> {
            parkingInfoResponse.getParks().stream().forEach(parking -> {
                ParkingInfoVO parkingInfoVO = new ParkingInfoVO(parking.getNo(), parking.getName(), parking.getTotalNum(),
                                                                parking.getTotalNum() -
                                                                parking.getTotalLeftNum(), parking.getTotalLeftNum());
                parkingInfoVOS.add(parkingInfoVO);
            });
        });
        return parkingInfoVOS;
    }

    @Override
    public List<ParkingInRecordVO> getParkingInRecords(ParkingInRecordQueryParam param) {
        return null;
    }

    @Override
    public List<ParkingOutRecordVO> getParkingOutRecords(ParkingOutRecordQueryParam param) {
        return null;
    }

    @Override
    public List<ParkingPayRecordVO> getParkingPayRecords(ParkingPayRecordQueryParam param) {
        return null;
    }

    @Override
    public ParkingPayTotalVO getParkingPayTotal(ParkingPayTotalQueryParam param) throws Exception {
        ChargeTotalQueryEvent chargeTotalQueryEvent = new ChargeTotalQueryEvent(this, new ChargeTotalRequest(DateUtils.format(param.getBeginTime(), "yyyyMMdd")));
        List<ChargeTotalResponse> chargeTotalResponses = this.chargeTotalEventHandler.getChargeTotalResponses(chargeTotalQueryEvent);
        if (CollectionUtils.isEmpty(chargeTotalResponses)) {
            return null;
        }
        BigDecimal payAmount = new BigDecimal(chargeTotalResponses.get(0).getPayAmount()).setScale(2, BigDecimal.ROUND_DOWN);
        return new ParkingPayTotalVO(payAmount);
    }

    public BasicResponse reportParkingInRecord(@NonNull ParkingInRecordReportParam param) {
        ParkingInRecordReportRequest inRecordReportRequest = ObjectUtils.copying(param, ParkingInRecordReportRequest.class);
        ParkingInRecordReportEvent parkingInRecordReportEvent = new ParkingInRecordReportEvent(this, inRecordReportRequest);
        this.applicationEventPublisher.publishEvent(parkingInRecordReportEvent);
        return parkingInRecordReportEvent.get();
    }

    public BasicResponse reportParkingOutRecord(@NonNull ParkingOutRecordReportParam param) {
        //出场记录
        ParkingOutRecordReportRequest outRecordReportRequest = ObjectUtils.copying(param, ParkingOutRecordReportRequest.class);
        ParkingOutRecordReportEvent parkingOutRecordReportEvent = new ParkingOutRecordReportEvent(this, outRecordReportRequest);
        this.applicationEventPublisher.publishEvent(parkingOutRecordReportEvent);
        return parkingOutRecordReportEvent.get();
    }

    public BasicResponse reportParkingFeeRecord(@NonNull ParkingOutRecordReportParam param) {
        //缴费记录
        ParkingPayRecordReportRequest payRecordReportRequest = ObjectUtils.copying(param, ParkingPayRecordReportRequest.class);
        ParkingPayRecordReportEvent parkingPayRecordReportEvent = new ParkingPayRecordReportEvent(this, payRecordReportRequest);
        this.applicationEventPublisher.publishEvent(parkingPayRecordReportEvent);
        return parkingPayRecordReportEvent.get();
    }
}
