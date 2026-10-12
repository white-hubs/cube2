package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao;

import com.baomidou.mybatisplus.annotation.InterceptorIgnore;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.guhecloud.cube.srv.gen.maintain.server.common.vo.InspectionRecordUserNumVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.DeviceOperationStatisticsDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.InspectRecordAndTaskDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.dto.RecordTaskDTO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.BaseInspectStatisticsParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.DeviceStatisticParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.ErrItemTagRecordParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRecordQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectRepairQueryParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectionAnalysisParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectionRecordErrorParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.TargetMaxRecordParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.TaskPageParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.TaskStandardAbnormalRateParam;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.FireinspRecordPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectRecordPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.AbnormalResultsStatisticsResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.result.TargetMaxRecordResult;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.DepInspectionDetailVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.DepInspectionStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.DepInspectionTaskInfoVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.DepPersonNumVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.DepartmentInspectionNumStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.ErrItemTagRecordVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordErrorVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectRecordVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectTargetRepairVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionObjStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionRecordAnalysisVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionSafetyReportVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionTaskTimeVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionTimeVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.ObjInspectionInfoVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.StaffInspectionStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.TargetCategoryDetailsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.TaskStandardAbnormalRateVO;
import com.guhecloud.platform.base.model.Period;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 巡检记录 Mapper 接口
 * </p>
 *
 * @author zwb
 * @since 2022-04-26
 */
@Mapper
public interface InspectRecordMapper extends BaseMapper<InspectRecordPO> {
    /**
     * @Author wql
     * @Description 部门巡检统计
     * @Date 14:08 2023/4/17
     **/
    Page<DepInspectionStatisticsVO> departmentStatistics(Page<?> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    List<DepInspectionStatisticsVO> departmentStatisticsExport(@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * @Author wql
     * @Description 人员巡检统计
     * @Date 19:22 2023/4/17
     **/
    Page<StaffInspectionStatisticsVO> staffStatistics(Page<?> page, @Param(Constants.WRAPPER) Wrapper wrapper);

    List<StaffInspectionStatisticsVO> staffStatisticsExport(@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 任务标准项异常率统计
     */
    Page<TaskStandardAbnormalRateVO> taskStandardAbnormalRate(Page<?> page, @Param("param") TaskStandardAbnormalRateParam param);

    /**
     * 任务标准项异常率导出
     */
    List<TaskStandardAbnormalRateVO> taskStandardAbnormalRateExport(@Param("param") TaskStandardAbnormalRateParam param);

    /**
     * @Author wql
     * @Description 统计巡检次数
     * @Date 19:38 2023/4/17
     **/
    @Select(" SELECT count(a.id) from inspect_record a ${ew.customSqlSegment}")
    Long countInspectionNum(@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 按部门批量统计巡检次数
     *
     * @param depIds 部门id集合
     * @param period 时间范围
     *
     * @return 巡检次数
     */
    List<DepartmentInspectionNumStatisticsVO> countDepartmentInspectionNum(@Param("depIds") List<Long> depIds,
                                                                           @Param("period") Period period
                                                                          );

    /**
     * @Author wql
     * @Description 巡检记录明细
     * @Date 20:14 2023/4/17
     **/
    @Select("SELECT b.create_time as releaseTime, a.target_name as objName,a.target_id as objId,b.name as taskName," +
            "    b.id as taskId,b.parent_id as parentId, a.nickname as inspectionStaff, " +
            "    a.status as state,a.time as inspectionTime,a.result as result " +
            "    from inspect_record a " +
            "    left join inspect_task b on a.task_id = b.id  ${ew.customSqlSegment} "
    )
    Page<DepInspectionDetailVO> inspectionDetail(Page<?> convert, @Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * @Author wql
     * @Description 巡检对象统计
     * @Date 9:09 2023/4/18
     **/
    @Select("select a.target_name as objName,a.target_id as objId,a.target_category as objType,a.area_name as area, " +
            "  COUNT(id) as inspectionNum  from inspect_record a ${ew.customSqlSegment}"
    )
    Page<InspectionObjStatisticsVO> objStatistics(Page<?> convert, @Param(Constants.WRAPPER) Wrapper wrapper);

    @Select(" select a.target_name as objName,a.target_id as objId,a.target_category as objType,a.area_name as area, " +
            "  COUNT(id) as inspectionNum  from inspect_record a ${ew.customSqlSegment}"
    )
    List<InspectionObjStatisticsVO> objStatisticsExport(@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * @Author wql
     * @Description 统计巡检完成的设备数量
     * @Date 9:45 2023/4/18
     **/
    @Select("SELECT count(id) FROM inspect_record where status = #{state} and task_id = #{taskId} ")
    Long countCompleteObjNum(@Param("state") String state, @Param("taskId") Long taskId);

    /**
     * @Author wql
     * @Description 统计部门任务耗时
     * @Date 17:41 2023/4/18
     **/
    List<DepInspectionTaskInfoVO> countTotalTimeByDep(@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * @Author wql
     * @Description 按巡检类型统计耗时
     * @Date 17:44 2023/4/18
     **/
    Long countTotalTime(@Param("param") InspectionAnalysisParam analysisParam, @Param("typeId") Long typeId);

    /**
     * @Author wql
     * @Description 按巡检类型分组统计耗时
     * @Date 18:08 2023/4/18
     **/
    List<InspectionTaskTimeVO> countTotalTimeByType(@Param("param") InspectionAnalysisParam analysisParam);

    /**
     * @Author wql
     * @Description 按对象分组统计对象巡检次数
     * @Date 19:27 2023/4/18
     **/
    List<ObjInspectionInfoVO> countInspectionNumByObj(@Param("param") InspectionAnalysisParam analysisParam);

    /**
     * @Author wql
     * @Description 对象异常项统计
     * @Date 19:44 2023/4/18
     **/
    Long countObjAbnormalNum(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime,
                             @Param("objId") Long objId
                            );

    /**
     * @Author wql
     * @Description 对象异常项统计根据对象分组
     * @Date 19:52 2023/4/18
     **/
    List<ObjInspectionInfoVO> countObjAbnormalNumByDep(@Param("param") InspectionAnalysisParam analysisParam);

    /**
     * @Author wql
     * @Description 按对象分组统计任务数
     * @Date 20:15 2023/4/18
     **/
    List<ObjInspectionInfoVO> countObjTaskNumByObj(@Param("param") InspectionAnalysisParam analysisParam);

    /**
     * @Author wql
     * @Description 统计巡检记录耗时
     * @Date 13:59 2023/4/19
     **/
    List<InspectionTimeVO> getResultAndCountTime(@Param("taskId") Long id);

    /**
     * @Author wql
     * @Description 统计某个对象的巡检耗时
     * @Date 20:50 2023/4/19
     **/
    @Select("select timestampdiff(MINUTE,b.begin_time,a.time) from inspect_record a left join inspect_task b on a.task_id = b.id ${ew.customSqlSegment}")
    List<Integer> objInspectionTime(@Param(Constants.WRAPPER) Wrapper wrapper);

    /**
     * 巡检档案分页列表(以对象最后一次的巡检记录)
     *
     * @param page
     *
     * @return
     */
    Page<InspectRecordPO> selectRecordPage(@Param("page") Page<?> page, @Param("param") InspectRecordQueryParam param);

    /**
     * 查询单个巡检台账
     * 台账就是单个对象(的巡检记录,取最近的一次)
     *
     * @param targetId 对象id
     *
     * @return
     */
    @Select("select ir.* from inspect_record ir inner join " +
            "(select ir2.target_id, max(ir2.`time`) `time` " +
            "from inspect_record ir2 where ir2.target_id = #{targetId} group by ir2.target_id) as ir3 " +
            "on (ir.target_id = ir3.target_id and ir.`time` = ir3.`time`) limit 1"
    )
    InspectRecordPO selectTargetRecordOne(@Param("targetId") Long targetId);

    /**
     * 获取巡检档案下的巡检记录
     * 巡检档案就是巡检对象
     *
     * @param param
     *
     * @return
     */
    Page<InspectRecordVO> getRecordList(@Param("page") Page<?> page, @Param("param") InspectRecordQueryParam param);

    List<InspectRecordVO> querTaskRecordList(@Param("param") TaskPageParam param);

    Page<InspectRecordVO> querTaskRecordList(@Param("page") Page<InspectRecordPO> page, @Param("param") TaskPageParam param);

    /**
     * 查询巡检任务详情导出的巡检项列表。
     *
     * @param param 任务详情筛选条件
     *
     * @return 每个巡检项一条记录
     */
    List<InspectRecordVO> queryTaskRecordItemExportList(@Param("param") TaskPageParam param);

    List<TargetCategoryDetailsVO> selectInspection(@Param("param") InspectionAnalysisParam param, @Param("result") String result);

    List<TargetCategoryDetailsVO> selectTask(@Param("param") InspectionAnalysisParam param);

    List<DepPersonNumVO> countPersonNum(@Param("period") Period period);

    List<DeviceOperationStatisticsDTO> deviceInspectNumStatistics(@Param("param") DeviceStatisticParam param);

    List<InspectionSafetyReportVO> selectInspectRecordForSafeyReport(@Param("period") Period period);

    @Select("select * from fireinsp_record")
    List<FireinspRecordPO> getTjInspectResult();

    Integer getErrorNumByTargetIdList(@Param("targetIdList") List<Long> targetIdList);

    /**
     * 查询巡检项选择为异常的巡检记录
     *
     * @param param 条件
     *
     * @return 异常记录
     */
    Page<ErrItemTagRecordVO> selectErrItemTagRecord(@Param("page") Page<?> page, @Param("param") ErrItemTagRecordParam param);

    /**
     * 巡检记录分页列表
     *
     * @param page  分页对象
     * @param param 条件
     *
     * @return 分页列表
     */
    Page<InspectRecordVO> inspectRecords(@Param("page") Page<?> page, @Param("param") InspectRecordQueryParam param);

    List<InspectRecordVO> inspectRecords(@Param("param") InspectRecordQueryParam param);

    /**
     * 巡检记录列表(不分页)
     *
     * @param page  分页对象
     * @param param 条件
     *
     * @return 分页列表
     */
    Page<InspectRecordVO> InspectRecordList(@Param("page") Page<?> page, @Param("param") InspectRecordQueryParam param);

    /**
     * 一段时间的异常项数量统计
     *
     * @param wrapper 条件
     *
     * @return 结果
     */
    @Select("select count(iri.id) as abnormalResultsCount " +
            "from inspect_record_item iri  " +
            "left join inspect_record ir on (iri.record_id = ir.id)  " +
            "${ew.customSqlSegment} "
    )
    <T> AbnormalResultsStatisticsResult abnormalResultsStatistics(@Param(Constants.WRAPPER) Wrapper<T> wrapper);

    List<TargetMaxRecordResult> getTargetErrorCount(@Param("param") TargetMaxRecordParam param, @Param("tag") String tag);

    List<TargetMaxRecordResult> getMaxTimeErrorItem(@Param("param") TargetMaxRecordParam param, @Param("tag") String tag);

    /**
     * 查询工程整改记录
     *
     * @param param
     *
     * @return
     */
    Page<InspectionRecordAnalysisVO> selectInspectRecord(@Param("page") Page<?> page, @Param("param") BaseInspectStatisticsParam param);

    /**
     * 异常巡检记录
     *
     * @param query 条件
     *
     * @return 巡检记录
     */
    Page<InspectRecordErrorVO> inspectRecordErrorPage(Page page, @Param("query") InspectionRecordErrorParam query);

    /**
     * 根据任务id和对象id集合获取巡检记录
     *
     * @param taskId       任务id
     * @param targetIdList 对象id集合
     *
     * @return 巡检记录
     */
    List<InspectRecordVO> getTaskRecordByTargetIds(@Param("taskId") Long taskId, @Param("targetIdList") List<Long> targetIdList);

    /**
     * 总部记录数据采集
     *
     * @param tenant 租户id
     * @param period 时间段
     *
     * @return 记录列表
     */
    @InterceptorIgnore(tenantLine = "on")
    List<InspectRecordPO> getTaskRecordCollectData(@Param("tenant") Integer tenant, @Param("period") Period period);

    Long selectCountCompletedRecord(@Param("period") Period period);

    List<InspectionRecordUserNumVO> selectRankUserInspectRecordNum(@Param("period") Period period, @Param("sort") String sort);

    /**
     * 获取任务类型的记录对象
     *
     * @param recordIdLit
     *
     * @return
     */
    List<RecordTaskDTO> getRecordIdAndTaskTypeId(@Param("recordIdLit") List<Long> recordIdLit);

    List<InspectRecordAndTaskDTO> getRecordAndTaskId(@Param("ids") List<Long> recordIdList);

    /**
     * 修改记录状态 疲劳修改
     *
     * @param recordIdList 记录id集合
     * @param param        修改内容
     * @param updateTime   修改时间
     * @param updateBy     修改人
     *
     * @return 受影响条数
     */
    Long updateStatusByIds(@Param("ids") List<Long> recordIdList,
                           @Param("param") InspectRecordPO param, @Param("updateTime") LocalDateTime updateTime,
                           @Param("updateBy") String updateBy
                          );

    /**
     * 查询修复对象分页
     *
     * @param page  分页
     * @param param 条件
     *
     * @return 分页
     */
    Page<InspectTargetRepairVO> getRecordRepairPage(@Param("page") Page<InspectRecordPO> page, @Param("param")
                                                    InspectRepairQueryParam param
                                                   );

}
