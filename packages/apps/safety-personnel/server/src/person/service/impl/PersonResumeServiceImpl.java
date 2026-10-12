package com.guhecloud.cube.srv.gen.safety.personnel.server.person.service.impl;

import com.guhecloud.cube.srv.gen.conferencemanage.client.dto.ConferenceResumeRecordDTO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.ResumeRecordTypeEnum;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.facade.SafetyPersonnelResumeFacadeService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.param.ResumeQueryParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.service.PersonResumeService;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.vo.ResumeDayGroupVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.vo.ResumeExportRowVO;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.vo.ResumeItemVO;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 安全履历聚合服务实现。
 */
@Slf4j
@Service("safetyPersonnelPersonResumeService")
@RequiredArgsConstructor
public class PersonResumeServiceImpl implements PersonResumeService {

    private static final int DEFAULT_DAYS = 5;
    private static final int MAX_QUERY_DAYS = 31;

    private final SafetyPersonnelResumeFacadeService resumeFacade;

    @Override
    public List<ResumeDayGroupVO> timeline(Long userId, ResumeQueryParam param) {
        List<ResumeItemVO> items = listItems(userId, param);
        Map<LocalDate, List<ResumeItemVO>> groupMap = items.stream()
                .filter(item -> item.getRecordTime() != null)
                .collect(Collectors.groupingBy(item -> item.getRecordTime().toLocalDate()));
        return groupMap.entrySet().stream()
                .map(entry -> ResumeDayGroupVO.of(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparing(ResumeDayGroupVO::getDate).reversed())
                .toList();
    }

    @Override
    public List<ResumeItemVO> listByType(Long userId, String type, ResumeQueryParam param) {
        String normalizedType = ResumeRecordTypeEnum.normalize(type);
        ResumeDateRange dateRange = resolveDateRange(param);
        List<ResumeItemVO> items = new ArrayList<>();
        if (Objects.equals(ResumeRecordTypeEnum.MEETING.getCode(), normalizedType)) {
            items.addAll(listMeetingItems(userId, dateRange));
        } else {
            items.addAll(listWorkCenterItems(userId, dateRange.period(), normalizedType));
        }
        return items.stream()
                .sorted(Comparator.comparing(ResumeItemVO::getRecordTime, Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
    }

    @Override
    public void export(Long userId, String userName, ResumeQueryParam param, HttpServletResponse response) throws IOException {
        List<ResumeExportRowVO> rows = listItems(userId, param).stream()
                .filter(item -> param == null || StringUtils.isBlank(param.getType())
                        || Objects.equals(param.getType(), item.getType()))
                .sorted(Comparator.comparing(ResumeItemVO::getRecordTime, Comparator.nullsLast(Comparator.reverseOrder())))
                .map(this::toExportRow)
                .toList();
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String displayName = StringUtils.isBlank(userName) ? "安全履历" : userName + "安全履历";
            String fileName = URLEncoder.encode(displayName, StandardCharsets.UTF_8).replace("+", "%20");
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ExcelTypeEnum.XLSX.getValue());
            EasyExcel.write(response.getOutputStream(), ResumeExportRowVO.class)
                    .sheet("履职记录")
                    .doWrite(rows);
        } catch (IOException e) {
            log.error("导出安全履历失败，userId={}", userId, e);
            throw new ServiceException("履历导出失败");
        }
    }

    private ResumeExportRowVO toExportRow(ResumeItemVO item) {
        ResumeExportRowVO row = new ResumeExportRowVO();
        row.setRecordTime(item.getRecordTime());
        if (StringUtils.isNotBlank(item.getName())) {
            row.setTaskName(item.getName());
        } else if (StringUtils.isNotBlank(item.getTypeName())) {
            row.setTaskName(item.getTypeName());
        } else {
            row.setTaskName("未命名任务");
        }
        return row;
    }

    private List<ResumeItemVO> listItems(Long userId, ResumeQueryParam param) {
        ResumeDateRange dateRange = resolveDateRange(param);
        List<ResumeItemVO> items = new ArrayList<>();
        items.addAll(listWorkCenterItems(userId, dateRange.period()));
        items.addAll(listMeetingItems(userId, dateRange));
        return items;
    }

    private List<ResumeItemVO> listWorkCenterItems(Long userId, Period period) {
        return resumeFacade.listWorkTaskResumeTypes().stream()
                .flatMap(resumeType -> listWorkCenterItems(userId, period, resumeType).stream())
                .toList();
    }

    private List<ResumeItemVO> listWorkCenterItems(Long userId, Period period, String resumeType) {
        return resumeFacade.listWorkTasks(userId, period, resumeType).stream()
                .map(task -> resumeFacade.toWorkTaskResumeItem(task, resumeType))
                .toList();
    }

    private List<ResumeItemVO> listMeetingItems(Long userId, ResumeDateRange dateRange) {
        List<ConferenceResumeRecordDTO> records = resumeFacade.listMeetingRecords(
                userId, dateRange.startDate(), dateRange.endDate());
        return records.stream()
                .map(resumeFacade::toMeetingResumeItem)
                .toList();
    }

    private ResumeDateRange resolveDateRange(ResumeQueryParam param) {
        LocalDate endDate = param != null && param.getEndDate() != null ? param.getEndDate() : LocalDate.now();
        LocalDate startDate = param != null && param.getStartDate() != null ? param.getStartDate() : endDate.minusDays(DEFAULT_DAYS - 1);
        long days = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        Assert.isParameterError(days <= 0, "开始日期不能晚于结束日期");
        Assert.isOutOfRange(days > MAX_QUERY_DAYS, "履历查询日期跨度不能超过31天");
        LocalDateTime begin = startDate.atStartOfDay();
        LocalDateTime end = endDate.plusDays(1).atStartOfDay().minusNanos(1);
        return new ResumeDateRange(startDate, endDate, Period.of(begin, end));
    }

    private record ResumeDateRange(LocalDate startDate, LocalDate endDate, Period period) {
    }
}
