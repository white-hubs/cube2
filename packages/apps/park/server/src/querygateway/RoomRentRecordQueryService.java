package com.guhecloud.cube.srv.gen.park.management.server.querygateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.RoomRentRecordVO;
import com.guhecloud.cube.srv.gen.park.management.server.po.RoomRentRecordPO;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.query.RoomRentRecordQuery;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface RoomRentRecordQueryService extends IService<RoomRentRecordPO> {

    RoomRentRecordVO getVoById(Long id);

    List<RoomRentRecordVO> getVoList(RoomRentRecordQuery query);

    Pagination<RoomRentRecordVO> getVoPage(RoomRentRecordQuery query);

}
