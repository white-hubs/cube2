package com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.converters;

import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.aggregates.IllegalParkingBlacklistInfo;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.domain.po.IllegalParkingBlacklistPO;
import com.guhecloud.cube.biz.subsystem.parking.server.Illegal.interfaces.vo.IllegalParkingBlacklistVO;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/1/6 11:03
 * @Description:
 */
public final class IllegalParkingBlacklistConverts {
    public static List<IllegalParkingBlacklistInfo> convertToBlacklist(List<IllegalParkingBlacklistPO> poList, Boolean add) {
        if (CollectionUtils.isEmpty(poList)) {
            return new ArrayList<>();
        }
        List<IllegalParkingBlacklistInfo> result = new ArrayList<>();
        for (var po : poList) {
            IllegalParkingBlacklistInfo illegalParkingBlacklistInfo = new IllegalParkingBlacklistInfo();
            illegalParkingBlacklistInfo.setOwner(po.getOwner());
            illegalParkingBlacklistInfo.setPlateNumber(po.getPlateNumber());
            illegalParkingBlacklistInfo.setPhone(po.getPhone());
            illegalParkingBlacklistInfo.setParkingLot(po.getParkingLot());
            illegalParkingBlacklistInfo.setType(add);
            illegalParkingBlacklistInfo.setStartTime(DateUtils.format(po.getStartTime()));
            illegalParkingBlacklistInfo.setEndTime(DateUtils.format(po.getEndTime()));
            result.add(illegalParkingBlacklistInfo);
        }

        return result;
    }

    public static IllegalParkingBlacklistVO convertToVO(IllegalParkingBlacklistPO pot, Map<String, List<BigDecimal>> plateNumberScoreMap) {

        IllegalParkingBlacklistVO result = new IllegalParkingBlacklistVO();
        result.setId(pot.getId());
        result.setOwner(pot.getOwner());
        result.setPlateNumber(pot.getPlateNumber());
        result.setPhone(pot.getPhone());
        result.setReason(pot.getReason());
        result.setCreateTime(pot.getCreateTime());
        BigDecimal reduce = plateNumberScoreMap.getOrDefault(pot.getPlateNumber(), new ArrayList<>())
                .stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        result.setViolationScore(reduce);
        return result;
    }
}
