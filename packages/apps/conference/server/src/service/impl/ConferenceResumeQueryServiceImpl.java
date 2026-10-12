package com.guhecloud.cube.srv.gen.conferencemanage.server.service.impl;

import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.conferencemanage.client.dto.ConferenceResumeRecordDTO;
import com.guhecloud.cube.srv.gen.conferencemanage.client.param.ConferenceResumeQueryParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.BookIntervalRecord;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.BookRoom;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.Conference;
import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.BookRoomEnum;
import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.ParticipantEnum;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.BookIntervalRecordService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.BookRoomService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.ConferenceResumeQueryService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.ConferenceService;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 会议履职记录查询服务实现。
 */
@Service("conferenceResumeQueryService")
@RequiredArgsConstructor
public class ConferenceResumeQueryServiceImpl implements ConferenceResumeQueryService {

    private static final Set<BookRoomEnum> EFFECTIVE_BOOK_STATUS = Set.of(BookRoomEnum.RESERVED, BookRoomEnum.BEING_USED, BookRoomEnum.FINISH);

    private final UserClient userClient;
    private final BookIntervalRecordService bookIntervalRecordService;
    private final BookRoomService bookRoomService;
    private final ConferenceService conferenceService;

    @Override
    public List<ConferenceResumeRecordDTO> listMeetingRecords(ConferenceResumeQueryParam param) {
        validateParam(param);
        LocalDateTime begin = param.getStartDate().atStartOfDay();
        LocalDateTime end = param.getEndDate().plusDays(1).atStartOfDay().minusNanos(1);
        List<BookIntervalRecord> intervals = bookIntervalRecordService.lambdaQuery()
                .between(BookIntervalRecord::getAppointmentDate, begin, end)
                .orderByDesc(BookIntervalRecord::getAppointmentDate)
                .list();
        if (intervals.isEmpty()) {
            return List.of();
        }

        Map<Long, List<BookIntervalRecord>> intervalMap = intervals.stream()
                .filter(interval -> interval.getBookId() != null)
                .collect(Collectors.groupingBy(BookIntervalRecord::getBookId));
        if (intervalMap.isEmpty()) {
            return List.of();
        }

        UserDTO user = userClient.getUser(param.getUserId());
        List<BookRoom> bookRooms = bookRoomService.listByIds(intervalMap.keySet()).stream()
                .filter(bookRoom -> EFFECTIVE_BOOK_STATUS.contains(bookRoom.getStatus()))
                .filter(bookRoom -> matchesUser(bookRoom, param.getUserId(), user))
                .toList();
        if (bookRooms.isEmpty()) {
            return List.of();
        }

        Map<Long, Conference> conferenceMap = listConferenceMap(bookRooms);
        return bookRooms.stream()
                .flatMap(bookRoom -> intervalMap.getOrDefault(bookRoom.getId(), List.of()).stream()
                        .map(interval -> toDTO(bookRoom, interval, conferenceMap.get(bookRoom.getConferenceId()), param.getUserId())))
                .sorted(Comparator.comparing(ConferenceResumeRecordDTO::getAppointmentTime,
                                             Comparator.nullsLast(Comparator.reverseOrder())))
                .toList();
    }

    private void validateParam(ConferenceResumeQueryParam param) {
        Assert.isParameterError(param == null, "会议履职查询参数不能为空");
        Assert.isParameterError(param.getUserId() == null, "OUAA用户ID不能为空");
        Assert.isParameterError(param.getStartDate() == null || param.getEndDate() == null, "查询日期不能为空");
        Assert.isParameterError(param.getStartDate().isAfter(param.getEndDate()), "开始日期不能晚于结束日期");
    }

    private boolean matchesUser(BookRoom bookRoom, Long userId, UserDTO user) {
        if (Objects.equals(bookRoom.getSubscribePerson(), userId)) {
            return true;
        }
        Set<Long> participantIds = parseParticipantIds(bookRoom.getParticipantIds());
        if (participantIds.isEmpty() || bookRoom.getParticipantType() == null) {
            return false;
        }
        if (ParticipantEnum.USER.equals(bookRoom.getParticipantType())) {
            return participantIds.contains(userId);
        }
        if (user == null) {
            return false;
        }
        if (ParticipantEnum.DEPARTMENT.equals(bookRoom.getParticipantType())) {
            return userOrganizationIds(user).stream().anyMatch(participantIds::contains);
        }
        if (ParticipantEnum.ROLE.equals(bookRoom.getParticipantType()) && user.getRoles() != null) {
            return user.getRoles().stream()
                    .map(UserDTO.Role::getId)
                    .filter(Objects::nonNull)
                    .anyMatch(participantIds::contains);
        }
        return false;
    }

    private Set<Long> parseParticipantIds(String participantIds) {
        if (participantIds == null || participantIds.isBlank()) {
            return Set.of();
        }
        return Arrays.stream(participantIds.split(","))
                .map(String::trim)
                .filter(value -> !value.isBlank())
                .filter(this::isLong)
                .map(Long::valueOf)
                .collect(Collectors.toSet());
    }

    private Set<Long> userOrganizationIds(UserDTO user) {
        Set<Long> organizationIds = new LinkedHashSet<>();
        if (user.getOrganization() != null && user.getOrganization().getId() != null) {
            organizationIds.add(user.getOrganization().getId());
        }
        if (user.getOrganizations() != null) {
            user.getOrganizations().stream()
                    .map(UserDTO.Organization::getId)
                    .filter(Objects::nonNull)
                    .forEach(organizationIds::add);
        }
        return organizationIds;
    }

    private boolean isLong(String value) {
        try {
            Long.parseLong(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private Map<Long, Conference> listConferenceMap(Collection<BookRoom> bookRooms) {
        List<Long> conferenceIds = bookRooms.stream()
                .map(BookRoom::getConferenceId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();
        if (conferenceIds.isEmpty()) {
            return Map.of();
        }
        return conferenceService.listByIds(conferenceIds).stream()
                .collect(Collectors.toMap(Conference::getId, Function.identity(), (left, right) -> left));
    }

    private ConferenceResumeRecordDTO toDTO(BookRoom bookRoom, BookIntervalRecord interval, Conference conference, Long userId) {
        ConferenceResumeRecordDTO dto = new ConferenceResumeRecordDTO();
        dto.setBookId(bookRoom.getId());
        dto.setIntervalId(interval.getId());
        dto.setUserId(userId);
        dto.setConferenceName(conference == null ? null : conference.getName());
        dto.setConferenceTheme(bookRoom.getConferenceTheme());
        dto.setTimeInterval(interval.getTimeInterval());
        dto.setAppointmentTime(interval.getAppointmentDate());
        dto.setStatus(bookRoom.getStatus() == null ? null : bookRoom.getStatus().name());
        dto.setProcessInstanceId(bookRoom.getProcessInsId());
        return dto;
    }
}
