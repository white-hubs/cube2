package com.guhecloud.cube.srv.gen.maintain.server.scada.querygateway;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.scada.interfaces.vo.ScadaResourcesVO;
import com.guhecloud.cube.srv.gen.maintain.server.scada.po.ScadaResourcesPO;
import com.guhecloud.cube.srv.gen.maintain.server.scada.querygateway.query.ScadaResourcesQueryParam;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface ScadaResourcesQueryService extends IService<ScadaResourcesPO> {

    ScadaResourcesVO getVOById(Long id);

    Pagination<ScadaResourcesVO> getPage(ScadaResourcesQueryParam param);

    List<ScadaResourcesVO> getList(ScadaResourcesQueryParam param);
}
