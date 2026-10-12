package com.guhecloud.cube.srv.gen.park.management.server.querygateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.AreaListVo;
import com.guhecloud.cube.srv.gen.park.management.server.interfaces.vo.RoomScadaResourcesVO;
import com.guhecloud.cube.srv.gen.park.management.server.po.RoomScadaResourcesPO;
import com.guhecloud.cube.srv.gen.park.management.server.querygateway.query.RoomScadaResourcesQuery;

import java.util.List;

public interface RoomScadaResourcesQueryService extends IService<RoomScadaResourcesPO> {

    RoomScadaResourcesVO getVoById(Long id);

    List<RoomScadaResourcesVO> getVOList(RoomScadaResourcesQuery query);

    List<AreaListVo> getVOTree(RoomScadaResourcesQuery query);

}
