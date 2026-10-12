package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectResultOptionCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectResultQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectResultPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectResultSimpleVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectResultVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface InspectResultQueryService extends IService<InspectResultPO> {

    InspectResultVO getById(Long id);

    InspectResultSimpleVO getSimpleVOById(Long id);

    Pagination<InspectResultVO> getPage(InspectResultQueryParam queryParam);

    List<InspectResultVO> getList(InspectResultQueryParam queryParam);

    List<InspectResultOptionCommandModel> getOptions(Long id);
}
