package com.guhecloud.cube.srv.gen.conferencemanage.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.client.UserClient;
import com.guhecloud.cube.biz.ouaa.client.dto.UserDTO;
import com.guhecloud.cube.srv.gen.conferencemanage.server.dao.AppointmentRecordMapper;
import com.guhecloud.cube.srv.gen.conferencemanage.server.dao.BookIntervalRecordMapper;
import com.guhecloud.cube.srv.gen.conferencemanage.server.dao.BookRoomMapper;
import com.guhecloud.cube.srv.gen.conferencemanage.server.dao.ConferenceMapper;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.BookIntervalRecord;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.BookRoom;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.BookTimeInterval;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.Conference;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.ConferenceDevice;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.ConferenceRoomType;
import com.guhecloud.cube.srv.gen.conferencemanage.server.enumeration.BookRoomEnum;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.AppointmentParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceAddOrUpdateParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceDeviceAddOrUpdateParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceQueryParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.ConferenceUsageParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.BookTimeIntervalService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.ConferenceDeviceService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.ConferenceRoomTypeService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.ConferenceService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.util.DateUnitUtils;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.AppointmentIdleVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.AppointmentIntervalLogVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.AppointmentUsageVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.AppointmentVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.ConferenceBaseEntityVO;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.ConferenceInfoVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.ConferenceUsedRatioVO;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.DevicesVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.model.Period;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.DateUtils;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.MathUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.platform.security.context.SecurityContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ConferenceServiceImpl extends ServiceImpl<ConferenceMapper, Conference> implements ConferenceService {

    private final ConferenceMapper conferenceMapper;
    private final BookTimeIntervalService bookTimeIntervalService;
    private final AppointmentRecordMapper appointmentRecordMapper;
    private final BookIntervalRecordMapper bookIntervalRecordMapper;
    private final ConferenceDeviceService conferenceDeviceService;
    private final BookRoomMapper bookRoomMapper;
    private final UserClient userClient;
    private final ConferenceRoomTypeService conferenceRoomTypeService;

    @Override
    @Transactional
    public void insertConference(ConferenceAddOrUpdateParam param) {
        // 预约时间段必输
        Assert.isParameterError(StringUtils.isBlank(param.getPeriod()), "可预约时间段不能为空");
        long id = IdWorker.getId();
        Conference conference = ObjectUtils.copying(param, Conference.class);
        conference.setId(id);
        Set<String> conferenceNameSet = conferenceMapper.selectList(new QueryWrapper<>()).stream().map(Conference::getName)
                .collect(Collectors.toSet());
        Assert.isConflict(conferenceNameSet.contains(param.getName()), "会议名称重复，请重新输入");
        this.save(conference);
        if (StringUtils.isNotBlank(param.getPeriod())) {
            List<BookTimeInterval> bookTimeIntervals = new ArrayList<>();
            List<String> strings = periodSplitting(param.getPeriod());
            for (String s : strings) {
                BookTimeInterval bookTimeInterval = new BookTimeInterval();
                bookTimeInterval.setTimeInterval(s);
                bookTimeInterval.setConferenceId(id);
                bookTimeInterval.setBookStatus(BookRoomEnum.RESERVABLE);
                bookTimeIntervals.add(bookTimeInterval);
            }
            bookTimeIntervalService.saveBatch(bookTimeIntervals);
        }
        List<ConferenceDeviceAddOrUpdateParam> devices = param.getDevices();
        List<ConferenceDevice> copying = ObjectUtils.copying(devices, ConferenceDevice.class);
        copying.forEach(s -> {
            s.setConferenceId(id);
        });
        conferenceDeviceService.saveBatch(copying);
    }

    @Override
    @Transactional
    public void updateConference(ConferenceAddOrUpdateParam param) {
        Conference conference = conferenceMapper.selectById(param.getId());
        ObjectUtils.copying(param, conference);
        log.info("要修改的conference如下:{}", JsonUtils.serialize(conference));
        conferenceMapper.updateById(conference);
        if (StringUtils.isNotBlank(param.getPeriod())) {
            List<BookTimeInterval> bookTimeIntervals = new ArrayList<>();
            List<String> strings = periodSplitting(param.getPeriod());
            for (String s : strings) {
                BookTimeInterval bookTimeInterval = new BookTimeInterval();
                bookTimeInterval.setTimeInterval(s);
                bookTimeInterval.setConferenceId(conference.getId());
                bookTimeInterval.setBookStatus(BookRoomEnum.RESERVABLE);
                bookTimeIntervals.add(bookTimeInterval);
            }
            bookTimeIntervalService.remove(Wrappers.<BookTimeInterval>lambdaQuery()
                                                   .eq(BookTimeInterval::getConferenceId, conference.getId()));
            bookTimeIntervalService.saveBatch(bookTimeIntervals);
        }

        //设备
        conferenceDeviceService.remove(Wrappers.<ConferenceDevice>lambdaQuery()
                                               .eq(ConferenceDevice::getConferenceId, conference.getId()));
        List<ConferenceDeviceAddOrUpdateParam> devices = param.getDevices();
        devices.forEach(d -> {
            d.setConferenceId(param.getId());
        });
        List<ConferenceDevice> copying = ObjectUtils.copying(devices, ConferenceDevice.class);
        conferenceDeviceService.saveBatch(copying);

    }

    @Override
    public Pagination<ConferenceInfoVo> selectConferenceInfo(ConferenceQueryParam param) {
        Page<Conference> page = this.page(PagingUtils.convert(param), Wrappers.<Conference>lambdaQuery()
                .like(StringUtils.isNotBlank(param.getName()),
                      Conference::getName, param.getName())
                .like(StringUtils.isNotBlank(param.getAreaName()),
                      Conference::getAreaName, param.getAreaName())
                .orderByDesc(Conference::getCreateTime));
        Pagination<ConferenceInfoVo> convert = PagingUtils.convert(page, ConferenceInfoVo.class);
        Set<Long> administrators = new HashSet<>();
        if (CollectionUtils.isNotEmpty(convert.getRecords())) {
            convert.getRecords().forEach(r -> {
                List<ConferenceDevice> conferenceDeviceList = conferenceDeviceService.list(
                        new QueryWrapper<ConferenceDevice>().eq("conference_id", r.getId()));
                if (!conferenceDeviceList.isEmpty()) {
                    List<DevicesVo> devicesVoList = conferenceDeviceList.stream().map(c -> {
                        DevicesVo devicesVo = new DevicesVo();
                        devicesVo.setConferenceId(r.getId());
                        if (StringUtils.isNotBlank(c.getId().toString())) {
                            devicesVo.setId(c.getId());
                        }
                        if (StringUtils.isNotBlank(c.getDeviceId().toString())) {
                            devicesVo.setDeviceId(c.getDeviceId());
                        }
                        if (StringUtils.isNotBlank(c.getName())) {
                            devicesVo.setName(c.getName());
                        }
                        if (StringUtils.isNotBlank(c.getDeviceCode())) {
                            devicesVo.setDeviceCode(c.getDeviceCode());
                        }
                        if (StringUtils.isNotBlank(c.getDeviceModel())) {
                            devicesVo.setDeviceModel(c.getDeviceModel());
                        }
                        if (StringUtils.isNotBlank(c.getType())) {
                            devicesVo.setType(c.getType());
                        }

                        return devicesVo;
                    }).collect(Collectors.toList());
                    r.setDevicesVos(devicesVoList);
                }
                if (StringUtils.isNotBlank(r.getAdministrators())) {
                    administrators.addAll(Arrays.stream(r.getAdministrators().split(","))
                                                  .map(Long::valueOf).toList());
                }

            });
        }
        Map<Long, String> userMap = new HashMap<>();
        if (CollectionUtils.isNotEmpty(administrators)) {
            List<UserDTO> userDTOS = userClient.listUserByIds(administrators);
            if (CollectionUtils.isNotEmpty(userDTOS)) {
                userMap = userDTOS.stream().collect(Collectors.toMap(UserDTO::getId, UserDTO::getName));
            }
        }
        if (CollectionUtils.isNotEmpty(convert.getRecords()) && MapUtils.isNotEmpty(userMap)) {
            for (ConferenceInfoVo record : convert.getRecords()) {
                if (StringUtils.isNotBlank(record.getAdministrators())) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (String s : record.getAdministrators().split(",")) {
                        if (StringUtils.isBlank(userMap.get(Long.valueOf(s)))) {
                            continue;
                        }
                        if (StringUtils.isNotBlank(stringBuilder.toString())) {
                            stringBuilder.append("、");
                        }
                        stringBuilder.append(userMap.get(Long.valueOf(s)));
                    }
                    record.setAdministratorsName(stringBuilder.toString());
                }
            }
        }
        return convert;
    }

    @Override
    public ConferenceInfoVo getConferenceDetailInfo(Long id) {
        Conference byId = this.getById(id);
        ConferenceInfoVo conferenceInfoVo = ObjectUtils.copying(byId, ConferenceInfoVo.class);
        if (Objects.nonNull(conferenceInfoVo.getConferenceRoomTypeId())) {
            ConferenceRoomType conferenceRoomType = conferenceRoomTypeService.getById(conferenceInfoVo.getConferenceRoomTypeId());
            conferenceInfoVo.setConferenceRoomTypeName(conferenceRoomType != null ? conferenceRoomType.getName() : null);
        }
        if (Objects.nonNull(conferenceInfoVo.getBookPersonType()) && StringUtils.isNotBlank(byId.getBookPerson())) {
            List<Long> longList = Arrays.stream(byId.getBookPerson().split(","))
                    .map(Long::valueOf).collect(Collectors.toList());
            List<ConferenceBaseEntityVO> personInfo = conferenceInfoVo.getBookPersonType().getPersonInfo(longList);
            conferenceInfoVo.setBookPersonList(personInfo);
        }
        List<ConferenceDevice> list = conferenceDeviceService.list(Wrappers.<ConferenceDevice>lambdaQuery()
                                                                           .eq(ConferenceDevice::getConferenceId, id));
        if (CollectionUtils.isNotEmpty(list)) {
            List<DevicesVo> copying = ObjectUtils.copying(list, DevicesVo.class);
            conferenceInfoVo.setDevicesVos(copying);
        }
        return conferenceInfoVo;
    }

    @Override
    public Pagination<AppointmentVo> getAppointmentVo(AppointmentParam param) {
        buildFilterParam(param);
        Page<AppointmentVo> appointmentVo = conferenceMapper.getAppointmentVo(PagingUtils.convert(param), param);
        if (!appointmentVo.getRecords().isEmpty()) {
            return PagingUtils.convert(appointmentVo);
        } else {
            return new Pagination<>();
        }

    }

    @Override
    public void buildFilterParam(AppointmentParam param) {
        Long userId = SecurityContextHolder.getPrincipal().getId();
        UserDTO user = userClient.getUser(userId);
        if (user != null) {
            List<String> organizationIds = user.getOrganizations().stream().map(t -> String.valueOf(t.getId()))
                    .collect(Collectors.toList());
            List<String> roleIds = user.getRoles().stream().map(t -> String.valueOf(t.getId())).collect(Collectors.toList());
            param.setUserId(userId.toString());
            param.setOrganizationIdList(organizationIds);
            param.setRoleIdList(roleIds);
        }
    }

    /**
     * @Description 时段拆分
     * @Author lrj
     */
    private List<String> periodSplitting(String period) {
        String[] split = period.split(",");
        String begin = split[0];
        String end = split[1];
        boolean bool = false;
        if ("23:59".equals(end)) {
            end = "00:00";
            bool = true;
        }

        LocalTime parse = LocalTime.parse(begin);
        int minute = parse.getMinute();
        int hour = parse.getHour();
        LocalTime beginTime;
        if (minute < 30) {
            beginTime = LocalTime.of(hour, 0);
        } else {
            beginTime = LocalTime.of(hour, 30);
        }
        LocalTime parse1 = LocalTime.parse(end);
        long between = 0;
        if (bool) {
            between = ChronoUnit.MINUTES.between(parse, LocalTime.of(23, 59)) + 1;
        } else {
            between = ChronoUnit.MINUTES.between(parse, parse1);
        }
        int num = (int) between / 30;
        List<String> periods = new ArrayList<>();
        for (int i = 1; i <= num; i++) {
            String s = beginTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            beginTime = beginTime.plusMinutes(30);
            String s1 = beginTime.format(DateTimeFormatter.ofPattern("HH:mm"));
            periods.add(s + "-" + s1);
        }
        return periods;
    }

    @Override
    public List<AppointmentUsageVo> selectConferenceUsage(ConferenceUsageParam param) {
        String startTime = null;
        LocalDateTime date = DateUtils.now();
        String endTime = DateUtils.format(DateUtils.now(), "yyyy-MM-dd HH:mm:ss");
        if (param != null) {
            if (param.getTimeFlag().equals("THIS_MONTH")) {
                startTime = DateUtils.beginOfMonth(date).toString();
            } else if (param.getTimeFlag().equals("THIS_WEEK")) {
                startTime = DateUtils.beginOfWeek(date).toString();
            } else if (param.getTimeFlag().equals("TODAY")) {
                startTime = DateUtils.beginOfDay(date).toString();
            } else if (param.getTimeFlag().equals("THIS_YEAR")) {
                startTime = DateUtils.beginOfYear(date).toString();
            } else if (StringUtils.isNotBlank(param.getPeriod())) {
                String[] split = param.getPeriod().split(",");
                startTime = split[0];
                endTime = split[1];
            }
        }
        return appointmentRecordMapper.selectConferenceUsage(param, startTime, endTime);

    }

    @Override
    public AppointmentUsageVo selectConferenceCount(ConferenceUsageParam param) {
        String startTime = null;
        LocalDateTime date = DateUtils.now();
        String endTime = DateUtils.format(DateUtils.now(), "yyyy-MM-dd HH:mm:ss");
        if (param != null) {
            if (param.getTimeFlag().equals("THIS_MONTH")) {
                startTime = DateUtils.beginOfMonth(date).toString();
            } else if (param.getTimeFlag().equals("THIS_WEEK")) {
                startTime = DateUtils.beginOfWeek(date).toString();
            } else if (param.getTimeFlag().equals("TODAY")) {
                startTime = DateUtils.beginOfDay(date).toString();
            } else if (param.getTimeFlag().equals("THIS_YEAR")) {
                startTime = DateUtils.beginOfYear(date).toString();
            } else if (StringUtils.isNotBlank(param.getPeriod())) {
                String[] split = param.getPeriod().split(",");
                startTime = split[0];
                endTime = split[1];
            }
        }
        return appointmentRecordMapper.selectConferenceCount(param, startTime, endTime);
    }

    @Override
    public List<AppointmentIdleVo> selectConferenceIdle(ConferenceUsageParam param) {

        List<Conference> conferences = conferenceMapper.selectList(new QueryWrapper<Conference>().eq("status", 0));
        List<AppointmentIdleVo> appointmentIdleVoList = null;
        if (!conferences.isEmpty()) {
            appointmentIdleVoList = conferences.stream().map(c -> {
                AppointmentIdleVo appointmentIdleVo = new AppointmentIdleVo();
                appointmentIdleVo.setConferenceName(c.getName());
                if (param != null) {
                    LocalDateTime startTime = DateUtils.now();
                    LocalDateTime endTime = DateUtils.now();
                    int day = 1;
                    if (StringUtils.isNotBlank(param.getPeriod()) && !param.getPeriod().isEmpty()) {
                        String[] split = param.getPeriod().split(",");
                        startTime = DateUtils.parse(split[0], "yyyy-MM-dd HH:mm:ss");
                        endTime = DateUtils.parse(split[1], "yyyy-MM-dd HH:mm:ss");
                        if (StringUtils.isNotBlank(param.getTimeFlag())) {
                            if (!DateUtils.parse(split[0], "yyyy-MM-dd").isEqual(DateUtils.parse(split[1], "yyyy-MM-dd"))) {
                                day = (int) ((DateUtils.toTimestamp(endTime) - DateUtils.toTimestamp(startTime)) / (1000 * 3600 * 24));
                            }
                        }
                    }
                    //预约但未被取消的总数
                    int bookSumCount = bookIntervalRecordMapper.getBookCount(c.getId(),
                                                                             DateUtils.format(startTime, "yyyy-MM-dd HH:mm:ss"),
                                                                             DateUtils.format(endTime, "yyyy-MM-dd HH:mm:ss"));
                    //一天可预约总数
                    int sumCount = bookIntervalRecordMapper.getCount(c.getId());
                    appointmentIdleVo.setIdleRate(new BigDecimal(0));
                    if (sumCount > 0) {
                        appointmentIdleVo.setIdleRate(new BigDecimal(bookSumCount).multiply(new BigDecimal("100"))
                                                              .divide(new BigDecimal(sumCount * day), 2,
                                                                      RoundingMode.HALF_UP));
                    }
                }
                return appointmentIdleVo;

            }).collect(Collectors.toList());
        }

        return appointmentIdleVoList;
    }

    @Override
    public List<AppointmentIntervalLogVo> selectTimeIntervalLog(ConferenceUsageParam param) {
        String startTime = null;
        String endTime = null;
        if (StringUtils.isNotBlank(param.getPeriod())) {
            String[] split = param.getPeriod().split(",");
            startTime = split[0];
            endTime = split[1];
        }
        return appointmentRecordMapper.selectTimeIntervalLog(param, startTime, endTime);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteConference(Long id) {
        List<BookIntervalRecord> bookIntervalRecordList = bookIntervalRecordMapper.selectList(
                Wrappers.<BookIntervalRecord>lambdaQuery()
                        .eq(BookIntervalRecord::getConferenceId, id)
                        .ge(BookIntervalRecord::getAppointmentDate, DateUtils.beginOfDay(DateUtils.now())));
        if (!bookIntervalRecordList.isEmpty()) {
            bookIntervalRecordList
                    .forEach(b -> {
                        List<BookRoom> bookRoomList = bookRoomMapper.selectList(
                                new QueryWrapper<BookRoom>().eq("id", b.getBookId())
                                        .ne("status", "CANCEL_RESERVATION"));
                        Assert.isNotAcceptable(!bookRoomList.isEmpty(), "该会议室有预约，不能删除");
                    });
        }
        DatabaseContextHolder.softDelete(this, id);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateConferenceStatus(Long id, Integer status) {
        Conference conference = conferenceMapper.selectById(id);
        conference.setStatus(status);
        conferenceMapper.updateById(conference);
    }

    @Override
    public ConferenceInfoVo getConferenceDetail(Long id) {
        Conference conference = conferenceMapper.selectById(id);
        ConferenceInfoVo conferenceInfoVo = new ConferenceInfoVo();
        if (conference != null) {
            ObjectUtils.copying(conference, conferenceInfoVo);
        }
        List<ConferenceDevice> conferenceDeviceList = conferenceDeviceService.list(
                new QueryWrapper<ConferenceDevice>().eq("conference_id", id));
        if (!conferenceDeviceList.isEmpty()) {
            List<DevicesVo> devicesVoList = conferenceDeviceList.stream().map(c -> {
                DevicesVo devicesVo = new DevicesVo();
                devicesVo.setConferenceId(id);
                if (StringUtils.isNotBlank(c.getId().toString())) {
                    devicesVo.setId(c.getId());
                }
                if (StringUtils.isNotBlank(c.getDeviceId().toString())) {
                    devicesVo.setDeviceId(c.getDeviceId());
                }
                if (StringUtils.isNotBlank(c.getName())) {
                    devicesVo.setName(c.getName());
                }
                if (StringUtils.isNotBlank(c.getDeviceCode())) {
                    devicesVo.setDeviceCode(c.getDeviceCode());
                }
                if (StringUtils.isNotBlank(c.getDeviceModel())) {
                    devicesVo.setDeviceModel(c.getDeviceModel());
                }
                if (StringUtils.isNotBlank(c.getType())) {
                    devicesVo.setType(c.getType());
                }
                return devicesVo;
            }).collect(Collectors.toList());
            conferenceInfoVo.setDevicesVos(devicesVoList);
        }
        return conferenceInfoVo;
    }

    @Override
    public ConferenceUsedRatioVO getConferenceUsedRatio(ConferenceUsageParam param) {
        LocalDateTime now = DateUtils.now();
        LocalDateTime startTime = DateUtils.beginOfDay(now);
        LocalDateTime endTime = DateUtils.endOfDay(now);
        if (StringUtils.isNotBlank(param.getPeriod())) {
            String[] split = param.getPeriod().split(",");
            startTime = DateUtils.parse(split[0]);
            endTime = DateUtils.parse(split[1]);
        }
        int userNum = bookRoomMapper.selectList(
                new QueryWrapper<BookRoom>().select("conference_id")
                        .eq("status", BookRoomEnum.RESERVED.name())
                        .between("end_book_time", startTime, endTime)
                        .groupBy("conference_id")).size();
        Long count = this.lambdaQuery().count();
        //使用率
        float usedRatio = MathUtils.calculateProportion(userNum, count).floatValue();
        //日同比
        Period previousPeriodDay = DateUnitUtils.getPreviousPeriod("TODAY", new Period(startTime, endTime));
        int lastDayUsedNum = bookRoomMapper.selectList(
                new QueryWrapper<BookRoom>().select("conference_id")
                        .eq("status", BookRoomEnum.RESERVED.name())
                        .between("end_book_time", previousPeriodDay.getBegin(), previousPeriodDay.getEnd())
                        .groupBy("conference_id")).size();
        //周同比
        Period previousPeriodMonth = DateUnitUtils.getPreviousPeriod("THIS_WEEK", new Period(startTime, endTime));
        int lastWeekUsedNum = bookRoomMapper.selectList(
                new QueryWrapper<BookRoom>().select("conference_id")
                        .eq("status", BookRoomEnum.RESERVED.name())
                        .between("end_book_time", previousPeriodMonth.getBegin(), previousPeriodMonth.getEnd())
                        .groupBy("conference_id")).size();
        return ConferenceUsedRatioVO.builder()
                .usedRatio(usedRatio)
                .dayOnDay(MathUtils.calculateRingRatio(lastDayUsedNum, userNum).floatValue())
                .weekOnWeek(MathUtils.calculateRingRatio(lastWeekUsedNum, userNum).floatValue())
                .build();
    }

    @Override
    public List<Conference> getConferenceList(String keyword) {
        return this.lambdaQuery()
                .like(StringUtils.isNotBlank(keyword),
                      Conference::getName, keyword)
                .orderByDesc(Conference::getCreateTime).list();
    }
}
