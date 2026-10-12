package com.guhecloud.cube.srv.gen.attendance.management.server.arrange.querygateway;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.style.StyleUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.common.commandgateway.WorkService;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.DutyWorkClassesMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.dao.DutyWorkMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo.ArrangeWorkVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo.DutyWorkAppVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.interfaces.vo.DutyWorkVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.DutyWorkClassesPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.po.DutyWorkPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.querygateway.query.DutyWorkQueryParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.arrange.utils.PageResponse;
import com.guhecloud.cube.srv.gen.attendance.management.server.common.util.ExcelUtils;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.dao.AttendanceGroupMapper;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.domain.classes.Classes;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.enumeration.AttendanceGroupTypeEnum;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo.AttendanceGroupStaffVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.interfaces.vo.ClassesVO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.po.AttendanceGroupPO;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.querygateway.AttendanceGroupQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.querygateway.ClassesQueryService;
import com.guhecloud.cube.srv.gen.attendance.management.server.rule.querygateway.query.ClassesQueryParam;
import com.guhecloud.cube.srv.gen.personnel.management.client.StaffClient;
import com.guhecloud.cube.srv.gen.personnel.management.client.dto.StaffDetailDTO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author bq
 * @date 2022/11/24
 * @description 值班查询
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DutyWorkQueryService extends ServiceImpl<DutyWorkMapper, DutyWorkPO> {

    private final DutyWorkClassesMapper dutyWorkClassesMapper;
    private final WorkService workService;
    private final AttendanceGroupQueryService attendanceGroupQueryService;
    private final ClassesQueryService classesQueryService;
    private final StaffClient staffClient;
    private final AttendanceGroupMapper attendanceGroupMapper;
    private final ArrangeWorkQueryService arrangeWorkQueryService;

    public Pagination<DutyWorkVO> getPage(DutyWorkQueryParam param) {
        List<DutyWorkPO> dutyWorkPOS = this.getQueryChainWrapper(param).list();
        List<DutyWorkVO> dutyWorkVOS = this.extend(dutyWorkPOS, param);
        // 内存分页
        return PageResponse.doPage(dutyWorkVOS, param.getPageNumber(), param.getPageSize());
    }

    public List<DutyWorkAppVO> getList(DutyWorkQueryParam param) {
        Long currentUserId = SecurityContextHolder.getPrincipal().getId();
        List<DutyWorkAppVO> vos = new ArrayList<>();
        int intervalDays = (int) DateUtils.between(param.getPeriod().getBegin(), param.getPeriod().getEnd(), ChronoUnit.DAYS, false) + 1;
        for (int i = 0; i < intervalDays; i++) {
            DutyWorkAppVO vo = new DutyWorkAppVO();
            LocalDateTime finalDate = DateUtils.offsetDay(param.getPeriod().getBegin(), i);
            vo.setDate(finalDate);
            List<DutyWorkPO> dutyWorkPOS = this.lambdaQuery().eq(DutyWorkPO::getDate, finalDate).ne(DutyWorkPO::getDutyWorkClassesIds, "")
                    .list();
            vo.setStaffList(dutyWorkPOS.parallelStream().map(duty -> {
                DutyWorkAppVO.Staff staff = new DutyWorkAppVO.Staff();
                staff.setStaffId(duty.getStaffId());
                staff.setStaffName(duty.getStaffName());
                staff.setIsCurrentUser(currentUserId.equals(duty.getStaffId()));
                staff.setDutyWorkClassesIds(duty.getDutyWorkClassesIds());
                return staff;
            }).collect(Collectors.toList()));
            vos.add(vo);
        }
        return vos;
    }

    public List<Classes> getDutyClasses(String dutyWorkClassesIds) {
        List<Classes> classesList = new ArrayList<>();
        if (StringUtils.isEmpty(dutyWorkClassesIds)) {
            return classesList;
        }
        String[] classesIds = dutyWorkClassesIds.split(",");
        for (String id : classesIds) {
            classesList.add(dutyWorkClassesMapper.selectById(id));
        }
        classesList.sort(Comparator.comparing(c -> DateUtils.parse(c.getStartWorkTime(), "HH:mm")));
        return classesList;
    }

    /**
     * 导出值班数据
     *
     * @param param
     * @param request
     * @param response
     */
    public void exportDuty(DutyWorkQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        List<DutyWorkPO> dutyWorkPOS = this.getQueryChainWrapper(param).list();
        List<DutyWorkVO> dutyWorkVOS = this.extend(dutyWorkPOS, param);

        Assert.isNotAcceptable(CollectionUtils.isEmpty(dutyWorkVOS), "未查询到值班数据，无法导出");
        List<Map<String, String>> excelDatas = this.formatExportData(dutyWorkVOS);
        //取出表头 同时删除表头数据
        Map<String, String> headers = excelDatas.removeLast();
        //导出Excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.setHeaderAlias(headers);
        writer.setOnlyAlias(true);
        //默认配置
        writer.write(excelDatas, true);
        writer.setColumnWidth(-1, 15);
        //设置单元格自动换行
        CellStyle cellStyle = writer.getCellStyle();
        cellStyle.setWrapText(true);
        String fileName = (StringUtils.isNotEmpty(param.getMonth()) ? param.getMonth() + "_" : "") + "考勤结果统计";
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    public void exportTemplate(DutyWorkQueryParam param, HttpServletRequest request, HttpServletResponse response) {
        String groupName = "";
        if (null != param.getGroupId()) {
            AttendanceGroupPO groupPO = attendanceGroupQueryService.getById(param.getGroupId());
            groupName = (groupPO == null ? groupName : groupPO.getName());
        }
        List<DutyWorkVO> dutyWorkVOS = this.getDataByMonth(param);
        Assert.isNotAcceptable(CollectionUtils.isEmpty(dutyWorkVOS), "未查询到值班人员，无法导出");
        List<Map<String, String>> excelDatas = this.formatTemplateData(dutyWorkVOS, false);
        //取出表头 同时删除表头数据
        Map<String, String> headers = excelDatas.removeLast();
        //导出Excel
        ExcelWriter writer = ExcelUtil.getWriter(true);
        writer.merge(excelDatas.getFirst().size() - 1, groupName + param.getMonth() + "月值班表", false);
        writer.setHeaderAlias(headers);
        writer.setOnlyAlias(true);
        // 设置表头背景色
        CellStyle headCellStyle = writer.getHeadCellStyle();
        StyleUtil.setColor(headCellStyle, IndexedColors.AQUA, FillPatternType.SOLID_FOREGROUND);
        // 配置班次下拉
        List<String> classesNames = classesQueryService.getList(new ClassesQueryParam()).parallelStream().map(ClassesVO::getSummaryName)
                .collect(Collectors.toList());
        Assert.isNotAcceptable(CollectionUtils.isEmpty(classesNames), "系统中未查询到班次列表，无法导出");
        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(2, excelDatas.size() + 1, 4, excelDatas.get(0).size() - 1);
        //        writer.addSelect(cellRangeAddressList, String.join(",", classesNames));
        // 最大的班次长度
        String max = classesNames.stream().max(Comparator.comparing(String::length)).get();
        //默认配置
        writer.write(excelDatas, true);
        writer.setColumnWidth(-1, max.length() + 8);
        //设置单元格自动换行
        CellStyle cellStyle = writer.getCellStyle();
        cellStyle.setWrapText(true);
        String fileName = (StringUtils.isNotEmpty(param.getMonth()) ? param.getMonth() + "_" : "") + "排班";
        arrangeWorkQueryService.createClassesSelect(writer, classesNames, cellRangeAddressList);
        ExcelUtils.downloadExcel(request, response, fileName, writer);
    }

    private List<DutyWorkVO> getDataByMonth(DutyWorkQueryParam param) {
        String month = param.getMonth();
        List<DutyWorkVO> vos = new ArrayList<>();
        LocalDateTime beginDate = null;
        LocalDateTime endDate = null;
        if (StringUtils.isNotEmpty(month)) {
            beginDate = DateUtils.parseDate(month + "-01");
            endDate = DateUtils.endOfMonth(beginDate);
        } else {
            beginDate = DateUtils.beginOfMonth(LocalDateTime.now());
            endDate = DateUtils.endOfMonth(LocalDateTime.now());
            param.setMonth(DateUtils.format(LocalDateTime.now(), "yyyy-MM"));
        }
        int intervalByDays = Integer.parseInt(String.valueOf(DateUtils.between(beginDate, endDate, ChronoUnit.DAYS, true)));
        // 查询考勤组的人员
        List<AttendanceGroupStaffVO> staffList = attendanceGroupQueryService.getStaffByGroupId(param.getGroupId(), AttendanceGroupTypeEnum.DUTY.name());
        if (CollectionUtils.isEmpty(staffList)) {
            return Collections.emptyList();
        }
        LocalDateTime finalBeginDate = beginDate;
        staffList.forEach(staff -> {
            DutyWorkVO workVO = ObjectUtils.copying(staff, DutyWorkVO.class);
            //TODO 优化一下，先把全量部门和考勤组查出来匹配，不需要每个去查
            StaffDetailDTO staffDetailDTO = staffClient.getById(staff.getStaffId() == null ? staff.getId() : staff.getStaffId());
            if (staffDetailDTO == null) {
                return;
            }
            workVO.setDeptName(staffDetailDTO.getOrganizationName());
            // 查询考勤组名称
            AttendanceGroupPO attendanceGroup = attendanceGroupMapper.selectById(staff.getGroupId());
            workVO.setGroupName(attendanceGroup != null ? attendanceGroup.getName() : "");
            // 组装一个月的时间列表
            List<DutyWorkVO.DateOfClasses> dateOfClasses = new ArrayList<>();
            for (int i = 0; i <= intervalByDays; i++) {
                DutyWorkVO.DateOfClasses dateWork = new DutyWorkVO.DateOfClasses();
                LocalDateTime date = DateUtils.offsetDay(finalBeginDate, i);
                int week = date.getDayOfWeek().getValue();
                dateWork.setWeek(week);
                dateWork.setDate(date);
                // 查询当天的班次列表 TODO 这里会影响下载的效率，如果不考虑回显班次就注释掉
                List<Classes> userClassesByDay = dutyWorkClassesMapper.getUserClassesByDay(staff.getStaffId(), date);
                dateWork.setClassesVOS(userClassesByDay);
                dateOfClasses.add(dateWork);
            }
            workVO.setDayOfClasses(dateOfClasses);
            vos.add(workVO);
        });
        return vos;
    }

    private List<Map<String, String>> formatTemplateData(List<DutyWorkVO> dutyWorkVOS, boolean isDutyWork) {
        Map<String, String> headerAlias = new LinkedHashMap<>();
        List<Map<String, String>> datas = new ArrayList<>();
        for (int i = 0; i < dutyWorkVOS.size(); i++) {
            DutyWorkVO dutyWorkVO = dutyWorkVOS.get(i);
            Map<String, String> data = new LinkedHashMap<>();
            data.put("staffName", dutyWorkVO.getStaffName());
            if (!isDutyWork) {
                data.put("staffMobile", dutyWorkVO.getStaffMobile());
                data.put("deptName", dutyWorkVO.getDeptName());
                data.put("groupName", dutyWorkVO.getGroupName());
            }
            if (i == 0) {
                headerAlias.put("staffName", "员工");
                if (!isDutyWork) {
                    headerAlias.put("staffMobile", "电话");
                    headerAlias.put("deptName", "部门");
                    headerAlias.put("groupName", "考勤组");
                }
            }
            if (!CollectionUtils.isEmpty(dutyWorkVO.getDayOfClasses())) {
                List<DutyWorkVO.DateOfClasses> dayOfClasses = dutyWorkVO.getDayOfClasses();
                for (int j = 0; j < dayOfClasses.size(); j++) {
                    DutyWorkVO.DateOfClasses dateOfClasses = dayOfClasses.get(j);
                    List<Classes> classesVOS = dateOfClasses.getClassesVOS();
                    if (!CollectionUtils.isEmpty(classesVOS)) {
                        //TODO 待优化 取当天的班次，如果是多班次的暂时不回显，属于后续优化
                        String classesName = classesVOS.getFirst().getSummaryName();
                        data.put("dateOfClasses" + j, classesName == null ? "" : classesName);
                    } else {
                        data.put("dateOfClasses" + j, "");
                    }
                    if (i == 0) {
                        int dayOfMonth = dateOfClasses.getDate().getDayOfMonth();
                        String headerName = (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth + "") + "/" +
                                            DateUtils.dayOfWeekFormat(dateOfClasses.getDate());
                        headerAlias.put("dateOfClasses" + j, headerName);
                    }
                }
            }
            datas.add(data);
        }
        //插入表头
        datas.add(headerAlias);
        return datas;
    }

    private List<Map<String, String>> formatExportData(List<DutyWorkVO> dutyWorkVOS) {
        Map<String, String> headerAlias = new LinkedHashMap<>();
        List<Map<String, String>> datas = new ArrayList<>();
        for (int i = 0; i < dutyWorkVOS.size(); i++) {
            DutyWorkVO dutyWorkVO = dutyWorkVOS.get(i);
            Map<String, String> data = new LinkedHashMap<>();
            data.put("staffName", dutyWorkVO.getStaffName());
            if (i == 0) {
                headerAlias.put("staffName", "员工");
            }
            if (!CollectionUtils.isEmpty(dutyWorkVO.getDayOfClasses())) {
                List<DutyWorkVO.DateOfClasses> dayOfClasses = dutyWorkVO.getDayOfClasses();
                for (int j = 0; j < dayOfClasses.size(); j++) {
                    DutyWorkVO.DateOfClasses dateOfClasses = dayOfClasses.get(j);
                    List<Classes> classesVOS = dateOfClasses.getClassesVOS();
                    if (!CollectionUtils.isEmpty(classesVOS)) {
                        String dayOfClassesStr = classesVOS.stream().map(classes -> classes.getName() +
                                                                                    "\r\n" +
                                                                                    classes.getStartWorkTime() + "-" +
                                                                                    classes.getEndWorkTime())
                                .collect(Collectors.joining("\r\n"));
                        data.put("dateOfClasses" + j, dayOfClassesStr);
                    } else {
                        data.put("dateOfClasses" + j, "休息");
                    }
                    if (i == 0) {
                        int dayOfMonth = dateOfClasses.getDate().getDayOfMonth();
                        String headerName = (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth + "") + "/" +
                                            DateUtils.dayOfWeekFormat(dateOfClasses.getDate());
                        headerAlias.put("dateOfClasses" + j, headerName);
                    }
                }
            }
            datas.add(data);
        }
        //插入表头
        datas.add(headerAlias);
        return datas;
    }

    private LambdaQueryChainWrapper<DutyWorkPO> getQueryChainWrapper(DutyWorkQueryParam param) {
        return this.lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getKeyword()), DutyWorkPO::getStaffName, param.getKeyword())
                .eq(null != param.getGroupId(), DutyWorkPO::getGroupId, param.getGroupId())
                .eq(StringUtils.isNotEmpty(param.getMonth()), DutyWorkPO::getMonth, param.getMonth())
                .eq(null != param.getOrganizationId(), DutyWorkPO::getStaffOrganizationId, param.getOrganizationId())
                .and(null != param.getPeriod(), q -> q
                        .between(null != param.getPeriod().getBegin() && null != param.getPeriod().getEnd(), DutyWorkPO::getDate, param
                                .getPeriod().getBegin(), param.getPeriod().getEnd())
                    )
                .eq(param.getIsCurrentUser() != null && param.getIsCurrentUser(), DutyWorkPO::getStaffId, SecurityContextHolder
                        .getPrincipal().getId())
                ;
    }

    private List<DutyWorkVO> extend(List<DutyWorkPO> records, DutyWorkQueryParam param) {
        List<DutyWorkVO> result = new ArrayList<>();
        List<DutyWorkVO> dutyWorkVOS = new ArrayList<>();
        // 寻找当前用户，如果当前用户在值班表中则排在第一位
        Long userId = SecurityContextHolder.getPrincipal().getId();
        DutyWorkVO currentUserWorkVO = new DutyWorkVO();
        records.stream().collect(Collectors.groupingBy(DutyWorkPO::getStaffId)).forEach((staffId, pos) -> {
            if (CollectionUtils.isEmpty(pos)) {
                return;
            }
            DutyWorkVO workVO = ObjectUtils.copying(pos.getFirst(), DutyWorkVO.class);
            List<DutyWorkVO.DateOfClasses> dateOfClasses = new ArrayList<>();
            pos.forEach(po -> {
                // 查询每天的班次
                DutyWorkVO.DateOfClasses date = ObjectUtils.copying(po, DutyWorkVO.DateOfClasses.class);
                String dutyWorkClassesIds = po.getDutyWorkClassesIds();
                if (StringUtils.isNotEmpty(dutyWorkClassesIds)) {
                    List<String> classesIds = Arrays.asList(dutyWorkClassesIds.split(","));
                    List<DutyWorkClassesPO> dutyWorkClassesPOS = dutyWorkClassesMapper.selectList(Wrappers.<DutyWorkClassesPO>lambdaQuery()
                                                                                                          .in(DutyWorkClassesPO::getId, classesIds));
                    List<Classes> classesList = ObjectUtils.copying(dutyWorkClassesPOS, Classes.class);
                    classesList.sort(Comparator.comparing(c -> DateUtils.parse(c.getStartWorkTime(), "HH:mm")));
                    date.setClassesVOS(classesList);
                }
                dateOfClasses.add(date);
            });
            workVO.setDayOfClasses(ObjectUtils.copying(workService.sortDateOfClasses(ObjectUtils.copying(dateOfClasses, ArrangeWorkVO.DateOfClasses.class), param.getMonth(), param.getPeriod()), DutyWorkVO.DateOfClasses.class));
            if (!staffId.equals(userId)) {
                dutyWorkVOS.add(workVO);
            } else {
                ObjectUtils.copying(workVO, currentUserWorkVO);
            }
        });
        // 如果当前用户值班班次存在，则插入数组第一位
        if (null != currentUserWorkVO.getStaffId()) {
            result.add(currentUserWorkVO);
        }
        result.addAll(dutyWorkVOS);
        return result;
    }

}
