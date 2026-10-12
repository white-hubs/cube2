package com.guhecloud.cube.srv.gen.adapter.access.control.server.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.queryparam.DoorEventsParam;
import com.guhecloud.cube.srv.gen.adapter.access.control.server.vo.DoorEventsVo;

/** 海康设备 ISAPI 门禁记录服务。 */
public interface HikIsapiAccessRecordService {

    Page<DoorEventsVo> getDoorEvents(DoorEventsParam param);
}
