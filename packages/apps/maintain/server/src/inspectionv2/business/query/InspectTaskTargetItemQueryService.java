package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTaskTargetItemPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionStandardVO;

import java.util.List;

public interface InspectTaskTargetItemQueryService extends IService<InspectTaskTargetItemPO> {
    /**
     * 获取任务对象的巡检内容
     *
     * @param taskId 任务id
     * @param objId  对象id
     *
     * @return 巡检内容
     */
    List<InspectionStandardVO> getObjStandardList(Long taskId, Long objId);
}
