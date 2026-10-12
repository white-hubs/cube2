package com.guhecloud.cube.srv.gen.visitor.management.server.third.lamp.service;

import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.KeyWordPageParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.AreaSelectResult;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.VisitAreaResult;

import java.util.List;

public interface LampAreaService {
    List<AreaSelectResult> getAreaList(KeyWordPageParam param);

    VisitAreaResult areaDetail(Long areaId);
}
