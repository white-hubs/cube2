package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.converters;

import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.IllegalParkingRecordPO;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.VehiclePenaltyRecordPO;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.vo.VehiclePenaltyRecordVO;

import java.util.Map;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/6 15:41
 * @Description:
 */
public final class VehiclePenaltyRecordConverts {
    public static VehiclePenaltyRecordVO convertToVO(VehiclePenaltyRecordPO t, Map<Long, IllegalParkingRecordPO> parkingRecordPOMap) {
        VehiclePenaltyRecordVO recordVO = new VehiclePenaltyRecordVO();
        recordVO.setPlateNumber(t.getPlateNumber());
        recordVO.setViolationScore(t.getViolationScore());
        recordVO.setIllegalTime(t.getIllegalTime());
        IllegalParkingRecordPO parkingRecordPO = parkingRecordPOMap.getOrDefault(t.getIllegalRecordId(), new IllegalParkingRecordPO());
        recordVO.setOwnerName(parkingRecordPO.getOwnerName());
        recordVO.setOwnerPhone(parkingRecordPO.getOwnerPhone());
        recordVO.setProcessInstanceId(parkingRecordPO.getProcessInstanceId());
        recordVO.setWorKOrderName(parkingRecordPO.getName());
        return recordVO;
    }
}
