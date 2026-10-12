package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectTaskQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.DetailRateResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.StandardResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectTaskVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface InspectTaskQueryService extends IService<InspectTaskPO> {

    InspectTaskVO getVOById(Long id);

    List<StandardResult> getDeailStandardList(Long taskId, Long targetId);

    DetailRateResult getDetailRateResult(Long taskId);

    Pagination<InspectTaskVO> getPage(InspectTaskQueryParam param);

    Pagination<InspectTaskVO> getPageByTarget(String targetCategory, Long targetId, InspectTaskQueryParam param);

    Pagination<InspectTaskVO> getMyTask(InspectTaskQueryParam param);

}
