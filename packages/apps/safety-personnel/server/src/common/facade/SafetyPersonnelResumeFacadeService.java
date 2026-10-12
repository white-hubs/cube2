package com.guhecloud.cube.srv.gen.safety.personnel.server.common.facade;

import com.guhecloud.cube.biz.work.center.client.WorkTaskClient;
import com.guhecloud.cube.biz.work.center.client.param.WorkTaskQueryParam;
import com.guhecloud.cube.biz.work.center.client.vo.WorkTaskPageVO;
import com.guhecloud.cube.srv.gen.conferencemanage.client.ConferenceResumeClient;
import com.guhecloud.cube.srv.gen.conferencemanage.client.dto.ConferenceResumeRecordDTO;
import com.guhecloud.cube.srv.gen.conferencemanage.client.param.ConferenceResumeQueryParam;
import com.guhecloud.cube.srv.gen.safety.personnel.server.common.enumeration.ResumeRecordTypeEnum;
import com.guhecloud.cube.srv.gen.safety.personnel.server.person.vo.ResumeItemVO;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 安全履历外部数据源门面。
 */
@Service("safetyPersonnelResumeFacade")
@RequiredArgsConstructor
public class SafetyPersonnelResumeFacadeService {

    private static final int WORK_CENTER_PAGE_SIZE = 1000;

    private static final Map<String, List<String>> WORK_TAG_CODES_BY_RESUME_TYPE = Map.of(
            ResumeRecordTypeEnum.ALARM.getCode(), List.of("ALARM_TASK"),
            ResumeRecordTypeEnum.APPROVAL.getCode(), List.of("APPROVE_TASK"),
            ResumeRecordTypeEnum.PATROL.getCode(), List.of("INSPECT_TASK", "ELECTRONIC_PATROL"),
            ResumeRecordTypeEnum.TRAINING.getCode(), List.of("CULTIVATE_TASK", "EXAM_TASK"),
            ResumeRecordTypeEnum.OTHER.getCode(), List.of(
                    "MAINTENACE_TASK",
                    "PROPERTY_MANAGEMENT",
                    "FOCUS_WORK_REPLY",
                    "DAILY_TASK",
                    "GRID_MANAGE"
                                                        )
                                                                             );

    private static final String MEETING_DETAIL_URL_PATTERN = "/conference/bookroom/%s";

    private final WorkTaskClient workTaskClient;
    private final ConferenceResumeClient conferenceResumeClient;

    /**
     * 查询工作中心安全相关任务明细。
     *
     * @param userId    OUAA用户ID
     * @param dateRange 查询时间范围
     *
     * @return 工作任务分页记录
     */
    public List<WorkTaskPageVO> listWorkTasks(Long userId, Period dateRange) {
        return listWorkTasks(userId, dateRange, WORK_TAG_CODES_BY_RESUME_TYPE.values().stream()
                .flatMap(Collection::stream)
                .toList());
    }

    /**
     * 按安全履历类型查询工作中心任务明细。
     *
     * @param userId     OUAA用户ID
     * @param dateRange  查询时间范围
     * @param resumeType 安全履历类型
     *
     * @return 工作任务分页记录
     */
    public List<WorkTaskPageVO> listWorkTasks(Long userId, Period dateRange, String resumeType) {
        return listWorkTasks(userId, dateRange, WORK_TAG_CODES_BY_RESUME_TYPE.getOrDefault(resumeType, List.of()));
    }

    /**
     * 查询工作中心支持的安全履历类型。
     *
     * @return 工作中心履历类型列表
     */
    public Set<String> listWorkTaskResumeTypes() {
        return WORK_TAG_CODES_BY_RESUME_TYPE.keySet();
    }

    private List<WorkTaskPageVO> listWorkTasks(Long userId, Period dateRange, Collection<String> workTagCodes) {
        if (workTagCodes.isEmpty()) {
            return List.of();
        }
        WorkTaskQueryParam query = new WorkTaskQueryParam();
        query.setPageNumber(1);
        query.setPageSize(WORK_CENTER_PAGE_SIZE);
        query.setUserIdList(List.of(userId));
        query.setPeriod(dateRange.toString());
        query.setWorkTagCodes(String.join(",", workTagCodes));
        Pagination<WorkTaskPageVO> page = workTaskClient.taskQueryPost(query);
        return page == null || page.getRecords() == null ? List.of() : page.getRecords();
    }

    /**
     * 查询会议室履职会议记录。
     *
     * @param userId    OUAA用户ID
     * @param startDate 开始日期
     * @param endDate   结束日期
     *
     * @return 会议记录列表
     */
    public List<ConferenceResumeRecordDTO> listMeetingRecords(Long userId, LocalDate startDate, LocalDate endDate) {
        ConferenceResumeQueryParam query = new ConferenceResumeQueryParam();
        query.setUserId(userId);
        query.setStartDate(startDate);
        query.setEndDate(endDate);
        List<ConferenceResumeRecordDTO> records = conferenceResumeClient.listMeetingRecords(query);
        return records == null ? List.of() : distinctMeetingRecords(records);
    }

    /**
     * 将工作中心任务转换为履历明细。
     *
     * @param task 工作任务
     *
     * @return 履历明细
     */
    public ResumeItemVO toWorkTaskResumeItem(WorkTaskPageVO task) {
        return toWorkTaskResumeItem(task, resolveWorkTaskType(task.getBusinessType(), task.getBusinessTypeName()));
    }

    /**
     * 将工作中心任务按指定安全履历类型转换为履历明细。
     *
     * @param task       工作任务
     * @param resumeType 安全履历类型
     *
     * @return 履历明细
     */
    public ResumeItemVO toWorkTaskResumeItem(WorkTaskPageVO task, String resumeType) {
        ResumeItemVO item = new ResumeItemVO();
        item.setId(task.getId() == null ? null : String.valueOf(task.getId()));
        item.setType(resumeType);
        item.setTypeName(ResumeRecordTypeEnum.getDescription(item.getType()));
        item.setName(task.getTaskName());
        item.setRecordTime(task.getTaskTime());
        item.setBusinessType(task.getBusinessType());
        item.setBusinessTypeName(task.getBusinessTypeName());
        item.setBusinessKey(task.getBusinessKey());
        item.setUrl(task.getUrl());
        return item;
    }

    /**
     * 将会议记录转换为履历明细。
     *
     * @param record 会议记录
     *
     * @return 履历明细
     */
    public ResumeItemVO toMeetingResumeItem(ConferenceResumeRecordDTO record) {
        ResumeItemVO item = new ResumeItemVO();
        item.setId(record.getBookId() == null ? null : String.valueOf(record.getBookId()));
        item.setType(ResumeRecordTypeEnum.MEETING.getCode());
        item.setTypeName(ResumeRecordTypeEnum.MEETING.getDescription());
        item.setName(isNotBlank(record.getConferenceTheme()) ? record.getConferenceTheme() : record.getConferenceName());
        item.setRecordTime(record.getAppointmentTime());
        item.setBusinessType("CONFERENCE_MEETING");
        item.setBusinessTypeName(ResumeRecordTypeEnum.MEETING.getDescription());
        item.setBusinessKey(record.getBookId() == null ? null : String.valueOf(record.getBookId()));
        item.setUrl(buildMeetingDetailUrl(record.getBookId()));
        return item;
    }

    /**
     * 构建会议详情前端路由。
     *
     * @param bookId 会议预约ID
     *
     * @return 相对路径，bookId 为空时返回 null
     */
    public String buildMeetingDetailUrl(Long bookId) {
        return bookId == null ? null : MEETING_DETAIL_URL_PATTERN.formatted(bookId);
    }

    private List<ConferenceResumeRecordDTO> distinctMeetingRecords(List<ConferenceResumeRecordDTO> records) {
        Set<Long> seenBookIds = new HashSet<>();
        return records.stream()
                .filter(Objects::nonNull)
                .filter(record -> record.getBookId() == null || seenBookIds.add(record.getBookId()))
                .toList();
    }

    private String resolveWorkTaskType(String businessType, String businessTypeName) {
        String text = String.join(" ", Objects.toString(businessType, ""), Objects.toString(businessTypeName, "")).toUpperCase();
        if (text.contains("PATROL") || text.contains("INSPECT") || text.contains("巡查") || text.contains("巡检")) {
            return ResumeRecordTypeEnum.PATROL.getCode();
        }
        if (text.contains("TRAIN") || text.contains("CULTIVATE") || text.contains("EXAM") || text.contains("培训") ||
            text.contains("考试")) {
            return ResumeRecordTypeEnum.TRAINING.getCode();
        }
        if (text.contains("APPROVE") || text.contains("PROCESS") || text.contains("审批")) {
            return ResumeRecordTypeEnum.APPROVAL.getCode();
        }
        if (text.contains("ALARM") || text.contains("告警") || text.contains("报警")) {
            return ResumeRecordTypeEnum.ALARM.getCode();
        }
        return ResumeRecordTypeEnum.OTHER.getCode();
    }

    private boolean isNotBlank(String value) {
        return StringUtils.isNotBlank(value);
    }
}
