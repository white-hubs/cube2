package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectTypeQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTypePO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectTypeVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface InspectTypeQueryService extends IService<InspectTypePO> {

    Pagination<InspectTypeVO> getPage(InspectTypeQueryParam queryParam);

    List<InspectTypeVO> getList(InspectTypeQueryParam queryParam);
}
