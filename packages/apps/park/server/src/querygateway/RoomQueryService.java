package com.guhecloud.cube.srv.gen.park.management.server.querygateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.RoomDeletedVO;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.RoomEnterpriseVO;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.RoomVO;
import com.guhecloud.cube.srv.gen.park.management.server.po.RoomPO;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.query.RoomQuery;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface RoomQueryService extends IService<RoomPO> {

    RoomVO getVoById(Long id);

    List<RoomVO> getVOList(RoomQuery query);

    List<RoomVO> tenantRoom();

    Pagination<RoomVO> getVOPage(RoomQuery query);

    RoomEnterpriseVO getRoomEnterpriseVO(Long id, String name);

    List<RoomDeletedVO> tenantRoomByIds(List<Long> ids);

    List<RoomDeletedVO> tenantStopRoomById(List<Long> ids);

}
