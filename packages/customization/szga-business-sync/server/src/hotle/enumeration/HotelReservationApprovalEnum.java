package com.guhecloud.cube.srv.gen.business.info.sync.server.hotle.enumeration;

import com.guhecloud.cube.srv.gen.business.info.sync.server.hotle.vo.HotelReservationVO;
import lombok.Getter;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2025/5/27 17:34
 * @Description:
 */

@Getter
public enum HotelReservationApprovalEnum {

    HOTEL_RESERVATION("酒店预约", HotelReservationVO.class);

    private final String desc;

    private final Class clazz;

    HotelReservationApprovalEnum(String desc, Class clazz) {
        this.desc = desc;
        this.clazz = clazz;
    }
}
