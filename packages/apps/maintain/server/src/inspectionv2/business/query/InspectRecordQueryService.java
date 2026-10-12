package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.query;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.maintain.server.common.dto.TotalNumberRateDTO;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.InspectionRecordNumMetricDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.DeviceOperationStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectRecordDataMode;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.AbnormalResultsStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.DeviceStatisticParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.ErrItemTagRecordParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordSubmitCheckParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRepairQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectionCommonParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectionRecordErrorParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.TargetMaxRecordParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.AbnormalResultsStatisticsResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.RecordDetailResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.TargetMaxRecordResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.ErrItemTagRecordVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordErrorVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordListVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordPageExportData;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectTargetRepairVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.ReplicableTargetVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;

import java.util.List;
import java.util.Map;

public interface InspectRecordQueryService extends IService<InspectRecordPO> {

    InspectRecordVO getVOById(Long id);

    InspectRecordVO getVOByTarget(String targetCategory, Long targetId, Long taskId);

    List<InspectRecordVO> getListByTaskId(Long taskId);

    Pagination<InspectRecordVO> getPage(InspectRecordQueryParam param);

    Pagination<RecordDetailResult> getTargetRecordPage(InspectRecordQueryParam param);

    RecordDetailResult getTargetRecordDetail(Long targetId, String targetCategory);

    Pagination<InspectRecordVO> getRecordPage(InspectRecordQueryParam param);

    Boolean recordIsExpired(Long recordId);

    List<DeviceOperationStatisticsDTO> deviceInspectNumStatistics(DeviceStatisticParam param);

    Long objInspectNumStatistics(DeviceStatisticParam param);

    /**
     * 巡检记录列表
     *
     * @param param period:巡检时间,status:状态
     *
     * @return 列表
     */
    List<InspectRecordListVO> inspectRecordVOList(InspectRecordQueryParam param);

    /**
     * 根据巡检目标获取异常目标数
     *
     * @param targetIdList 目标id集合
     *
     * @return 异常的目标数量
     */
    Integer getErrNumByTargets(List<Long> targetIdList);

    List<InspectRecordDataMode> getInspectRecordSafetyData(Period period);

    /**
     * 查询巡检项选择为异常的巡检记录
     *
     * @param param 条件
     *
     * @return 异常记录
     */
    Pagination<ErrItemTagRecordVO> selectErrItemTagRecord(ErrItemTagRecordParam param);

    Pagination<InspectRecordVO> InspectRecordPage(InspectRecordQueryParam param);

    InspectRecordPageExportData InspectRecordExportData(InspectRecordQueryParam param);

    AbnormalResultsStatisticsResult abnormalResultsStatistics(AbnormalResultsStatisticsParam param);

    Map<Long, Map<String, TargetMaxRecordResult>> getTargetMaxError(TargetMaxRecordParam param);

    Pagination<InspectRecordErrorVO> inspectRecordErrorPage(InspectionRecordErrorParam param);

    /**
     * 批量获取任务对象的巡检记录
     *
     * @param taskId       任务id
     * @param targetIdList 对象id集合
     *
     * @return map key:对象id，value:巡检记录集合
     */
    Map<Long, InspectRecordVO> getRecordTargetIdMap(Long taskId, List<Long> targetIdList);

    TotalNumberRateDTO getRecordErrRate(InspectionCommonParam param);

    TotalNumberRateDTO getRecordCompleteRate(InspectionCommonParam param);

    /**
     * 获取巡检次数指标
     *
     * @param period
     *
     * @return
     */
    InspectionRecordNumMetricDTO getRecordUserNumMetric(Period period);

    List<ReplicableTargetVO> getReplicableTargetList(InspectRecordQueryParam param);

    Pagination<InspectTargetRepairVO> getRecordRepairPage(InspectRepairQueryParam param);

    InspectTargetRepairVO getRecordRepairDetail(InspectRepairQueryParam param);

    Boolean checkSubmitRecord(InspectRecordSubmitCheckParam param);
}
