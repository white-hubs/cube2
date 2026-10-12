package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectRecordItemDataMode;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectRecordItemStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordItemQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordItemPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordItemListVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordItemVO;
import com.guhecloud.platform.base.model.Period;

import java.util.List;
import java.util.Map;

public interface InspectRecordItemQueryService extends IService<InspectRecordItemPO> {

    List<InspectRecordItemVO> getListByRecordId(Long recordId);

    /**
     * 巡检记录项列表
     *
     * @param param 条件
     *
     * @return 列表
     */
    List<InspectRecordItemListVO> inspectRecordItemList(InspectRecordItemQueryParam param);

    List<InspectRecordItemDataMode> getInspectRecordItemSafetyData(Period period);

    List<InspectRecordItemStatisticsVO> getInspectRecordItemsStatistics(InspectRecordItemQueryParam param);

    /**
     * 批量获取巡检记录的记录项
     *
     * @param recordIdList 记录id集合
     *
     * @return map key:记录id，value:记录项集合
     */
    Map<Long, List<InspectRecordItemVO>> getListByRecordIdList(List<Long> recordIdList);

    /**
     * 批量查询异常巡检记录的记录项
     *
     * @param recordIds
     *
     * @return
     */
    Map<Long, List<InspectRecordItemVO>> getErrorItemByRecordIdList(List<Long> recordIds);
}
