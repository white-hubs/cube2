package com.guhecloud.cube.srv.gen.third.connection.server.huawei.attendace.service;

import com.guhecloud.cube.base.dictionary.adapter.DataDictionaryAdapter;
import com.guhecloud.cube.base.dictionary.param.DataDictionaryaramByItemAndCodeParam;
import com.guhecloud.cube.srv.gen.third.connection.server.huawei.attendace.config.HrDatabaseConfig;
import com.guhecloud.cube.srv.gen.third.connection.server.huawei.attendace.dto.UserAttendanceStatus;
import com.guhecloud.cube.srv.gen.third.connection.server.huawei.attendace.entity.HrLeave;
import com.guhecloud.cube.srv.gen.third.connection.server.huawei.attendace.entity.HrShift;
import com.guhecloud.cube.srv.gen.third.connection.server.huawei.attendace.handler.ResultSetHandler;
import com.guhecloud.cube.srv.gen.third.connection.server.huawei.attendace.util.HrJdbcUtil;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.base.util.ListUtils;
import com.guhecloud.platform.base.util.MapUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 考勤服务类
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final DataDictionaryAdapter dataDictionaryAdapter;

    /**
     * 字典项编码
     */
    private static final String DICTIONARY_ITEM_CODE = "HR_DATABASE_CONFIG";

    /**
     * 从字典获取数据库配置（每次调用都获取最新配置）
     * variable 字段包含 JSON 字符串，包含所有数据库配置
     */
    private HrDatabaseConfig getHrDatabaseConfig() {
        log.debug("开始从字典获取HR数据库配置，字典项编码: {}", DICTIONARY_ITEM_CODE);

        try {
            var param = new DataDictionaryaramByItemAndCodeParam();
            param.setItemCode(DICTIONARY_ITEM_CODE);
            param.setCode(DICTIONARY_ITEM_CODE);
            var dictionaryDTO = dataDictionaryAdapter.getDataDictionaryByItemAndCode(param);

            // 使用 Assert 检查字典是否存在
            Assert.isNotFound(dictionaryDTO == null,
                              String.format("未找到HR数据库配置字典，字典项编码: %s", DICTIONARY_ITEM_CODE));

            // 检查 variable 字段是否存在
            var variableJson = dictionaryDTO.getVariable();
            Assert.isNotFound(StringUtils.isBlank(variableJson),
                              String.format("HR数据库配置字典的 variable 字段为空，字典项编码: %s", DICTIONARY_ITEM_CODE));

            log.debug("成功获取字典配置，开始解析JSON");

            // 解析 JSON 字符串为 HrDatabaseConfig 对象
            var config = JsonUtils.deserialize(variableJson, HrDatabaseConfig.class);

            // 验证配置对象是否为空
            Assert.isNotFound(config == null, "解析HR数据库配置JSON失败，配置对象为空");

            // 验证必填字段
            Assert.isNotFound(StringUtils.isBlank(config.getHost()),
                              "HR数据库配置中 host 字段不能为空");
            Assert.isNotFound(StringUtils.isBlank(config.getPort()),
                              "HR数据库配置中 port 字段不能为空");
            Assert.isNotFound(StringUtils.isBlank(config.getDatabase()),
                              "HR数据库配置中 database 字段不能为空");
            Assert.isNotFound(StringUtils.isBlank(config.getUsername()),
                              "HR数据库配置中 username 字段不能为空");
            Assert.isNotFound(StringUtils.isBlank(config.getPassword()),
                              "HR数据库配置中 password 字段不能为空");
            Assert.isNotFound(StringUtils.isBlank(config.getDriverClass()),
                              "HR数据库配置中 driverClass 字段不能为空");

            log.debug("成功获取并解析HR数据库配置，host: {}, port: {}, database: {}",
                      config.getHost(), config.getPort(), config.getDatabase());

            return config;

        } catch (Exception e) {
            log.error("获取HR数据库配置失败，字典项编码: {}", DICTIONARY_ITEM_CODE, e);
            Assert.isNotFound(true,
                              String.format("获取HR数据库配置失败: %s", e.getMessage()));
            return null; // 不会执行到这里，因为上面会抛出异常
        }
    }

    /**
     * 查询排班信息
     * 视图 hrshift 只提供当天以及前一天的排班数据，直接查询全部排班数据
     */
    public List<HrShift> queryShifts() {
        var sql = "SELECT A0190, CellPhone, duty_date, shiftno, workbegin, workend, shift_name " +
                  "FROM hrshift " +
                  "ORDER BY A0190, shiftno";

        var hrDatabaseConfig = getHrDatabaseConfig();
        return HrJdbcUtil.executeQuery(hrDatabaseConfig, sql, null, new ResultSetHandler<List<HrShift>>() {
            @Override
            public List<HrShift> handle(ResultSet rs) throws SQLException {
                var list = new ArrayList<HrShift>();
                while (rs.next()) {
                    var shift = new HrShift();
                    shift.setA0190(rs.getString("A0190"));
                    shift.setCellPhone(rs.getString("CellPhone"));
                    var dutyDate = rs.getTimestamp("duty_date");
                    if (dutyDate != null) {
                        shift.setDutyDate(dutyDate.toLocalDateTime());
                    }
                    shift.setShiftno(rs.getString("shiftno"));
                    var workbegin = rs.getTimestamp("workbegin");
                    if (workbegin != null) {
                        shift.setWorkbegin(workbegin.toLocalDateTime());
                    }
                    var workend = rs.getTimestamp("workend");
                    if (workend != null) {
                        shift.setWorkend(workend.toLocalDateTime());
                    }
                    shift.setShiftName(rs.getString("shift_name"));
                    list.add(shift);
                }
                return list;
            }
        });
    }

    /**
     * 查询请假信息
     * 视图 hrleave 提供所有请假单据数据，不限日期范围
     */
    public List<HrLeave> queryLeaves(String employeeNo, LocalDateTime checkTime) {
        var sql = "SELECT A0190, CellPhone, leave_type, begintime, endtime " +
                  "FROM hrleave " +
                  "WHERE A0190 = ? AND begintime <= ? AND endtime >= ?";

        var params = new Object[]{ employeeNo, Timestamp.valueOf(checkTime), Timestamp.valueOf(checkTime) };

        var hrDatabaseConfig = getHrDatabaseConfig();
        return HrJdbcUtil.executeQuery(hrDatabaseConfig, sql, params, new ResultSetHandler<List<HrLeave>>() {
            @Override
            public List<HrLeave> handle(ResultSet rs) throws SQLException {
                var list = new ArrayList<HrLeave>();
                while (rs.next()) {
                    var leave = new HrLeave();
                    leave.setA0190(rs.getString("A0190"));
                    leave.setCellPhone(rs.getString("CellPhone"));
                    leave.setLeaveType(rs.getString("leave_type"));
                    var begintime = rs.getTimestamp("begintime");
                    if (begintime != null) {
                        leave.setBegintime(begintime.toLocalDateTime());
                    }
                    var endtime = rs.getTimestamp("endtime");
                    if (endtime != null) {
                        leave.setEndtime(endtime.toLocalDateTime());
                    }
                    list.add(leave);
                }
                return list;
            }
        });
    }

    /**
     * 根据手机号列表批量查询请假信息
     * 视图 hrleave 提供所有请假单据数据，不限日期范围
     *
     * @param cellPhones 手机号列表
     * @param checkTime  检查时间
     *
     * @return 请假信息列表
     */
    public List<HrLeave> queryLeavesByCellPhones(List<String> cellPhones, LocalDateTime checkTime) {
        if (ListUtils.isEmpty(cellPhones)) {
            return new ArrayList<>();
        }

        // 构建 IN 查询的占位符
        var placeholders = new StringBuilder();
        for (var i = 0; i < cellPhones.size(); i++) {
            if (i > 0) {
                placeholders.append(",");
            }
            placeholders.append("?");
        }

        var sql = "SELECT A0190, CellPhone, leave_type, begintime, endtime " +
                  "FROM hrleave " +
                  "WHERE CellPhone IN (" + placeholders + ") AND begintime <= ? AND endtime >= ?";

        var params = new Object[cellPhones.size() + 2];
        for (var i = 0; i < cellPhones.size(); i++) {
            params[i] = cellPhones.get(i);
        }
        params[cellPhones.size()] = Timestamp.valueOf(checkTime);
        params[cellPhones.size() + 1] = Timestamp.valueOf(checkTime);

        var hrDatabaseConfig = getHrDatabaseConfig();
        return HrJdbcUtil.executeQuery(hrDatabaseConfig, sql, params, new ResultSetHandler<List<HrLeave>>() {
            @Override
            public List<HrLeave> handle(ResultSet rs) throws SQLException {
                var list = new ArrayList<HrLeave>();
                while (rs.next()) {
                    var leave = new HrLeave();
                    leave.setA0190(rs.getString("A0190"));
                    leave.setCellPhone(rs.getString("CellPhone"));
                    leave.setLeaveType(rs.getString("leave_type"));
                    var begintime = rs.getTimestamp("begintime");
                    if (begintime != null) {
                        leave.setBegintime(begintime.toLocalDateTime());
                    }
                    var endtime = rs.getTimestamp("endtime");
                    if (endtime != null) {
                        leave.setEndtime(endtime.toLocalDateTime());
                    }
                    list.add(leave);
                }
                return list;
            }
        });
    }

    /**
     * 批量检查用户是否在岗
     * 1. 查询所有排班数据
     * 2. 根据手机号对排班数据进行分组
     * 3. 根据手机号批量查询请假数据
     * 4. 遍历每个用户的排班数据，判断当前时间是否有排班或请假，判断是否在岗
     *
     * @param checkTime 检查时间
     *
     * @return 用户考勤状态列表（每个员工一个状态）
     */
    public List<UserAttendanceStatus> batchCheckUsersOnDuty(LocalDateTime checkTime) {
        var result = new ArrayList<UserAttendanceStatus>();

        // 1. 查询所有排班信息
        var shifts = queryShifts();
        if (ListUtils.isEmpty(shifts)) {
            log.warn("未查询到排班信息，检查时间: {}", checkTime);
            return result;
        }

        // 2. 根据手机号对排班数据进行分组
        var shiftsByCellPhone = MapUtils.listGroupToMap(shifts, HrShift::getCellPhone);

        // 3. 获取所有手机号
        var cellPhones = new ArrayList<String>(shiftsByCellPhone.keySet());
        if (ListUtils.isEmpty(cellPhones)) {
            log.warn("未获取到手机号信息");
            return result;
        }

        // 4. 根据手机号批量查询请假数据
        var allLeaves = queryLeavesByCellPhones(cellPhones, checkTime);

        // 5. 按手机号分组请假数据
        var leavesByCellPhone = MapUtils.listGroupToMap(allLeaves, HrLeave::getCellPhone);

        // 6. 遍历每个用户的排班数据，判断是否在岗
        for (var entry : shiftsByCellPhone.entrySet()) {
            var cellPhone = entry.getKey();
            var userShifts = entry.getValue();

            if (ListUtils.isEmpty(userShifts)) {
                continue;
            }

            // 获取员工工号（从第一个排班记录中获取）
            var firstShift = userShifts.get(0);
            var employeeNo = firstShift != null ? firstShift.getA0190() : null;

            if (employeeNo == null) {
                log.warn("排班记录中员工工号为空，手机号: {}", cellPhone);
                continue;
            }

            var status = new UserAttendanceStatus();
            status.setEmployeeNo(employeeNo);
            status.setCellPhone(cellPhone);
            status.setIsOnDuty(false);
            status.setStatusDesc("未在岗");

            // 检查当前时间是否在任何一个排班时间内，并检查班次名称是否为"休"
            var isInShiftTime = false;
            var isRestShift = false;
            for (var shift : userShifts) {
                if (shift.getWorkbegin() != null && shift.getWorkend() != null) {
                    if (!checkTime.isBefore(shift.getWorkbegin()) && !checkTime.isAfter(shift.getWorkend())) {
                        isInShiftTime = true;
                        // 检查班次名称是否为"休"
                        if ("休".equals(shift.getShiftName())) {
                            isRestShift = true;
                        }
                        break;
                    }
                }
            }

            if (!isInShiftTime) {
                status.setStatusDesc("不在排班时间内");
                result.add(status);
                continue;
            }

            // 如果班次名称为"休"，则判定为休息状态
            if (isRestShift) {
                status.setIsOnDuty(false);
                status.setStatusDesc("休息");
                result.add(status);
                continue;
            }

            // 检查是否在请假时间内
            var leaves = leavesByCellPhone.get(cellPhone);
            if (ListUtils.isNotEmpty(leaves)) {
                var isOnLeave = false;
                for (var leave : leaves) {
                    if (leave.getBegintime() != null && leave.getEndtime() != null) {
                        if (!checkTime.isBefore(leave.getBegintime()) && !checkTime.isAfter(leave.getEndtime())) {
                            isOnLeave = true;
                            break;
                        }
                    }
                }
                if (isOnLeave) {
                    status.setStatusDesc("在请假期间");
                    result.add(status);
                    continue;
                }
            }

            // 在排班时间内且未请假，判定为在岗
            status.setIsOnDuty(true);
            status.setStatusDesc("在岗");
            result.add(status);
        }

        return result;
    }
}
