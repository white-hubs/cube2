package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordToBusinessPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordToBusinessVO;

import java.util.List;
import java.util.Map;

public interface InspectRecordToBusinessQueryService extends IService<InspectRecordToBusinessPO> {

    List<InspectRecordToBusinessVO> getListByRecordId(Long recordId);

    /**
     * 根据巡检记录id集合批量获取转工单信息
     *
     * @param recordIdList 记录id集合
     *
     * @return map key:记录id，value:转工单信息集合
     */
    Map<Long, List<InspectRecordToBusinessVO>> getMapByRecordIdList(List<Long> recordIdList);
}
