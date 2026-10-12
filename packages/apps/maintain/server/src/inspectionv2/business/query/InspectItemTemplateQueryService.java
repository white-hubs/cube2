package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectItemTemplateQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectItemTemplatePO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectItemTemplateVO;
import com.guhecloud.platform.base.model.Pagination;

public interface InspectItemTemplateQueryService extends IService<InspectItemTemplatePO> {

    Pagination<InspectItemTemplateVO> getPage(InspectItemTemplateQueryParam param);

    InspectItemTemplateVO getInspectItemDetail(Long id);
}
