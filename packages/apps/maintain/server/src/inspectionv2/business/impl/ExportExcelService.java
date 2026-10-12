package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.impl;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.LocationStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.electron.vo.statistics.PersonnelStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.CheckTheMismatchDetailVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.DepInspectionDetailVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.DepInspectionStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.HiddenDangerDetailVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionObjStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionResultDetailVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.InspectionResultVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.StaffInspectionStatisticsVO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.vo.TroubleListDetailVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wql
 * @describe 导出excel处理
 * @date 2023/4/25 11:49
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ExportExcelService {
    /**
     * @Author wql
     * @Description 巡检结果统计导出
     * @Date 11:52 2023/4/25
     **/
    public ExcelWriter resultStatisticsExport(List<InspectionResultVO> list) {
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("name", "巡检任务");
        writer.addHeaderAlias("targetNum", "巡检对象数");
        writer.addHeaderAlias("typeName", "巡检类型");
        writer.addHeaderAlias("inspectionStaff", "巡检人员");
        writer.addHeaderAlias("checkStaff", "稽查人员");
        writer.addHeaderAlias("checkResult", "巡检结果");
        writer.addHeaderAlias("checkMatchRatio", "稽查结果匹配率");
        writer.addHeaderAlias("checkNotMatchNum", "稽查不匹配项数");
        writer.addHeaderAlias("hiddenDangerNum", "异常隐患数");
        writer.addHeaderAlias("faultNum", "巡检报修数");
        writer.addHeaderAlias("elapsedTime", "巡检耗时");
        List<Map<String, Object>> maps = new ArrayList<>();
        list.forEach(o -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", o.getName());
            map.put("targetNum", o.getTargetNum());
            map.put("typeName", o.getTypeName());
            map.put("inspectionStaff", o.getInspectionStaff());
            map.put("checkStaff", o.getCheckStaff());
            map.put("checkResult", o.getCheckResult());
            map.put("checkMatchRatio", o.getCheckMatchRatio());
            map.put("checkNotMatchNum", o.getCheckNotMatchNum());
            map.put("hiddenDangerNum", o.getHiddenDangerNum());
            map.put("faultNum", o.getFaultNum());
            map.put("elapsedTime", o.getElapsedTime());
            maps.add(map);
        });
        writer.write(maps, true);
        return writer;
    }

    /**
     * @Author wql
     * @Description 部门巡检统计导出excel
     * @Date 11:56 2023/4/25
     **/
    public ExcelWriter departmentStatisticsExport(List<DepInspectionStatisticsVO> list) {
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("depName", "所属部门");
        writer.addHeaderAlias("coverObjNum", "覆盖对象数");
        writer.addHeaderAlias("coverObjRatio", "对象覆盖率");
        writer.addHeaderAlias("taskTotalNum", "任务总数");
        writer.addHeaderAlias("completeTaskNum", "完成任务数");
        writer.addHeaderAlias("expireTaskNum", "过期未执行任务数");
        writer.addHeaderAlias("taskCompleteRatio", "任务完成率");
        writer.addHeaderAlias("inspectionNum", "巡检次数");
        writer.addHeaderAlias("inspectionElapsedTimeStr", "巡检耗时");
        writer.addHeaderAlias("checkMatchRatio", "稽查平均匹配率");
        writer.addHeaderAlias("abnormalHiddenDangerNum", "异常隐患数");
        writer.addHeaderAlias("haveDoneHiddenDangerNum", "已处理隐患数");
        writer.addHeaderAlias("HiddenDangerDisposeRatio", "隐患处理率");
        writer.addHeaderAlias("validHiddenDangerRatio", "有效隐患占比");
        List<Map<String, Object>> maps = new ArrayList<>();
        list.forEach(o -> {
            Map<String, Object> map = new HashMap<>();
            map.put("depName", o.getDepName());
            map.put("coverObjNum", o.getCoverObjNum());
            map.put("coverObjRatio", o.getCoverObjRatio());
            map.put("taskTotalNum", o.getTaskTotalNum());
            map.put("completeTaskNum", o.getCompleteTaskNum());
            map.put("expireTaskNum", o.getExpireTaskNum());
            map.put("taskCompleteRatio", o.getTaskCompleteRatio());
            map.put("inspectionNum", o.getInspectionNum());
            map.put("inspectionElapsedTimeStr", o.getInspectionElapsedTimeStr());
            map.put("checkMatchRatio", o.getCheckMatchRatio());
            map.put("abnormalHiddenDangerNum", o.getAbnormalHiddenDangerNum());
            map.put("haveDoneHiddenDangerNum", o.getHaveDoneHiddenDangerNum());
            map.put("HiddenDangerDisposeRatio", o.getHiddenDangerDisposeRatio());
            map.put("validHiddenDangerRatio", o.getValidHiddenDangerRatio());
            maps.add(map);
        });
        writer.write(maps, true);
        return writer;
    }

    /**
     * @Author wql
     * @Description 人员巡检统计导出
     * @Date 13:31 2023/4/25
     **/
    public ExcelWriter staffStatisticsExport(List<StaffInspectionStatisticsVO> list) {
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("staffName", "人员姓名");
        writer.addHeaderAlias("depName", "所属部门");
        writer.addHeaderAlias("depTaskNum", "部门任务总数");
        writer.addHeaderAlias("personageTaskNum", "个人任务总数");
        writer.addHeaderAlias("completeTaskNum", "完成任务数");
        writer.addHeaderAlias("expireTaskNum", "过期未执行任务数");
        writer.addHeaderAlias("taskProportion", "任务占比");
        writer.addHeaderAlias("taskCompleteRatio", "任务完成率");
        writer.addHeaderAlias("checkMatchingRatio", "稽查平均匹配率");
        writer.addHeaderAlias("depInspectionNum", "部门巡检次数");
        writer.addHeaderAlias("personageInspectionNum", "个人巡检次数");
        writer.addHeaderAlias("personageNumRatio", "次数占比");
        writer.addHeaderAlias("inspectionElapsedTimeStr", "巡检耗时");
        writer.addHeaderAlias("abnormalHiddenDangerNum", "异常隐患数");
        writer.addHeaderAlias("haveDoneHiddenDangerNum", "已处理隐患数");
        writer.addHeaderAlias("hiddenDangerDisposeRatio", "隐患处理率");
        writer.addHeaderAlias("hiddenDangerValidRatio", "隐患有效占比");
        List<Map<String, Object>> maps = new ArrayList<>();
        list.forEach(o -> {
            Map<String, Object> map = new HashMap<>();
            map.put("staffName", o.getStaffName());
            map.put("depName", o.getDepName());
            map.put("depTaskNum", o.getDepTaskNum());
            map.put("personageTaskNum", o.getPersonageTaskNum());
            map.put("completeTaskNum", o.getCompleteTaskNum());
            map.put("expireTaskNum", o.getExpireTaskNum());
            map.put("taskProportion", o.getTaskProportion());
            map.put("taskCompleteRatio", o.getTaskCompleteRatio());
            map.put("checkMatchingRatio", o.getCheckMatchingRatio());
            map.put("depInspectionNum", o.getDepInspectionNum());
            map.put("personageInspectionNum", o.getPersonageInspectionNum());
            map.put("personageNumRatio", o.getPersonageNumRatio());
            map.put("inspectionElapsedTimeStr", o.getInspectionElapsedTimeStr());
            map.put("abnormalHiddenDangerNum", o.getAbnormalHiddenDangerNum());
            map.put("haveDoneHiddenDangerNum", o.getHaveDoneHiddenDangerNum());
            map.put("hiddenDangerDisposeRatio", o.getHiddenDangerDisposeRatio());
            map.put("hiddenDangerValidRatio", o.getHiddenDangerValidRatio());
            maps.add(map);
        });
        writer.write(maps, true);
        return writer;

    }

    /**
     * @Author wql
     * @Description 巡检对象统计导出
     * @Date 13:34 2023/4/25
     **/
    public ExcelWriter objStatisticsExport(List<InspectionObjStatisticsVO> list) {
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("objName", "对象名称");
        writer.addHeaderAlias("objType", "对象类型");
        writer.addHeaderAlias("area", "所在区域");
        writer.addHeaderAlias("inspectionNum", "巡检次数");
        writer.addHeaderAlias("taskNum", "巡检任务数");
        writer.addHeaderAlias("abnormalHiddenDangerNum", "异常隐患数");
        writer.addHeaderAlias("validHiddenDangerNum", "有效隐患数");
        writer.addHeaderAlias("processedHiddenDangerNum", "已处理隐患数");
        writer.addHeaderAlias("untreatedHiddenDangerNum", "未处理隐患数");
        writer.addHeaderAlias("hiddenDangerDisposeRatio", "隐患处理率");
        writer.addHeaderAlias("repairsNum", "报修次数");
        writer.addHeaderAlias("inspectionAveTime", "巡检平均耗时");
        List<Map<String, Object>> maps = new ArrayList<>();
        list.forEach(o -> {
            Map<String, Object> map = new HashMap<>();
            map.put("objName", o.getObjName());
            map.put("objType", o.getObjType());
            map.put("area", o.getArea());
            map.put("inspectionNum", o.getInspectionNum());
            map.put("taskNum", o.getTaskNum());
            map.put("abnormalHiddenDangerNum", o.getAbnormalHiddenDangerNum());
            map.put("validHiddenDangerNum", o.getValidHiddenDangerNum());
            map.put("processedHiddenDangerNum", o.getProcessedHiddenDangerNum());
            map.put("untreatedHiddenDangerNum", o.getUntreatedHiddenDangerNum());
            map.put("hiddenDangerDisposeRatio", o.getHiddenDangerDisposeRatio());
            map.put("repairsNum", o.getRepairsNum());
            map.put("inspectionAveTime", o.getInspectionAveTime());
            maps.add(map);
        });
        writer.write(maps, true);
        return writer;
    }

    /**
     * @Author wql
     * @Description 巡检记录明细导出
     * @Date 14:20 2023/4/25
     **/
    public ExcelWriter inspectionDetailExport(List<DepInspectionDetailVO> voList) {
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("releaseTime", "发布时间");
        writer.addHeaderAlias("objName", "巡检对象名称");
        writer.addHeaderAlias("taskName", "任务名称");
        writer.addHeaderAlias("inspectionStaff", "巡检人员");
        writer.addHeaderAlias("checkStaff", "稽查人员");
        writer.addHeaderAlias("state", "状态");
        writer.addHeaderAlias("inspectionTime", "巡检时间");
        writer.addHeaderAlias("result", "结果");
        List<Map<String, Object>> maps = new ArrayList<>();
        voList.forEach(o -> {
            Map<String, Object> map = new HashMap<>();
            map.put("releaseTime", o.getReleaseTime());
            map.put("objName", o.getObjName());
            map.put("taskName", o.getTaskName());
            map.put("inspectionStaff", o.getInspectionStaff());
            map.put("checkStaff", o.getCheckStaff());
            map.put("state", o.getState());
            map.put("inspectionTime", o.getInspectionTime());
            map.put("result", o.getResult());
            maps.add(map);
        });
        writer.write(maps, true);
        return writer;
    }

    /**
     * @Author wql
     * @Description 任务明细导出
     * @Date 14:24 2023/4/25
     **/
    public ExcelWriter taskDetailExport(List<InspectionResultDetailVO> records) {
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("taskName", "任务名称");
        writer.addHeaderAlias("inspectionType", "巡检类型");
        writer.addHeaderAlias("taskType", "任务类型");
        writer.addHeaderAlias("dutyOfficer", "责任人/部门");
        writer.addHeaderAlias("publishTime", "发布时间");
        writer.addHeaderAlias("taskTimeLimitStr", "任务时限");
        writer.addHeaderAlias("objNum", "巡检对象数");
        writer.addHeaderAlias("progress", "进度");
        writer.addHeaderAlias("state", "状态");
        List<Map<String, Object>> maps = new ArrayList<>();
        records.forEach(o -> {
            Map<String, Object> map = new HashMap<>();
            map.put("taskName", o.getTaskName());
            map.put("inspectionType", o.getInspectionType());
            map.put("taskType", o.getTaskType());
            map.put("dutyOfficer", o.getDutyOfficer());
            map.put("publishTime", o.getPublishTime());
            map.put("taskTimeLimitStr", o.getTaskTimeLimitStr());
            map.put("objNum", o.getObjNum());
            map.put("progress", o.getProgress());
            map.put("state", o.getState());
            maps.add(map);
        });
        writer.write(maps, true);
        return writer;
    }

    /**
     * @Author wql
     * @Description 报修记录明细导出
     * @Date 14:32 2023/4/25
     **/
    public ExcelWriter repairsDetailExport(List<TroubleListDetailVO> list) {
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("submitTime", "提交时间");
        writer.addHeaderAlias("name", "故障单名称");
        writer.addHeaderAlias("equipment", "故障设备");
        writer.addHeaderAlias("source", "故障来源");
        writer.addHeaderAlias("submitStaff", "提交人");
        writer.addHeaderAlias("state", "状态");
        writer.addHeaderAlias("result", "处理结果");
        List<Map<String, Object>> maps = new ArrayList<>();
        list.forEach(o -> {
            Map<String, Object> map = new HashMap<>();
            map.put("submitTime", o.getSubmitTime());
            map.put("name", o.getName());
            map.put("equipment", o.getEquipment());
            map.put("source", o.getSource());
            map.put("submitStaff", o.getSubmitStaff());
            map.put("state", o.getState());
            map.put("result", o.getResult());
            maps.add(map);
        });
        writer.write(maps, true);
        return writer;
    }

    /**
     * @Author wql
     * @Description 隐患记录导出
     * @Date 14:37 2023/4/25
     **/
    public ExcelWriter getAllHiddenDangerDetail(List<HiddenDangerDetailVO> list) {
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("submitTime", "提交时间");
        writer.addHeaderAlias("name", "隐患单名称");
        writer.addHeaderAlias("hiddenDangerObj", "隐患对象");
        writer.addHeaderAlias("source", "隐患来源");
        writer.addHeaderAlias("classify", "隐患分类");
        writer.addHeaderAlias("levelName", "隐患程度");
        writer.addHeaderAlias("submitter", "提交人");
        writer.addHeaderAlias("state", "状态");
        writer.addHeaderAlias("result", "处理结果");
        List<Map<String, Object>> maps = new ArrayList<>();
        list.forEach(o -> {
            Map<String, Object> map = new HashMap<>();
            map.put("submitTime", o.getSubmitTime());
            map.put("name", o.getName());
            map.put("hiddenDangerObj", o.getHiddenDangerObj());
            map.put("source", o.getSource());
            map.put("classify", o.getClassify());
            map.put("levelName", o.getLevelName());
            map.put("submitter", o.getSubmitter());
            map.put("state", o.getState());
            map.put("result", o.getResult());
            maps.add(map);
        });
        writer.write(maps, true);
        return writer;
    }

    /**
     * @Author wql
     * @Description 稽查不匹配项导出
     * @Date 14:47 2023/4/25
     **/
    public ExcelWriter checkDetailExport(List<CheckTheMismatchDetailVO> list) {
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("name", "巡检项名称");
        writer.addHeaderAlias("inspectionObj", "巡检项对象");
        writer.addHeaderAlias("content", "巡检项内容");
        writer.addHeaderAlias("result", "巡检结果");
        writer.addHeaderAlias("reportResult", "上报结果");
        writer.addHeaderAlias("checkResult", "稽查结果");
        writer.addHeaderAlias("principal", "任务负责人");
        writer.addHeaderAlias("checkPerson", "稽查人");
        List<Map<String, Object>> maps = new ArrayList<>();
        list.forEach(o -> {
            Map<String, Object> map = new HashMap<>();
            map.put("name", o.getName());
            map.put("inspectionObj", o.getInspectionObj());
            map.put("content", o.getContent());
            map.put("result", o.getResult());
            map.put("reportResult", o.getReportResult());
            map.put("checkResult", o.getCheckResult());
            map.put("principal", o.getPrincipal());
            map.put("checkPerson", o.getCheckPerson());
            maps.add(map);
        });
        writer.write(maps, true);
        return writer;
    }

    /**
     * @Author wql
     * @Description 巡更点统计导出
     * @Date 17:06 2023/5/31
     **/
    public ExcelWriter locationExport(List<LocationStatisticsVO> list) {
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("locationName", "巡更点");
        writer.addHeaderAlias("areaName", "所属区域");
        writer.addHeaderAlias("totalNum", "应巡次数");
        writer.addHeaderAlias("realNum", "实巡");
        writer.addHeaderAlias("omitNum", "漏巡");
        writer.addHeaderAlias("patrolRatio", "巡更率");
        writer.addHeaderAlias("abnormalNum", "异常次数");
        writer.addHeaderAlias("hiddenDangerNum", "隐患数");
        writer.addHeaderAlias("hiddenDangerDisposeNum", "隐患处理数");
        List<Map<String, Object>> maps = new ArrayList<>();
        list.forEach(o -> {
            Map<String, Object> map = new HashMap<>();
            map.put("locationName", o.getLocationName());
            map.put("areaName", o.getAreaName());
            map.put("totalNum", o.getTotalNum());
            map.put("realNum", o.getRealNum());
            map.put("omitNum", o.getOmitNum());
            map.put("patrolRatio", o.getPatrolRatio());
            map.put("abnormalNum", o.getAbnormalNum());
            map.put("hiddenDangerNum", o.getHiddenDangerNum());
            map.put("hiddenDangerDisposeNum", o.getHiddenDangerDisposeNum());
            maps.add(map);
        });
        writer.write(maps, true);
        return writer;
    }

    /**
     * @Author wql
     * @Description 巡更人员统计导出
     * @Date 18:11 2023/5/31
     **/
    public ExcelWriter personnelExport(List<PersonnelStatisticsVO> list) {
        ExcelWriter writer = ExcelUtil.getWriter();
        writer.addHeaderAlias("userName", "巡更人员");
        writer.addHeaderAlias("depName", "所属部门");
        writer.addHeaderAlias("taskNum", "巡更任务数");
        writer.addHeaderAlias("abnormalTaskNum", "异常任务数");
        writer.addHeaderAlias("totalNum", "巡更点总数");
        writer.addHeaderAlias("realNum", "已巡更点数");
        writer.addHeaderAlias("omitNum", "漏巡更点数");
        writer.addHeaderAlias("patrolRatio", "巡更率");
        List<Map<String, Object>> maps = new ArrayList<>();
        list.forEach(o -> {
            Map<String, Object> map = new HashMap<>();
            map.put("userName", o.getUserName());
            map.put("depName", o.getDepName());
            map.put("taskNum", o.getTaskNum());
            map.put("abnormalTaskNum", o.getAbnormalTaskNum());
            map.put("totalNum", o.getTotalNum());
            map.put("realNum", o.getRealNum());
            map.put("omitNum", o.getOmitNum());
            map.put("patrolRatio", o.getPatrolRatio());
            maps.add(map);
        });
        writer.write(maps, true);
        return writer;
    }
}
