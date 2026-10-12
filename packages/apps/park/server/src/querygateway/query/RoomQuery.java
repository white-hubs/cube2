package com.guhecloud.cube.srv.gen.park.management.server.querygateway.query;

import com.guhecloud.platform.base.model.Paging;
import com.guhecloud.platform.base.model.Period;
import lombok.Data;

@Data
public class RoomQuery extends Paging {

    private String keyword;

    /**
     * 区域ID
     */
    private Long areaId;

    /**
     * 房间类型ID
     */
    private Long roomTypeId;

    /**
     * 状态
     */
    private String status;

    /**
     * 是否被租赁，false为租赁，true已租赁
     */
    private Boolean rent;

    private Period period;
}
