package com.guhecloud.cube.srv.gen.conferencemanage.server.service.impl;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.file.client.util.CustomCellWriteUtil;
import com.guhecloud.cube.base.flow.client.FlowClient;
import com.guhecloud.cube.base.message.sdk.MessageSendClient;
import com.guhecloud.cube.base.message.sdk.api.enumeration.MessageNotifyTargetEnum;
import com.guhecloud.cube.base.message.sdk.api.model.MessageNotifyTarget;
import com.guhecloud.cube.base.message.sdk.api.request.MessageSpecifyTmpSendRequest;
import com.guhecloud.cube.base.message.sdk.api.response.MessageSendResponse;
import com.guhecloud.cube.biz.ouaa.client.ApplicationClient;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.ApplicationDTO;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.biz.ouaa.client.queryrier.UserQuery;
import com.guhecloud.cube.srv.gen.conferencemanage.server.dao.BookIntervalRecordMapper;
import com.guhecloud.cube.srv.gen.conferencemanage.server.dao.BookRoomMapper;
import com.guhecloud.cube.srv.gen.conferencemanage.server.dao.ConferenceMapper;
import com.guhecloud.cube.srv.gen.conferencemanage.server.dao.RoomScreenConfigMapper;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.AppointmentRecord;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.BookIntervalRecord;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.BookRoom;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.BookTimeInterval;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.Conference;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.ConferenceDevice;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.ConferenceType;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.RoomScreenConfig;
import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.BookRoomApprovalEnum;
import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.BookRoomEnum;
import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.ConferenceBookStatusEnum;
import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.OperateEnum;
import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.ParticipantEnum;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.AppointmentParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.BookRoomParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.AppointmentRecordService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.BookIntervalRecordService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.BookRoomService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.BookTimeIntervalService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.ConferenceDeviceService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.ConferenceService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.ConferenceTypeService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.third.service.DeviceProgramSendService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.util.ConferenceAbility;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.AppointmentDetailsVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.AppointmentRecordExportVO;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.AppointmentRecordVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.AppointmentVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.BookRoomAppVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.BookRoomAppointmentRecordVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.BookRoomAppointmentVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.BookRoomVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.ConferenceBaseEntityVO;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.ConferenceDevicesVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.FilterIntervalRecordVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.MeetingRoomReservationVO;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.MobileAppointmentVo;
import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.entity.BasicEntity;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import instance.command.SubmitInitFormCommand;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookRoomServiceImpl extends ServiceImpl<BookRoomMapper, BookRoom> implements BookRoomService {

    private final BookIntervalRecordService bookIntervalRecordService;

    private final UserClient userClient;

    private final ConferenceService conferenceService;

    private final AppointmentRecordService appointmentRecordService;

    private final BookRoomMapper bookRoomMapper;

    private final BookTimeIntervalService bookTimeIntervalService;

    private final ConferenceDeviceService conferenceDeviceService;

    private final ConferenceMapper conferenceMapper;

    private final BookIntervalRecordMapper bookIntervalRecordMapper;

    private final ApplicationClient applicationClient;

    private final ConferenceTypeService conferenceTypeService;

    private final FlowClient flowClient;

    private final RoomScreenConfigMapper roomScreenConfigMapper;
    private final List<DeviceProgramSendService> deviceProgramSendServiceList;

    /**
     * 获取时间段
     *
     * @param splitTimeInterval
     * @param timeInterval
     * @param intervalFlag
     *
     * @return
     */
    private static String getTimeInterval(String[] splitTimeInterval, String timeInterval, String intervalFlag) {
        for (int i = 0; i < splitTimeInterval.length; i++) {
            if (i == 0) {
                timeInterval = timeInterval + splitTimeInterval[i].split("-")[0] + "-";
                intervalFlag = splitTimeInterval[i].split("-")[1];
            } else {
                String[] otherSplit = splitTimeInterval[i].split("-");
                if (intervalFlag.equals(otherSplit[0])) {
                    intervalFlag = otherSplit[1];
                } else {
                    timeInterval = timeInterval + intervalFlag + "," + splitTimeInterval[i].split("-")[0] + "-";
                    intervalFlag = splitTimeInterval[i].split("-")[1];

                }

                if (i == splitTimeInterval.length - 1) {
                    timeInterval = timeInterval + intervalFlag;
                }
            }
        }
        return timeInterval;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized void addBookRoom(BookRoomParam bookRoomParam) {
        Conference conference = conferenceService.getById(bookRoomParam.getConferenceId());
        Assert.isConflict(conference.getStatus().equals(1), "该会议室已被禁用");
        Assert.isOutOfRange(Objects.nonNull(bookRoomParam.getBookNum()) &&
                            conference.getCapacity() < bookRoomParam.getBookNum(), "参会人员不能超过会议室可容纳人数");
        long id = IdWorker.getId();
        BookRoom bookRoom = ObjectUtils.copying(bookRoomParam, BookRoom.class);
        bookRoom.setId(id);
        bookRoom.setStatus(ConferenceAbility.getConferenceStatus());
        bookRoom.setSubscribePerson(SecurityContextHolder.getPrincipal().getId());
        String[] split = bookRoomParam.getTimeInterval().split(",");
        List<String> collect = Arrays.stream(split).sorted().collect(Collectors.toList());
        List<BookIntervalRecord> bookIntervalRecords = new ArrayList<>();
        for (String s : collect) {
            BookIntervalRecord bookIntervalRecord = new BookIntervalRecord();
            bookIntervalRecord.setBookId(id);
            bookIntervalRecord.setAppointmentDate(bookRoomParam.getAppointmentDate());
            bookIntervalRecord.setTimeInterval(s);
            bookIntervalRecord.setConferenceId(bookRoomParam.getConferenceId());
            bookIntervalRecords.add(bookIntervalRecord);
        }
        //时间段显示处理
        String timeInterval = "";
        if (collect.size() > 1) {
            String[] splitTimeInterval = bookRoomParam.getTimeInterval().split(",");
            Arrays.sort(splitTimeInterval);
            String intervalFlag = "";
            timeInterval = getTimeInterval(splitTimeInterval, timeInterval, intervalFlag);
        } else {
            timeInterval = bookRoomParam.getTimeInterval();
        }
        //校验预约时间段是否存在已被预约的
        List<BookRoom> result = this.lambdaQuery()
                .eq(BookRoom::getConferenceId, bookRoomParam.getConferenceId())
                .in(BookRoom::getStatus, BookRoomEnum.RESERVED, BookRoomEnum.APPROVAL)
                .between(BookRoom::getEndBookTime, DateUtils.beginOfDay(bookRoomParam.getAppointmentDate()),
                         DateUtils.endOfDay(bookRoomParam.getAppointmentDate()))
                .select(BookRoom::getTimeInterval)
                .list();
        if (CollectionUtils.isNotEmpty(result)) {
            //单个时间段：半小时
            //多个时间段组合：一小时以上
            checkTimeInterval(result, timeInterval);
        }
        bookRoom.setTimeInterval(timeInterval);
        //预约结束
        String s = split[split.length - 1];
        String[] split1 = s.split("-");
        String s1 = split1[split1.length - 1];
        String endBookTime = DateUtils.format(bookRoomParam.getAppointmentDate(), "yyyy-MM-dd") + " " + s1 + ":00";
        bookRoom.setEndBookTime(endBookTime);
        bookRoom.setGoodsNames(bookRoomParam.getGoodsNames());
        //插入操作记录
        UserDTO user = userClient.getUser(SecurityContextHolder.getPrincipal().getId());
        String conferenceDevice = bookRoomMapper.getConferenceDevice(bookRoomParam.getConferenceId());
        AppointmentRecord appointmentRecord = new AppointmentRecord();
        appointmentRecord.setTimeInterval(timeInterval);
        appointmentRecord.setOperate(ConferenceAbility.getRecordStatus());
        appointmentRecord.setConferenceName(conference.getName());
        appointmentRecord.setUserId(user.getId());
        appointmentRecord.setUserName(user.getName());
        appointmentRecord.setDepartmentName(Objects.isNull(user.getOrganization()) ? "" : user.getOrganization().getName());
        appointmentRecord.setConferenceDevice(conferenceDevice);
        appointmentRecord.setAppointmentDate(bookRoomParam.getAppointmentDate());
        appointmentRecord.setGoodsNames(bookRoomParam.getGoodsNames());
        //插入预约时段记录
        bookIntervalRecordService.saveBatch(bookIntervalRecords);
        String processInsId = this.submitProcess(bookRoomParam, conference, timeInterval);
        appointmentRecord.setProcessInsId(processInsId);
        appointmentRecordService.save(appointmentRecord);
        bookRoom.setProcessInsId(processInsId);
        bookRoom.setParticipantIds(StringUtils.join(",", bookRoomParam.getParticipantIds()));
        this.save(bookRoom);
        //发起流程不进行预约消息通知
        if (StringUtils.isNotBlank(processInsId)) {
            return;
        }
        sendProgram(bookRoom);
        //消息发送
        Set<Long> userIds = Arrays.stream(conference.getAdministrators().split(","))
                .map(Long::valueOf).collect(Collectors.toSet());
        // 通知参会人员
        if (Objects.nonNull(bookRoomParam.getParticipantType())) {
            this.getNoticeUserIds(bookRoomParam.getParticipantType(), bookRoomParam.getParticipantIds(), userIds);
        }
        List<UserDTO> userDTOS = userClient.listUserByIds(userIds);
        for (UserDTO userDTO : userDTOS) {
            Map<String, String> tmpParams = new HashMap<>();
            tmpParams.put("receiveName", userDTO.getName());
            tmpParams.put("sendName", user.getName());
            tmpParams.put("timeInterval", timeInterval);
            tmpParams.put("conferenceName", conference.getName());
            tmpParams.put("month", String.valueOf(bookRoomParam.getAppointmentDate().getMonthValue()));
            tmpParams.put("day", String.valueOf(bookRoomParam.getAppointmentDate().getDayOfMonth()));
            sendMsg(Collections.singleton(userDTO.getId()), "MEETING_APPOINTMENT_REMINDER", tmpParams);
        }

    }

    private void sendProgram(BookRoom bookRoom) {
        RoomScreenConfig screenConfig = roomScreenConfigMapper.selectOne(Wrappers.<RoomScreenConfig>lambdaQuery()
                                                                                 .eq(RoomScreenConfig::getConferenceId, bookRoom.getConferenceId()));
        if (Objects.isNull(screenConfig)) {
            return;
        }
        Optional<DeviceProgramSendService> sendServiceOptional = deviceProgramSendServiceList.stream()
                .filter(t -> t.support(screenConfig.getManufacturerCode())).findFirst();
        if (sendServiceOptional.isEmpty()) {
            return;
        }
        //异步发送节目
        CompletableFuture.runAsync(
                () -> {
                    sendServiceOptional.get().sendProgram(bookRoom, screenConfig);

                }).handle((threadResult, ex) -> {
            if (ex != null) {
                log.info("sendProgram error: ", ex);
            } else {
                log.info("sendProgram success: {}", threadResult);
            }
            return threadResult;
        });
    }

    /**
     * 根据参与人类型获取参与人id
     *
     * @param participantType 参会人类型 （DEPARTMENT, ROLE, USER）
     * @param participantIds  参会人id
     * @param userIds
     *
     * @return
     */
    private void getNoticeUserIds(ParticipantEnum participantType, List<Long> participantIds, Set<Long> userIds) {
        if (CollectionUtils.isEmpty(participantIds)) {
            return;
        }
        if (ParticipantEnum.USER.equals(participantType)) {
            userIds.addAll(participantIds);
        }
        if (ParticipantEnum.DEPARTMENT.equals(participantType)) {
            List<Long> userIdList = userClient.listEnableUserByOrgAndPost(participantIds, null);
            if (CollectionUtils.isNotEmpty(userIdList)) {
                userIds.addAll(userIdList);
            }
        }
        if (ParticipantEnum.ROLE.equals(participantType)) {
            UserQuery queryParam = new UserQuery();
            queryParam.setEnabled(true);
            queryParam.setRoleIds(new HashSet<>(participantIds));
            Optional.ofNullable(userClient.postList(queryParam)).orElse(new ArrayList<>())
                    .stream().map(UserDTO::getId).forEach(userIds::add);
        }
    }

    /**
     * 发送审批事件
     *
     * @param bookRoomParam
     * @param conference
     * @param timeInterval
     *
     * @return
     */

    private String submitProcess(BookRoomParam bookRoomParam, Conference conference, String timeInterval) {

        if (!ConferenceAbility.approvalFlag()) {
            return null;
        }
        ConferenceType conferenceType = conferenceTypeService.getById(bookRoomParam.getConferenceType());
        SubmitInitFormCommand command = new SubmitInitFormCommand();
        command.setType("FLOW_FORM");
        command.setBusinessType(BookRoomApprovalEnum.MEETING_ROOM_RESERVATION.name());
        command.setData(JsonUtils.serialize(MeetingRoomReservationVO.builder()
                                                    .conferenceName(conference.getName())
                                                    .bookNum(bookRoomParam.getBookNum())
                                                    .content(bookRoomParam.getContent())
                                                    .timeInterval(timeInterval)
                                                    .conferenceTheme(bookRoomParam.getConferenceTheme())
                                                    .conferenceType(
                                                            Objects.nonNull(conferenceType) ? conferenceType.getName() : null)
                                                    .build()));
        return flowClient.submitInnerServer(command);
    }

    /**
     * 检查时间段是否被预约
     *
     * @param result
     * @param timeInterval
     */
    private void checkTimeInterval(List<BookRoom> result, String timeInterval) {
        for (BookRoom room : result) {
            //已预约的多个时间段
            String[] split1 = room.getTimeInterval().split(",");
            for (String time : split1) {
                //已预约单个时间段拆分
                String[] split2 = time.split("-");
                //准备预约的多个时间段
                String[] paramDate = timeInterval.split(",");
                for (String date : paramDate) {
                    //准备预约单个时间段拆分
                    String[] split3 = date.split("-");
                    boolean isBookRoom = this.bookRoomCheck(split2, split3);
                    Assert.isConflict(isBookRoom, "预约时间段存在已被预约请刷新后重试");
                }
            }

        }
    }

    /**
     * 校验待预约时间段是否被预约
     *
     * @param split2 已预约时间段
     * @param split3 待预约时间段
     *
     * @return
     */
    private boolean bookRoomCheck(String[] split2, String[] split3) {
        //判断已预约的开始时间是否在待预约开始时间之前并且已预约结束时间在待预约结束时间之后
        LocalDateTime bookedStartTime = DateUtils.parse(split2[0], "HH:mm");
        LocalDateTime preBookStartTime = DateUtils.parse(split3[0], "HH:mm");
        LocalDateTime bookedEndTime = DateUtils.parse(split2[1], "HH:mm");
        LocalDateTime preBookEndTime = DateUtils.parse(split3[1], "HH:mm");
        boolean isBookRoom = (bookedStartTime.isBefore(preBookStartTime) || bookedStartTime.isEqual(preBookStartTime))
                             && (bookedEndTime.isAfter(preBookEndTime) || bookedEndTime.isEqual(preBookEndTime));
        //特殊情况，当待预约结束时间为00:00时，且已预约结束时间不为00：00时应允许被预约
        if (StringUtils.equals(split3[1], "00:00") && !StringUtils.equals(split2[1], "00:00")) {
            isBookRoom = false;
        }
        return isBookRoom;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelBookRoom(Long bookId, Boolean isCancel, String cancelReason) {
        BookRoom bookRoom = this.getById(bookId);
        bookRoom.setStatus(BookRoomEnum.CANCEL_RESERVATION);
        bookRoom.setCancelReason(cancelReason);
        this.updateById(bookRoom);
        //插入操作记录
        UserDTO user = userClient.getUser(SecurityContextHolder.getPrincipal().getId());
        Conference conference = conferenceService.getById(bookRoom.getConferenceId());
        String conferenceDevice = bookRoomMapper.getConferenceDevice(bookRoom.getConferenceId());
        List<LocalDateTime> dateList = bookIntervalRecordService.list(new QueryWrapper<BookIntervalRecord>().eq("book_id", bookId))
                .stream().map(BookIntervalRecord::getAppointmentDate)
                .toList();
        AppointmentRecord appointmentRecord = new AppointmentRecord();
        appointmentRecord.setTimeInterval(bookRoom.getTimeInterval());
        appointmentRecord.setOperate(OperateEnum.CANCEL_RESERVATION);
        appointmentRecord.setConferenceName(conference.getName());
        appointmentRecord.setUserId(user.getId());
        appointmentRecord.setUserName(user.getName());
        appointmentRecord.setDepartmentName(
                Optional.ofNullable(user.getOrganization()).orElse(new UserDTO.Organization()).getName());
        appointmentRecord.setConferenceDevice(conferenceDevice);
        appointmentRecord.setAppointmentDate(dateList.getFirst());
        appointmentRecord.setGoodsNames(bookRoom.getGoodsNames());
        appointmentRecord.setCancelReason(cancelReason);
        appointmentRecord.setProcessInsId(bookRoom.getProcessInsId());
        appointmentRecordService.save(appointmentRecord);
        //消息发送
        Set<Long> userIds = new HashSet<>();
        if (Objects.nonNull(bookRoom.getParticipantType()) && StringUtils.isNotBlank(bookRoom.getParticipantIds())) {
            List<Long> partcipantIdList = Arrays.stream(bookRoom.getParticipantIds().split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            this.getNoticeUserIds(bookRoom.getParticipantType(), partcipantIdList, userIds);
        }
        String msgSceneCode = "";
        //当前用户是否是预约人自己
        if (!bookRoom.getSubscribePerson().equals(SecurityContextHolder.getPrincipal().getId())) {
            isCancel = false;
        }
        List<UserDTO> userDTOS = new ArrayList<>();
        if (isCancel.equals(true)) {
            userIds = Arrays.stream(conference.getAdministrators().split(","))
                    .map(Long::valueOf).collect(Collectors.toSet());
            userDTOS = userClient.listUserByIds(userIds);
            msgSceneCode = "MEETING_CANCEL_REMINDER";
        } else {
            userIds.add(bookRoom.getSubscribePerson());
            userDTOS = userClient.listUserByIds(userIds);
            msgSceneCode = "MEETING_REVOKE_REMINDER";
        }
        for (UserDTO userDTO : userDTOS) {
            Map<String, String> tmpParams = new HashMap<>();
            tmpParams.put("receiveName", userDTO.getName());
            tmpParams.put("sendName", user.getName());
            tmpParams.put("timeInterval", bookRoom.getTimeInterval());
            tmpParams.put("conferenceName", conference.getName());
            tmpParams.put("month", String.valueOf(dateList.getFirst().getMonthValue()));
            tmpParams.put("day", String.valueOf(dateList.getFirst().getDayOfMonth()));
            sendMsg(Collections.singleton(userDTO.getId()), msgSceneCode, tmpParams);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateBookRoom(String processInstanceId) {
        BookRoom bookRoom = this.lambdaQuery()
                .eq(BookRoom::getProcessInsId, processInstanceId)
                .last("limit 1").one();
        bookRoom.setStatus(BookRoomEnum.RESERVED);
        this.updateById(bookRoom);
        //生成预约操作记录
        Conference conference = conferenceService.getById(bookRoom.getConferenceId());
        UserDTO user = userClient.getUser(bookRoom.getSubscribePerson());
        String conferenceDevice = bookRoomMapper.getConferenceDevice(bookRoom.getConferenceId());
        List<LocalDateTime> dateList = bookIntervalRecordService
                .list(new QueryWrapper<BookIntervalRecord>().eq("book_id", bookRoom.getId()))
                .stream().map(BookIntervalRecord::getAppointmentDate)
                .toList();
        AppointmentRecord appointmentRecord = new AppointmentRecord();
        appointmentRecord.setTimeInterval(bookRoom.getTimeInterval());
        appointmentRecord.setOperate(OperateEnum.APPOINTMENT);
        appointmentRecord.setConferenceName(conference.getName());
        appointmentRecord.setUserId(user.getId());
        appointmentRecord.setUserName(user.getName());
        appointmentRecord.setDepartmentName(Objects.isNull(user.getOrganization()) ? "" : user.getOrganization().getName());
        appointmentRecord.setConferenceDevice(conferenceDevice);
        appointmentRecord.setAppointmentDate(dateList.getFirst());
        appointmentRecord.setGoodsNames(bookRoom.getGoodsNames());
        appointmentRecord.setProcessInsId(bookRoom.getProcessInsId());
        appointmentRecordService.save(appointmentRecord);
        //发送节目
        sendProgram(bookRoom);
        //发送消息
        Set<Long> userIds = new HashSet<>();
        if (Objects.nonNull(bookRoom.getParticipantType()) && StringUtils.isNotBlank(bookRoom.getParticipantIds())) {
            List<Long> partcipantIdList = Arrays.stream(bookRoom.getParticipantIds().split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toList());
            this.getNoticeUserIds(bookRoom.getParticipantType(), partcipantIdList, userIds);
        }

        List<UserDTO> userDTOS = userClient.listUserByIds(userIds);

        for (UserDTO userDTO : userDTOS) {
            Map<String, String> tmpParams = new HashMap<>();
            tmpParams.put("receiveName", userDTO.getName());
            tmpParams.put("sendName", user.getName());
            tmpParams.put("timeInterval", bookRoom.getTimeInterval());
            tmpParams.put("conferenceName", conference.getName());
            tmpParams.put("month", String.valueOf(dateList.getFirst().getMonthValue()));
            tmpParams.put("day", String.valueOf(dateList.getFirst().getDayOfMonth()));
            sendMsg(Collections.singleton(userDTO.getId()), "MEETING_APPOINTMENT_REMINDER", tmpParams);
        }
    }

    @Override
    public Pagination<AppointmentVo> getAppointmentVos(AppointmentParam param) {
        LocalDateTime dateTime = DateUtils.now();
        String date = DateUtils.format(dateTime, "yyyy-MM-dd");
        Pagination<AppointmentVo> appointmentVo = conferenceService.getAppointmentVo(param);
        if (!appointmentVo.getRecords().isEmpty()) {
            List<Long> ids = appointmentVo.getRecords().stream().map(AppointmentVo::getConferenceId).collect(Collectors.toList());
            //会仪室可约时间段
            List<BookTimeInterval> list = bookTimeIntervalService.list(Wrappers.<BookTimeInterval>lambdaQuery()
                                                                               .in(CollectionUtils.isNotEmpty(ids),
                                                                                   BookTimeInterval::getConferenceId, ids));
            List<AppointmentDetailsVo> appointmentDetailsVos = ObjectUtils.copying(list, AppointmentDetailsVo.class);
            //会议室预约记录
            List<BookIntervalRecord> bookIntervalRecords = bookRoomMapper.getBookIntervalRecords(param.getDate(), ids);
            Map<Long, List<BookIntervalRecord>> bookIntervalRecordMap = bookIntervalRecords.stream().collect(
                    Collectors.groupingBy(BookIntervalRecord::getConferenceId));
            //当天周期
            if (date.equals(param.getDate())) {
                //时间组装
                int minute = dateTime.getMinute();
                int hour = dateTime.getHour();
                String minuteFlag = "";
                List<String> passTime = new ArrayList<>();
                //过期时间组装
                for (int i = 0; i <= hour; i++) {
                    if (i < 10) {
                        passTime.add("0" + i + ":00-0" + i + ":30");
                        if (i == 9) {
                            passTime.add("0" + i + ":30-" + (i + 1) + ":00");
                        } else {
                            passTime.add("0" + i + ":30-0" + (i + 1) + ":00");
                        }

                    } else {
                        passTime.add(i + ":00-" + i + ":30");
                        if (i == 23) {
                            passTime.add(i + ":30-00:00");
                        } else {
                            passTime.add(i + ":30-" + (i + 1) + ":00");
                        }

                    }

                }
                if (minute <= 30) {
                    if (hour < 10) {
                        minuteFlag = "0" + hour + ":00-0" + hour + ":30";
                    } else {
                        minuteFlag = hour + ":00-" + hour + ":30";
                    }
                    passTime = passTime.subList(0, passTime.size() - 2);
                } else {
                    if (hour < 10) {
                        minuteFlag = "0" + hour + ":30-0" + (hour + 1) + ":00";
                    } else {
                        minuteFlag = hour + ":30-" + (hour + 1) + ":00";
                    }
                    passTime = passTime.subList(0, passTime.size() - 1);
                }
                //预约状态设置
                for (AppointmentDetailsVo appointmentDetailsVo : appointmentDetailsVos) {
                    List<BookIntervalRecord> bookIntervalRecords1 = bookIntervalRecordMap.get(
                            appointmentDetailsVo.getConferenceId());
                    if (CollectionUtils.isNotEmpty(bookIntervalRecords1)) {
                        for (BookIntervalRecord bookIntervalRecord : bookIntervalRecords1) {
                            if (appointmentDetailsVo.getConferenceId().equals(bookIntervalRecord.getConferenceId()) &&
                                appointmentDetailsVo.getTimeInterval().equals(bookIntervalRecord.getTimeInterval()) &&
                                minuteFlag.equals(bookIntervalRecord.getTimeInterval())) {
                                appointmentDetailsVo.setBookStatus(BookRoomEnum.APPROVAL.equals(bookIntervalRecord.getStatus()) ?
                                                                   BookRoomEnum.APPROVAL :
                                                                   BookRoomEnum.BEING_USED);
                                appointmentDetailsVo.setBookId(bookIntervalRecord.getBookId());
                            } else if (appointmentDetailsVo.getConferenceId().equals(bookIntervalRecord.getConferenceId()) &&
                                       appointmentDetailsVo.getTimeInterval().equals(bookIntervalRecord.getTimeInterval())) {
                                appointmentDetailsVo.setBookStatus(bookIntervalRecord.getStatus());
                                appointmentDetailsVo.setBookId(bookIntervalRecord.getBookId());
                            }
                        }
                    }
                    //过期状态设置
                    if (passTime.contains(appointmentDetailsVo.getTimeInterval())) {
                        appointmentDetailsVo.setBookStatus(BookRoomEnum.EXPIRE);
                    }
                }

            } else {//其他时间周期
                for (AppointmentDetailsVo appointmentDetailsVo : appointmentDetailsVos) {
                    List<BookIntervalRecord> bookIntervalRecords1 = bookIntervalRecordMap.get(
                            appointmentDetailsVo.getConferenceId());
                    if (CollectionUtils.isNotEmpty(bookIntervalRecords1)) {
                        for (BookIntervalRecord bookIntervalRecord : bookIntervalRecords1) {
                            if (appointmentDetailsVo.getConferenceId().equals(bookIntervalRecord.getConferenceId()) &&
                                appointmentDetailsVo.getTimeInterval().equals(bookIntervalRecord.getTimeInterval())) {
                                appointmentDetailsVo.setBookStatus(bookIntervalRecord.getStatus());
                                appointmentDetailsVo.setBookId(bookIntervalRecord.getBookId());
                            }
                        }
                    }

                }
            }
            Map<Long, List<AppointmentDetailsVo>> collect = appointmentDetailsVos.stream().collect(
                    Collectors.groupingBy(AppointmentDetailsVo::getConferenceId));
            appointmentVo.getRecords().forEach(s -> {
                s.setAppointmentDetails(collect.get(s.getConferenceId()));
            });

        }

        return appointmentVo;
    }

    @Override
    public Pagination<MobileAppointmentVo> getAppAppointmentVos(AppointmentParam param) {
        LocalDateTime dateTime = DateUtils.now();
        String date = DateUtils.format(dateTime, "yyyy-MM-dd");
        Page<Conference> page = conferenceService.page(PagingUtils.convert(param), Wrappers.lambdaQuery());
        List<Conference> records = page.getRecords();
        List<MobileAppointmentVo> mobileAppointmentVos = new ArrayList<>();
        Page<MobileAppointmentVo> mobileAppointmentVoPage = new Page<>(param.getPageNumber(), param.getPageSize());
        for (Conference record : records) {
            MobileAppointmentVo mobileAppointmentVo = new MobileAppointmentVo();
            ObjectUtils.copying(record, mobileAppointmentVo);
            mobileAppointmentVo.setConferenceId(record.getId());
            Long id = record.getId();
            List<BookTimeInterval> appointmentDetailsVoList = bookTimeIntervalService.list(
                    Wrappers.<BookTimeInterval>lambdaQuery().eq(BookTimeInterval::getConferenceId, id));
            List<BookIntervalRecord> bookIntervalRecords = bookIntervalRecordService.list(
                    new QueryWrapper<BookIntervalRecord>().eq("conference_id", id).like("appointment_date", param.getDate()));
            int count = appointmentDetailsVoList.size() - bookIntervalRecords.size();
            if (CollectionUtils.isNotEmpty(appointmentDetailsVoList) && count > 0) {
                mobileAppointmentVo.setConferenceBookStatus(ConferenceBookStatusEnum.AVALIABLE);
            } else {
                mobileAppointmentVo.setConferenceBookStatus(ConferenceBookStatusEnum.NOAPPOINTMENT);
            }
            List<ConferenceDevice> list = conferenceDeviceService.list(
                    Wrappers.<ConferenceDevice>lambdaQuery().eq(ConferenceDevice::getConferenceId, id));
            StringBuilder deviceName = new StringBuilder();
            if (CollectionUtils.isNotEmpty(list)) {
                for (ConferenceDevice conferenceDevice : list) {
                    deviceName.append(" ").append(conferenceDevice.getName());
                }
                mobileAppointmentVo.setDeviceName(String.valueOf(deviceName));
            }
            List<AppointmentDetailsVo> appointmentDetailsVos = ObjectUtils.copying(appointmentDetailsVoList,
                                                                                   AppointmentDetailsVo.class);
            Map<Long, List<BookIntervalRecord>> bookIntervalRecordMap = bookIntervalRecords.stream().collect(
                    Collectors.groupingBy(BookIntervalRecord::getConferenceId));

            //当天周期
            if (date.equals(param.getDate())) {
                //时间组装
                int minute = dateTime.getMinute();
                int hour = dateTime.getHour();
                String minuteFlag = "";
                List<String> passTime = new ArrayList<>();
                //过期时间组装
                for (int i = 0; i <= hour; i++) {
                    if (i < 10) {
                        passTime.add("0" + i + ":00-0" + i + ":30");
                        if (i == 9) {
                            passTime.add("0" + i + ":30-" + (i + 1) + ":00");
                        } else {
                            passTime.add("0" + i + ":30-0" + (i + 1) + ":00");
                        }

                    } else {
                        passTime.add(i + ":00-" + i + ":30");
                        if (i == 23) {
                            passTime.add(i + ":30-00:00");
                        } else {
                            passTime.add(i + ":30-" + (i + 1) + ":00");
                        }

                    }

                }
                if (minute <= 30) {
                    if (hour < 10) {
                        minuteFlag = "0" + hour + ":00-0" + hour + ":30";
                    } else {
                        minuteFlag = hour + ":00-" + hour + ":30";
                    }
                    passTime = passTime.subList(0, passTime.size() - 2);
                } else {
                    if (hour < 10) {
                        minuteFlag = "0" + hour + ":30-0" + (hour + 1) + ":00";
                    } else {
                        minuteFlag = hour + ":30-" + (hour + 1) + ":00";
                    }
                    passTime = passTime.subList(0, passTime.size() - 1);
                }

                for (AppointmentDetailsVo appointmentDetailsVo : appointmentDetailsVos) {
                    List<BookIntervalRecord> bookIntervalRecords1 = bookIntervalRecordMap.get(
                            appointmentDetailsVo.getConferenceId());
                    if (CollectionUtils.isNotEmpty(bookIntervalRecords1)) {
                        for (BookIntervalRecord bookIntervalRecord : bookIntervalRecords1) {
                            if (appointmentDetailsVo.getConferenceId().equals(bookIntervalRecord.getConferenceId()) &&
                                appointmentDetailsVo.getTimeInterval().equals(bookIntervalRecord.getTimeInterval()) &&
                                minuteFlag.equals(bookIntervalRecord.getTimeInterval())) {
                                appointmentDetailsVo.setBookStatus(BookRoomEnum.BEING_USED);
                                appointmentDetailsVo.setBookId(bookIntervalRecord.getBookId());
                            } else if (appointmentDetailsVo.getConferenceId().equals(bookIntervalRecord.getConferenceId()) &&
                                       appointmentDetailsVo.getTimeInterval().equals(bookIntervalRecord.getTimeInterval())) {
                                appointmentDetailsVo.setBookStatus(BookRoomEnum.RESERVED);
                                appointmentDetailsVo.setBookId(bookIntervalRecord.getBookId());
                            }
                        }
                    }
                    //过期状态设置
                    if (passTime.contains(appointmentDetailsVo.getTimeInterval())) {
                        appointmentDetailsVo.setBookStatus(BookRoomEnum.EXPIRE);
                    }
                }
            } else {//其他时间周期
                for (AppointmentDetailsVo appointmentDetailsVo : appointmentDetailsVos) {
                    List<BookIntervalRecord> bookIntervalRecords1 = bookIntervalRecordMap.get(
                            appointmentDetailsVo.getConferenceId());
                    if (CollectionUtils.isNotEmpty(bookIntervalRecords1)) {
                        for (BookIntervalRecord bookIntervalRecord : bookIntervalRecords1) {
                            if (appointmentDetailsVo.getConferenceId().equals(bookIntervalRecord.getConferenceId()) &&
                                appointmentDetailsVo.getTimeInterval().equals(bookIntervalRecord.getTimeInterval())) {
                                appointmentDetailsVo.setBookStatus(BookRoomEnum.RESERVED);
                                appointmentDetailsVo.setBookId(bookIntervalRecord.getBookId());
                            }
                        }
                    }

                }
            }
            mobileAppointmentVo.setAppointmentDetails(appointmentDetailsVos);
            mobileAppointmentVos.add(mobileAppointmentVo);
            mobileAppointmentVoPage.setRecords(mobileAppointmentVos);
        }
        return PagingUtils.convert(mobileAppointmentVoPage, MobileAppointmentVo.class);
    }

    @Override
    public Pagination<MobileAppointmentVo> getNewAppAppointmentVos(AppointmentParam param) {
        LocalDateTime dateTime = DateUtils.now();
        String date = DateUtils.format(dateTime, "yyyy-MM-dd");
        //正在使用
        String usagePeriod = "";
        List<String> passTime = new ArrayList<>();
        if (date.equals(param.getDate())) {
            //时间组装
            int minute = dateTime.getMinute();
            int hour = dateTime.getHour();

            //过期时间组装
            for (int i = 0; i <= hour; i++) {
                if (i < 10) {
                    passTime.add("0" + i + ":00-0" + i + ":30");
                    if (i == 9) {
                        passTime.add("0" + i + ":30-" + (i + 1) + ":00");
                    } else {
                        passTime.add("0" + i + ":30-0" + (i + 1) + ":00");
                    }
                } else {
                    passTime.add(i + ":00-" + i + ":30");
                    if (i == 23) {
                        passTime.add(i + ":30-00:00");
                    } else {
                        passTime.add(i + ":30-" + (i + 1) + ":00");
                    }
                }
            }

            if (minute > 30) {
                usagePeriod = passTime.getLast();
                passTime = passTime.subList(0, passTime.size() - 1);
            } else {
                usagePeriod = passTime.get(passTime.size() - 2);
                passTime = passTime.subList(0, passTime.size() - 2);
            }

        }
        conferenceService.buildFilterParam(param);
        Page<MobileAppointmentVo> appointmentVoPage = conferenceMapper.getAppConference(PagingUtils.convert(param), param);
        if (CollectionUtils.isEmpty(appointmentVoPage.getRecords())) {
            return PagingUtils.convert(appointmentVoPage);
        }

        //会议室
        List<Long> collect = appointmentVoPage.getRecords().stream().map(MobileAppointmentVo::getConferenceId)
                .collect(Collectors.toList());
        //预约记录
        List<FilterIntervalRecordVo> filterIntervalRecords = bookIntervalRecordMapper.getFilterIntervalRecords(collect,
                                                                                                               param.getDate());
        return handleTime(appointmentVoPage, filterIntervalRecords, passTime, usagePeriod);

    }

    /**
     * @Description 时段处理
     * @Author lrj
     */
    private Pagination<MobileAppointmentVo> handleTime(Page<MobileAppointmentVo> appointmentVoPage,
                                                       List<FilterIntervalRecordVo> filterIntervalRecords, List<String> passTime,
                                                       String usagePeriod
                                                      ) {
        Map<Long, List<FilterIntervalRecordVo>> collect = filterIntervalRecords.stream().collect(
                Collectors.groupingBy(FilterIntervalRecordVo::getConferenceId));
        for (MobileAppointmentVo record : appointmentVoPage.getRecords()) {
            List<AppointmentDetailsVo> appointmentDetails = record.getAppointmentDetails();
            List<FilterIntervalRecordVo> filterIntervalRecordVos = collect.get(record.getConferenceId());
            List<String> timeIntervals = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(filterIntervalRecordVos)) {
                timeIntervals = filterIntervalRecordVos.stream().map(FilterIntervalRecordVo::getTimeInterval)
                        .toList();
            }
            if (CollectionUtils.isNotEmpty(passTime)) {
                for (AppointmentDetailsVo appointmentDetail : appointmentDetails) {
                    if (timeIntervals.contains(appointmentDetail.getTimeInterval())) {
                        appointmentDetail.setBookStatus(filterIntervalRecordVos.getFirst().getStatus());
                    }
                    if (passTime.contains(appointmentDetail.getTimeInterval())) {
                        appointmentDetail.setBookStatus(BookRoomEnum.EXPIRE);
                    }
                    if (usagePeriod.equals(appointmentDetail.getTimeInterval()) &&
                        appointmentDetail.getBookStatus().equals(BookRoomEnum.RESERVED)) {
                        BookRoomEnum status = filterIntervalRecordVos.getFirst().getStatus();
                        appointmentDetail.setBookStatus(
                                BookRoomEnum.APPROVAL.equals(status) ? BookRoomEnum.APPROVAL : BookRoomEnum.BEING_USED);
                    }
                }

            } else {
                for (AppointmentDetailsVo appointmentDetail : appointmentDetails) {
                    if (timeIntervals.contains(appointmentDetail.getTimeInterval())) {
                        appointmentDetail.setBookStatus(filterIntervalRecordVos.getFirst().getStatus());
                    }

                }
            }

            List<AppointmentDetailsVo> reservable = appointmentDetails.stream().filter(s -> s.getBookStatus()
                            .equals(BookRoomEnum.RESERVABLE))
                    .toList();
            if (CollectionUtils.isNotEmpty(reservable)) {
                record.setConferenceBookStatus(ConferenceBookStatusEnum.AVALIABLE);
            } else {
                record.setConferenceBookStatus(ConferenceBookStatusEnum.NOAPPOINTMENT);
            }

        }
        return PagingUtils.convert(appointmentVoPage);

    }

    @Override
    public MobileAppointmentVo getAppAppointmentDetailVos(AppointmentParam param) {
        MobileAppointmentVo conferenceDetail = conferenceMapper.getConferenceDetail(param.getConferenceId());
        //会仪室可约时间段
        List<BookTimeInterval> list = bookTimeIntervalService.list(Wrappers.<BookTimeInterval>lambdaQuery()
                                                                           .eq(BookTimeInterval::getConferenceId,
                                                                               conferenceDetail.getConferenceId()));
        List<AppointmentDetailsVo> appointmentDetailsVos = ObjectUtils.copying(list, AppointmentDetailsVo.class);
        List<Long> ids = new ArrayList<>();
        ids.add(conferenceDetail.getConferenceId());
        //会议室预约记录
        List<BookIntervalRecord> bookIntervalRecords = bookRoomMapper.getBookIntervalRecords(param.getDate(), ids);

        LocalDateTime dateTime = DateUtils.now();
        String date = DateUtils.format(dateTime, "yyyy-MM-dd");
        if (date.equals(param.getDate())) {
            //时间组装
            int minute = dateTime.getMinute();
            int hour = dateTime.getHour();
            String minuteFlag = "";
            List<String> passTime = new ArrayList<>();
            //过期时间组装
            for (int i = 0; i <= hour; i++) {
                if (i < 10) {
                    passTime.add("0" + i + ":00-0" + i + ":30");
                    if (i == 9) {
                        passTime.add("0" + i + ":30-" + (i + 1) + ":00");
                    } else {
                        passTime.add("0" + i + ":30-0" + (i + 1) + ":00");
                    }

                } else {
                    passTime.add(i + ":00-" + i + ":30");
                    if (i == 23) {
                        passTime.add(i + ":30-00:00");
                    } else {
                        passTime.add(i + ":30-" + (i + 1) + ":00");
                    }

                }

            }
            if (minute <= 30) {
                if (hour < 10) {
                    minuteFlag = "0" + hour + ":00-0" + hour + ":30";
                } else {
                    minuteFlag = hour + ":00-" + hour + ":30";
                }
                passTime = passTime.subList(0, passTime.size() - 2);
            } else {
                if (hour < 10) {
                    minuteFlag = "0" + hour + ":30-0" + (hour + 1) + ":00";
                } else {
                    minuteFlag = hour + ":30-" + (hour + 1) + ":00";
                }
                passTime = passTime.subList(0, passTime.size() - 1);
            }

            //预约状态设置
            for (AppointmentDetailsVo appointmentDetailsVo : appointmentDetailsVos) {
                if (CollectionUtils.isNotEmpty(bookIntervalRecords)) {
                    for (BookIntervalRecord bookIntervalRecord : bookIntervalRecords) {
                        if (appointmentDetailsVo.getTimeInterval().equals(bookIntervalRecord.getTimeInterval()) &&
                            minuteFlag.equals(bookIntervalRecord.getTimeInterval())) {
                            appointmentDetailsVo.setBookStatus(BookRoomEnum.APPROVAL.equals(bookIntervalRecord.getStatus()) ?
                                                               BookRoomEnum.APPROVAL :
                                                               BookRoomEnum.BEING_USED);
                            appointmentDetailsVo.setBookId(bookIntervalRecord.getBookId());
                        } else if (appointmentDetailsVo.getTimeInterval().equals(bookIntervalRecord.getTimeInterval())) {
                            appointmentDetailsVo.setBookStatus(bookIntervalRecord.getStatus());
                            appointmentDetailsVo.setBookId(bookIntervalRecord.getBookId());
                        }
                    }
                }
                //过期状态设置
                if (passTime.contains(appointmentDetailsVo.getTimeInterval())) {
                    appointmentDetailsVo.setBookStatus(BookRoomEnum.EXPIRE);
                }
            }

        } else {
            for (AppointmentDetailsVo appointmentDetailsVo : appointmentDetailsVos) {
                if (CollectionUtils.isNotEmpty(bookIntervalRecords)) {
                    for (BookIntervalRecord bookIntervalRecord : bookIntervalRecords) {
                        if (appointmentDetailsVo.getConferenceId().equals(bookIntervalRecord.getConferenceId()) &&
                            appointmentDetailsVo.getTimeInterval().equals(bookIntervalRecord.getTimeInterval())) {
                            appointmentDetailsVo.setBookStatus(bookIntervalRecord.getStatus());
                            appointmentDetailsVo.setBookId(bookIntervalRecord.getBookId());
                        }
                    }
                }

            }
        }
        for (Long id : ids) {
            long i = bookIntervalRecords.stream().filter(b -> b.getConferenceId().equals(id)).count();
            List<BookTimeInterval> conference_id = bookTimeIntervalService.list(
                    new QueryWrapper<BookTimeInterval>().eq("conference_id", id));
            if (conference_id.size() > i) {
                conferenceDetail.setConferenceBookStatus(ConferenceBookStatusEnum.AVALIABLE);
            } else {
                conferenceDetail.setConferenceBookStatus(ConferenceBookStatusEnum.NOAPPOINTMENT);
            }
        }

        conferenceDetail.setAppointmentDetails(appointmentDetailsVos);
        return conferenceDetail;
    }

    @Override
    public Pagination<AppointmentRecordVo> appointmentPage(AppointmentParam param) {
        Page<AppointmentRecordVo> appointmentRecordVoPage = appointmentRecordService.selectAppointRecord(param);
        Pagination<AppointmentRecordVo> convert = PagingUtils.convert(appointmentRecordVoPage, AppointmentRecordVo.class);
        this.setTimeInterval(convert.getRecords());
        return convert;
    }

    @Override
    public BookRoomVo getBookRoomVo(Long bookId) {
        BookRoomVo bookRoomVo = bookRoomMapper.getBookRoomVo(bookId);
        String conferenceDevice = bookRoomMapper.getConferenceDevice(bookRoomVo.getConferenceId());
        List<BookIntervalRecord> list = bookIntervalRecordService.list(Wrappers.<BookIntervalRecord>lambdaQuery()
                                                                               .eq(BookIntervalRecord::getBookId, bookId));
        bookRoomVo.setAppointmentDate(list.getFirst().getAppointmentDate());
        UserDTO user = userClient.getUser(bookRoomVo.getSubscribePerson());
        bookRoomVo.setSubscribePersonName(user.getName());
        bookRoomVo.setOrganization(user.getOrganization());
        bookRoomVo.setConferenceDevices(conferenceDevice);
        bookRoomVo.setIsCancel(bookRoomVo.getSubscribePerson().equals(SecurityContextHolder.getPrincipal().getId()));
        if (Objects.nonNull(bookRoomVo.getParticipantType()) && StringUtils.isNotBlank(bookRoomVo.getParticipantIds())) {
            List<Long> longList = Arrays.stream(bookRoomVo.getParticipantIds().split(","))
                    .map(Long::valueOf).collect(Collectors.toList());
            List<ConferenceBaseEntityVO> personInfo = bookRoomVo.getParticipantType().getPersonInfo(longList);
            bookRoomVo.setParticipantList(personInfo);
        }
        return bookRoomVo;
    }

    @Override
    public Map<String, Object> getAppStatistics(String timeFlag) {
        HashMap<String, Object> remap = new HashMap<>();
        LocalDateTime date = DateUtils.now();
        LocalDateTime endTime = DateUtils.endOfDay(date);
        LocalDateTime beginTime = null;

        if (timeFlag.equals("TODAY")) {
            beginTime = DateUtils.beginOfDay(endTime);
        } else if (timeFlag.equals("THIS_WEEK")) {
            beginTime = DateUtils.beginOfWeek(endTime);
        } else if (timeFlag.equals("THIS_MONTH")) {
            beginTime = DateUtils.beginOfMonth(endTime);
        } else if (timeFlag.equals("THIS_YEAR")) {
            beginTime = DateUtils.beginOfYear(endTime);
        }
        long appointmentCount = appointmentRecordService.count(Wrappers.<AppointmentRecord>lambdaQuery()
                                                                       .eq(AppointmentRecord::getUserId,
                                                                           SecurityContextHolder.getPrincipal().getId())
                                                                       .eq(AppointmentRecord::getOperate, OperateEnum.APPOINTMENT)
                                                                       .between(BasicEntity::getCreateTime, beginTime, endTime));
        long reservationCount = appointmentRecordService.count(Wrappers.<AppointmentRecord>lambdaQuery()
                                                                       .eq(AppointmentRecord::getUserId,
                                                                           SecurityContextHolder.getPrincipal().getId())
                                                                       .eq(AppointmentRecord::getOperate,
                                                                           OperateEnum.CANCEL_RESERVATION)
                                                                       .between(BasicEntity::getCreateTime, beginTime, endTime));
        remap.put("appointmentCount", appointmentCount);
        remap.put("reservationCount", reservationCount);
        return remap;
    }

    @Override
    public Pagination<BookRoomAppVo> myAppointmentPage(AppointmentParam param) {
        Page<BookRoomAppVo> appMyAppointment = bookRoomMapper.getAppMyAppointment(PagingUtils.convert(param),
                                                                                  SecurityContextHolder.getPrincipal().getId());
        return PagingUtils.convert(appMyAppointment);
    }

    /**
     * @param param
     *
     * @return
     *
     * @Description app会议室预约列表
     */
    @Override
    public BookRoomAppointmentVo bookRoomAppointmentInfo(AppointmentParam param) {
        Conference byId = conferenceService.getById(param.getConferenceId());
        Assert.isNotFound(Objects.isNull(byId), "会议室不存在");
        BookRoomAppointmentVo rs = new BookRoomAppointmentVo();
        rs.setCapacity(byId.getCapacity());
        rs.setAreaName(byId.getAreaName());
        rs.setConferenceId(byId.getId());
        rs.setConferenceName(byId.getName());
        LocalDateTime dateTime = DateUtils.now();
        int minute = dateTime.getMinute();
        String date = DateUtils.format(dateTime, "yyyy-MM-dd");

        //会议室预约记录
        List<BookRoomAppointmentRecordVo> appointmentRecordList = bookIntervalRecordMapper.getAppointmentRecordList(
                param.getDate(), param.getConferenceId());
        //
        //正在使用
        String usagePeriod = "";
        //当前时分
        String currentPeriod = DateUtils.format(dateTime, "HH:mm");
        List<String> passTime = new ArrayList<>();
        boolean isToday = false;
        if (date.equals(param.getDate())) {
            isToday = true;
            int hour = dateTime.getHour();
            //过期时间组装
            for (int i = 0; i <= hour; i++) {
                if (i < 10) {
                    passTime.add("0" + i + ":00-0" + i + ":30");
                    if (i == 9) {
                        passTime.add("0" + i + ":30-" + (i + 1) + ":00");
                    } else {
                        passTime.add("0" + i + ":30-0" + (i + 1) + ":00");
                    }
                } else {
                    passTime.add(i + ":00-" + i + ":30");
                    if (i == 23) {
                        passTime.add(i + ":30-00:00");
                    } else {
                        passTime.add(i + ":30-" + (i + 1) + ":00");
                    }
                }
            }
            if (minute > 30) {
                usagePeriod = passTime.getLast();
                passTime = passTime.subList(0, passTime.size() - 1);
            } else {
                usagePeriod = passTime.get(passTime.size() - 2);
                passTime = passTime.subList(0, passTime.size() - 2);
            }

        }
        List<BookRoomAppointmentRecordVo> bookRoomAppointmentRecordVos = handleDetail(appointmentRecordList, usagePeriod,
                                                                                      passTime);
        Set<BookRoomEnum> collect = bookRoomAppointmentRecordVos.stream().map(BookRoomAppointmentRecordVo::getBookStatus)
                .collect(Collectors.toSet());
        if (collect.contains(BookRoomEnum.RESERVABLE)) {
            rs.setConferenceBookStatus(ConferenceBookStatusEnum.AVALIABLE);
        } else {
            rs.setConferenceBookStatus(ConferenceBookStatusEnum.NOAPPOINTMENT);
        }
        List<BookRoomAppointmentRecordVo> BookRooms = bookRoomAppointmentRecordVos.stream().filter(s -> s.getBookStatus()
                                                                                                                .equals(BookRoomEnum.RESERVED) ||
                                                                                                        s.getBookStatus()
                                                                                                                .equals(BookRoomEnum.EXPIRE) ||
                                                                                                        s.getBookStatus()
                                                                                                                .equals(BookRoomEnum.BEING_USED) ||
                                                                                                        s.getBookStatus()
                                                                                                                .equals(BookRoomEnum.APPROVAL))
                .toList();
        //数据过滤
        List<BookRoomAppointmentRecordVo> filterBookRooms = BookRooms.stream()
                .filter(s -> StringUtils.isNotEmpty(
                        s.getUserName())).collect(Collectors.toList());
        //时间段处理
        List<BookRoomAppointmentRecordVo> recordVoList = mergeTimeData(filterBookRooms, currentPeriod, isToday);
        rs.setRecords(recordVoList);
        return rs;
    }

    @Override
    public void appointmentExportForPC(AppointmentParam param, HttpServletResponse response) {
        List<AppointmentRecordVo> appointRecordList = appointmentRecordService.selectAppointRecordList(param);
        Assert.isNotFound(CollectionUtils.isEmpty(appointRecordList), "没有预约记录,无法导出");
        this.setTimeInterval(appointRecordList);
        //导出数据转换
        List<AppointmentRecordExportVO> exportResultList = appointRecordList.stream().map(item -> {
            AppointmentRecordExportVO copying = ObjectUtils.copying(item, AppointmentRecordExportVO.class);
            copying.setOperateDesc(item.getOperate().getDescription());
            return ObjectUtils.copying(item, AppointmentRecordExportVO.class);
        }).collect(Collectors.toList());
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        try {
            EasyExcelFactory.write(response.getOutputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .registerWriteHandler(new CustomCellWriteUtil())
                    .head(AppointmentRecordExportVO.class)
                    .sheet("会议室预约记录")
                    .doWrite(exportResultList);
        } catch (IOException e) {
            log.error("会议室预约记录导出失败", e);
            throw new ServiceException("文件下载失败");
        }
    }

    private void setTimeInterval(List<AppointmentRecordVo> appointmentRecordVos) {
        for (AppointmentRecordVo record : appointmentRecordVos) {
            if (StringUtils.isNotBlank(record.getTimeInterval()) && Objects.nonNull(record.getAppointmentDate())) {
                String[] split = record.getTimeInterval().split(",");
                List<String> collect = Arrays.stream(split).sorted().toList();
                //时间段显示处理
                String timeInterval = DateUtils.format(record.getAppointmentDate(), "yyyy-MM-dd") + " ";
                if (collect.size() > 1) {
                    String s = collect.getFirst();
                    String[] beginTime = s.split("-");
                    LocalDateTime parseBegin = DateUtils.parse(beginTime[0], "HH:mm");
                    //结尾时间
                    String s1 = collect.getLast();
                    String[] endTime = s1.split("-");
                    LocalDateTime parseEnd = DateUtils.parse(endTime[1], "HH:mm");
                    int between = (int) DateUtils.between(parseBegin, parseEnd, ChronoUnit.MINUTES);
                    int num = between / 30;
                    if (num == collect.size()) {
                        timeInterval = timeInterval + beginTime[0] + "-" + endTime[1];
                    } else {
                        timeInterval = timeInterval + record.getTimeInterval();
                    }
                } else {
                    timeInterval = timeInterval + record.getTimeInterval();
                }

                record.setTimeInterval(timeInterval);
            }

        }
    }

    private List<BookRoomAppointmentRecordVo> mergeTimeData(List<BookRoomAppointmentRecordVo> filterBookRooms,
                                                            String currentPeriod, boolean isToday
                                                           ) {
        List<BookRoomAppointmentRecordVo> recordVoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(filterBookRooms)) {
            //用户分组 时间排序 合并时间段  当前时间在时间段全为使用中，
            Map<String, List<BookRoomAppointmentRecordVo>> groupByUserName = filterBookRooms
                    .stream().collect(Collectors.groupingBy(BookRoomAppointmentRecordVo::getUserName));
            for (Map.Entry<String, List<BookRoomAppointmentRecordVo>> bookRoomRecord : groupByUserName.entrySet()) {
                //再根据bookRoomId分组
                Map<Long, List<BookRoomAppointmentRecordVo>> groupByBookIdMap = bookRoomRecord.getValue().stream()
                        .collect(Collectors.groupingBy(
                                BookRoomAppointmentRecordVo::getBookId));

                for (Map.Entry<Long, List<BookRoomAppointmentRecordVo>> entry : groupByBookIdMap.entrySet()) {
                    //时间段显示处理
                    String timeInterval = "";
                    //排序时间
                    String sortTime = "";
                    if (entry.getValue().size() > 1) {
                        String intervalFlag = "";
                        for (int i = 0; i < entry.getValue().size(); i++) {
                            String[] otherSplit = entry.getValue().get(i).getTimeInterval().split("-");
                            if (i == 0) {
                                sortTime = otherSplit[0];
                                timeInterval = otherSplit[0] + "-";
                                intervalFlag = otherSplit[1];
                            } else {
                                if (intervalFlag.equals(otherSplit[0])) {
                                    intervalFlag = otherSplit[1];
                                } else {
                                    timeInterval = timeInterval + intervalFlag + "," + otherSplit[0] + "-";
                                    intervalFlag = otherSplit[1];
                                }
                                if (i == entry.getValue().size() - 1) {
                                    timeInterval = timeInterval + intervalFlag;
                                }
                            }
                        }
                    } else {
                        timeInterval = entry.getValue().getFirst().getTimeInterval();
                        sortTime = timeInterval.split("-")[0];
                    }
                    BookRoomAppointmentRecordVo bookRoomAppointmentRecordVo = entry.getValue().getLast();
                    //当前时间是否在会议时间中
                    if (isToday) {
                        for (String time : timeInterval.split(",")) {
                            String[] split = time.split("-");
                            if (DateUtils.parse(split[1], "HH:mm").isAfter(DateUtils.parse(currentPeriod, "HH:mm"))
                                && DateUtils.parse(split[0], "HH:mm").isBefore(DateUtils.parse(currentPeriod, "HH:mm"))) {
                                bookRoomAppointmentRecordVo.setBookStatus(BookRoomEnum.BEING_USED);
                            }
                        }
                    }
                    bookRoomAppointmentRecordVo.setSortTime(DateUtils.parse(sortTime, "HH:mm"));
                    bookRoomAppointmentRecordVo.setTimeInterval(timeInterval);
                    recordVoList.add(bookRoomAppointmentRecordVo);
                }
            }
            recordVoList = recordVoList.stream().sorted((Comparator.comparing(BookRoomAppointmentRecordVo::getSortTime)))
                    .collect(Collectors.toList());
        }
        return recordVoList;
    }

    private List<BookRoomAppointmentRecordVo> handleDetail(List<BookRoomAppointmentRecordVo> appointmentRecordList,
                                                           String usagePeriod, List<String> passTime
                                                          ) {
        List<Long> collect = appointmentRecordList.stream().filter(s -> Objects.nonNull(s.getSubscribePerson()) &&
                                                                        (BookRoomEnum.RESERVED.equals(s.getBookStatus()) ||
                                                                         BookRoomEnum.APPROVAL.equals(s.getBookStatus())))
                .map(BookRoomAppointmentRecordVo::getSubscribePerson)
                .collect(Collectors.toList());
        Map<Long, UserDTO> userDTOMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(collect)) {
            List<UserDTO> userDTOS = userClient.listUserByIds(collect);
            userDTOMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, s -> s));
        }
        //状态处理
        if (CollectionUtils.isNotEmpty(passTime)) {
            for (BookRoomAppointmentRecordVo bookRoomAppointmentRecordVo : appointmentRecordList) {
                if (passTime.contains(bookRoomAppointmentRecordVo.getTimeInterval())) {
                    bookRoomAppointmentRecordVo.setBookStatus(BookRoomEnum.EXPIRE);
                }
                if (usagePeriod.equals(bookRoomAppointmentRecordVo.getTimeInterval()) &&
                    bookRoomAppointmentRecordVo.getBookStatus().equals(BookRoomEnum.RESERVED)) {
                    bookRoomAppointmentRecordVo.setBookStatus(BookRoomEnum.BEING_USED);
                }
                UserDTO userDTO = userDTOMap.get(bookRoomAppointmentRecordVo.getSubscribePerson());
                if (Objects.nonNull(userDTO)) {
                    bookRoomAppointmentRecordVo.setUserName(userDTO.getName());
                    bookRoomAppointmentRecordVo.setDeptName(
                            Optional.ofNullable(userDTO.getOrganization()).orElse(new UserDTO.Organization()).getName());
                }
            }
        } else {
            for (BookRoomAppointmentRecordVo bookRoomAppointmentRecordVo : appointmentRecordList) {

                UserDTO userDTO = userDTOMap.get(bookRoomAppointmentRecordVo.getSubscribePerson());
                if (Objects.nonNull(userDTO)) {
                    bookRoomAppointmentRecordVo.setUserName(userDTO.getName());
                    bookRoomAppointmentRecordVo.setDeptName(
                            Optional.ofNullable(userDTO.getOrganization()).orElse(new UserDTO.Organization()).getName());
                }
            }
        }
        return appointmentRecordList;
    }

    private Pagination<MobileAppointmentVo> getConference(Page<MobileAppointmentVo> todayConference) {
        if (CollectionUtils.isNotEmpty(todayConference.getRecords())) {
            List<MobileAppointmentVo> records = todayConference.getRecords();
            List<Long> collect = records.stream().map(MobileAppointmentVo::getConferenceId).collect(Collectors.toList());
            List<ConferenceDevicesVo> conferenceDevice = conferenceMapper.getConferenceDevice(collect);
            Map<Long, ConferenceDevicesVo> collect1 = conferenceDevice.stream().collect(
                    Collectors.toMap(ConferenceDevicesVo::getConferenceId, s -> s));
            records.forEach(s -> {
                ConferenceDevicesVo conferenceDeviceVo = collect1.get(s.getConferenceId());
                if (Objects.nonNull(conferenceDeviceVo)) {
                    s.setDeviceName(conferenceDeviceVo.getName());
                }

            });
        }
        return PagingUtils.convert(todayConference, MobileAppointmentVo.class);

    }

    /**
     * @Description 消息发送
     * @Author lrj
     */
    private void sendMsg(Set<Long> userIds, String msgSceneCode, Map<String, String> tmpParams) {
        MessageSendClient sendClient = ApplicationContextHolder.getBean(MessageSendClient.class);
        ApplicationDTO application = applicationClient.getApplicationByCode("MEETING_ROOM_MANAGEM");
        //发送人
        List<MessageNotifyTarget> messageNotifyTargets = new ArrayList<>();
        MessageNotifyTarget messageNotifyTarget = new MessageNotifyTarget();
        messageNotifyTarget.setNotifyTargetType(MessageNotifyTargetEnum.USER);
        messageNotifyTarget.setIds(userIds);
        messageNotifyTargets.add(messageNotifyTarget);
        MessageSpecifyTmpSendRequest message = MessageSpecifyTmpSendRequest.builder()
                .tenant(SessionContextHolder.getRequester()
                                .getTenant())
                .applicationCode(application == null ? ""
                                                     : application.getCode())
                .applicationName(application == null ? ""
                                                     : application.getName())
                .msgSceneCode(msgSceneCode)
                .tmpParams(tmpParams)
                .messageNotifyTargets(messageNotifyTargets)
                .build();
        MessageSendResponse messageSendResponse = null;
        try {
            messageSendResponse = sendClient.sceneSpecifyTmpSend(message);
            log.info("消息发送结果,{}", JSON.toJSONString(messageSendResponse));
        } catch (Exception e) {
            log.error("消息发送失败:{}", e.getMessage());
        }
    }

}
