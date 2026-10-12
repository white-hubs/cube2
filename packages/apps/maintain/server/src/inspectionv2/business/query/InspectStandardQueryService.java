package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectStandardQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectStandardTargetVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectStandardVO;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface InspectStandardQueryService extends IService<InspectStandardPO> {

    InspectStandardVO getVOById(Long id);

    Pagination<InspectStandardVO> getPage(InspectStandardQueryParam param);

    List<InspectStandardVO> getList(InspectStandardQueryParam param);

    List<InspectStandardVO> getListWithResult(List<Long> standardIds);

    /**
     * 根据 目标对象ID 获取标准
     *
     * @param targetCategory 目标类别
     * @param targetId       目标ID
     *
     * @return 巡检标准
     */
    InspectStandardTargetVO getVoByTarget(String targetCategory, Long targetId);

    List<InspectStandardVO> getStandardsByTargetType(String targetCategory, String targetType);
}
