package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectPlanQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectPlanPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.StandardResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectPlanVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface InspectPlanQueryService extends IService<InspectPlanPO> {

    InspectPlanVO getVOById(Long id);

    List<StandardResult> getDeailStandardList(Long plainId, Long targetId);

    Pagination<InspectPlanVO> getPage(InspectPlanQueryParam param);

    String getPlanName(Long id);
}
