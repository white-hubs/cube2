package com.guhecloud.cube.biz.door.server.queryparam;

import com.guhecloud.platform.base.model.Paging;
import lombok.Data;

import java.util.List;

@Data
public class EventRecordQueryParam extends Paging {
    //关键字
    private String keyword;
    //结果
    private String eventResult;
    //时间
    private String period;

    private String beginTime;

    private String endTime;

    private List<Long> ids;

    private Long areaId;

    private Long sceneType;
    // 三方设备ids
    private List<String> thirdPartyIds;

}
