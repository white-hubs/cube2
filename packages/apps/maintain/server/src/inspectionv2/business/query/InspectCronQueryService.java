package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectCronQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectCronPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectCronVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface InspectCronQueryService extends IService<InspectCronPO> {

    InspectCronVO getVoById(Long id);

    Pagination<InspectCronVO> getPage(InspectCronQueryParam queryParam);

    List<InspectCronVO> getList(InspectCronQueryParam queryParam);
}
