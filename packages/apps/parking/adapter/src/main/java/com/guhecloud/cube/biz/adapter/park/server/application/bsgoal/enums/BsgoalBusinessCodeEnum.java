package com.guhecloud.cube.biz.adapter.park.server.application.bsgoal.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BsgoalBusinessCodeEnum {
    AREA_GET_LIST("AreaGetList_001", "车场区域信息"),
    LOT_GET_LIST("LotGetList_001", "获取车位数"),
    IO_GET_LIST("IOGetList_001", "获取车牌进出记录列表接口"),
    ;
    private final String code;
    private final String desc;

}
