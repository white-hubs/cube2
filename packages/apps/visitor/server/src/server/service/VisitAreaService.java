package com.guhecloud.cube.srv.gen.visitor.management.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.visitor.management.server.po.VisitAreaPO;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.KeyWordPageParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.VisitAreaDetailParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.VisitAreaParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.AreaSelectResult;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.VisitAreaResult;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface VisitAreaService extends IService<VisitAreaPO> {
    List<AreaSelectResult> getAreaList(KeyWordPageParam param);

    Pagination<VisitAreaResult> periodPage(KeyWordPageParam param);

    VisitAreaResult periodDetail(VisitAreaDetailParam param);

    Long addVisitPeriod(VisitAreaParam param);

    void editPeriods(VisitAreaParam param);

    void removePeriod(Long id);
}
