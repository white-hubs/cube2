package com.guhecloud.cube.biz.adapter.park.server.application.iot.service;

import com.guhecloud.cube.biz.adapter.park.server.application.AbstractParkingService;
import com.guhecloud.cube.biz.adapter.park.server.application.iot.facade.event.IotParkingInfoEvent;
import com.guhecloud.cube.biz.adapter.park.server.application.iot.facade.handler.IotParkingInfoEventHandler;
import com.guhecloud.cube.biz.adapter.park.server.application.iot.facade.response.IotDeviceResponse;
import com.guhecloud.cube.biz.adapter.park.server.application.iot.facade.response.KeyValueWithTs;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingInRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingOutRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.param.ParkingPayRecordQueryParam;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingInfoVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingOutRecordVO;
import com.guhecloud.cube.biz.adapter.park.server.vo.ParkingPayRecordVO;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.subsystem.config.constant.SubsystemTypeConstants;
import com.guhecloud.subsystem.config.listener.nacos.entity.SubsystemAdapterConfig;
import com.guhecloud.subsystem.config.redis.SubsystemRedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IotParkingService extends AbstractParkingService {

    private final IotParkingInfoEventHandler iotParkingInfoEventHandler;

    private final SubsystemRedisUtils subsystemRedisUtils;

    @Override
    public List<ParkingInfoVO> getParkingInfo() {
        String key = SubsystemTypeConstants.PARKING_SERVER + ":" + "iot" + ":" + SessionContextHolder.getRequester().getTenant().toString();
        SubsystemAdapterConfig subAdapterRefreshConfig = subsystemRedisUtils.getSubAdapterRefreshConfig(key);
        Map manufacturerJson = (Map<String, Object>) subAdapterRefreshConfig.getManufacturerJson();
        Object prodCode = manufacturerJson.get("prodCode");
        List<IotDeviceResponse> iotParkingInfo = iotParkingInfoEventHandler.getIotParkingInfo(new IotParkingInfoEvent(this, subAdapterRefreshConfig.getTenant(), prodCode.toString()));

        ArrayList<ParkingInfoVO> objects = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(iotParkingInfo)) {

            for (IotDeviceResponse iotDeviceResponse : iotParkingInfo) {
                Integer remainSpace = 0;
                Integer inSpace = 0;
                Integer totalSpace = 0;
                String parkId = "";
                List<KeyValueWithTs> snapshots = iotDeviceResponse.getSnapshots();
                if (CollectionUtils.isNotEmpty(snapshots)) {
                    for (KeyValueWithTs snapshot : snapshots) {
                        if (snapshot.getKey().equals("remainSpace")) {
                            remainSpace = Integer.valueOf(snapshot.getValue().toString());
                        }
                        if (snapshot.getKey().equals("inSpace")) {
                            inSpace = Integer.valueOf(snapshot.getValue().toString());
                        }
                        if (snapshot.getKey().equals("totalSpace")) {
                            totalSpace = Integer.valueOf(snapshot.getValue().toString());
                        }
                        if (snapshot.getKey().equals("parkId")) {
                            parkId = snapshot.getValue().toString();
                        }
                    }

                }

                ParkingInfoVO parkingInfoVO = new ParkingInfoVO(parkId, iotDeviceResponse.getDeviceName(), totalSpace, inSpace, remainSpace);

                objects.add(parkingInfoVO);
            }
        }

        return objects;
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
}
