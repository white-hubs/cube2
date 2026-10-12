package com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces;

import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.application.commandgateway.AttStaffClockApplication;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.AddClockInParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.ClockInLogDateParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.ClockInLogPageParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.ClockInMachineParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.param.ClockInParam;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.AttClockInRecordslogResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.ClockInInLedgerByUserResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.ClockInInfoResult;
import com.guhecloud.cube.srv.gen.attendance.management.server.attend.clockin.interfaces.result.ClockInLogResult;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.security.permission.annotation.DataPermissionSwitch;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

/**
 * 打卡信息
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AttStaffClockController {

    private final AttStaffClockApplication attStaffClockService;

    /**
     * pc打卡记录
     *
     * @author: lijie
     * @date: 2023/9/26 16:59
     * @param:
     * @return:
     **/
    @GetMapping("/getClockInLog")
    @DataPermissionSwitch(dataModel = "attendance:clockInRecord")
    public Pagination<ClockInLogResult> getClockInLog(ClockInLogPageParam param) {
        return attStaffClockService.getClockInLog(param);
    }

    /**
     * 某日打卡数据
     *
     * @author: lijie
     * @date: 2023/10/12 15:19
     * @param:
     * @return:
     **/
    @GetMapping("/getClockInLogByDate")
    public List<ClockInLogResult> getClockInLogByDate(ClockInLogDateParam param) {
        return attStaffClockService.getClockInLogByDate(param);
    }

    /**
     * app获取用户打卡信息
     *
     * @author: lijie
     * @date: 2023/9/21 18:59
     * @param:
     * @return:
     **/
    @GetMapping("/app/getClockInInfo")
    public ClockInInfoResult getClockInInfo() {
        return attStaffClockService.getClockInInof();
    }

    /**
     * app打卡
     *
     * @author: lijie
     * @date: 2023/9/21 19:00
     * @param: clockInParam 打卡信息
     * @return: 打卡结果
     **/
    @PostMapping("/app/clockIn")
    public void appClockIn(@RequestBody ClockInParam clockInParam) {
        attStaffClockService.appClockIn(clockInParam);
    }

    /**
     * 考勤机打卡
     *
     * @author: lijie
     * @date: 2023/9/21 19:00
     * @param: 用户信息、打卡时间
     * @return: 打卡结果
     **/
    @PostMapping("/machine/clockIn")
    public Boolean machineClockIn(@RequestBody ClockInMachineParam clockInParam) {
        return attStaffClockService.machineClockIn(clockInParam);
    }

    /**
     * pc增加打卡记录
     *
     * @author: lijie
     * @date: 2023/10/10 15:50
     * @param:
     * @return: 新增结果
     **/
    @PostMapping("/add/clockIn")
    public Boolean addClockIn(@RequestBody AddClockInParam param) {
        return attStaffClockService.addClockIn(param);
    }

    /**
     * pc打卡记录修改
     *
     * @author: lijie
     * @date: 2023/10/10 15:50
     * @param:
     * @return: 修改结果
     **/
    @PutMapping("/update/clockIn/{id}")
    public Boolean updateClockIn(@PathVariable Long id) {
        return attStaffClockService.updateClockIn(id);
    }

    /**
     * app查询打卡记录
     *
     * @author: lijie
     * @date: 2023/9/21 19:00
     * @param: void
     * @return: 用户当日打卡记录
     **/
    @GetMapping("/app/getClockInInfoByUser")
    public AttClockInRecordslogResult getClockInInfoByUser(String clockInDate) {
        return attStaffClockService.getClockInInfoByUser(clockInDate);
    }

    /**
     * app个人月度考勤(日历)
     *
     * @author: lijie
     * @date: 2023/10/13 09:00
     * @param: month 月份(yyyy-MM)
     * @return: 用户当月考勤
     **/
    @GetMapping("/app/clockIn/ledgerByUser")
    public List<ClockInInLedgerByUserResult> getClockInInLedgerByUser(String month) {
        return attStaffClockService.getClockInInLedgerByUser(month);
    }

    /**
     * 打卡日志导出
     *
     * @author: lijie
     * @date: 2023/10/20 9:19
     * @param: void
     * @return: 文件流
     **/
    @GetMapping("/clockIn/getClockInLogExcel")
    public void getClockInLogExcel(HttpServletRequest request, HttpServletResponse response, ClockInLogPageParam param) {
        attStaffClockService.getClockInLogExcel(request, response, param);
    }

    /**
     * 获取打卡服务系统时间
     *
     * @return 系统当前时间
     */
    @GetMapping("/clockIn/getClockServiceSystemTime")
    public Long getClockServiceSystemTime() {
        Instant now = Instant.now();
        return now.toEpochMilli();
    }
}
