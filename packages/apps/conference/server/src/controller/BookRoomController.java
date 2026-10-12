package com.guhecloud.cube.srv.gen.conferencemanage.server.controller;

import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.FieldConfiguration;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.AppointmentParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.BookRoomParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.FieldConfigurationParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.BookRoomService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.service.FieldConfigurationService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.AppointmentRecordVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.AppointmentVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.BookRoomAppVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.BookRoomAppointmentVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.BookRoomVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.FieldConfigurationVO;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.MobileAppointmentVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 会议室预约
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BookRoomController {

    private final BookRoomService bookRoomService;
    private final FieldConfigurationService fieldConfigurationService;

    /**
     * PC预约
     *
     * @param bookRoomParam
     */
    @PostMapping("/book-room")
    public void addBookRoom(@RequestBody BookRoomParam bookRoomParam) {
        bookRoomService.addBookRoom(bookRoomParam);
    }

    /**
     * 预约取消
     *
     * @param bookId
     * @param isCancel
     * @param cancelReason
     */
    @DeleteMapping("/book-room-cancel/{bookId}")
    public void cancelBookRoom(@PathVariable Long bookId, @RequestParam(required = true) Boolean isCancel,
                               @RequestParam(required = false) String cancelReason
                              ) {
        bookRoomService.cancelBookRoom(bookId, isCancel, cancelReason);
    }

    /**
     * PC预约分页
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/appointment")
    public Pagination<AppointmentVo> getAppointmentVos(AppointmentParam param) {
        return bookRoomService.getAppointmentVos(param);
    }

    /**
     * app预约分页
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/app-appointment")
    public Pagination<MobileAppointmentVo> getAppAppointmentVos(AppointmentParam param) {
        return bookRoomService.getNewAppAppointmentVos(param);
    }

    /**
     * app会议室预约
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/app-appointment-detail")
    public MobileAppointmentVo getAppAppointmentDetailVos(AppointmentParam param) {
        return bookRoomService.getAppAppointmentDetailVos(param);
    }

    /**
     * pc预约记录分页
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/book-room/appointmentPage")
    public Pagination<AppointmentRecordVo> appointmentPage(AppointmentParam param) {
        return bookRoomService.appointmentPage(param);
    }

    /**
     * pc预约记录导出
     *
     * @return
     */
    @GetMapping("/book-room/appointment/export")
    public void appointmentExportForPC(AppointmentParam param, HttpServletResponse response) {
        bookRoomService.appointmentExportForPC(param, response);
    }

    /**
     * 预约详情
     *
     * @param bookId
     *
     * @return
     */
    @GetMapping("/bookroom/{bookId}")
    BookRoomVo getBookRoomVo(@PathVariable Long bookId) {
        return bookRoomService.getBookRoomVo(bookId);
    }

    /**
     * app首页统计
     *
     * @param timeFlag
     *
     * @return
     */
    @GetMapping("/statistics/{timeFlag}")
    public Map<String, Object> getAppStatistics(@PathVariable String timeFlag) {
        return bookRoomService.getAppStatistics(timeFlag);
    }

    /**
     * app我的预约列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/book-room/myappointment")
    public Pagination<BookRoomAppVo> myAppointmentPage(AppointmentParam param) {
        return bookRoomService.myAppointmentPage(param);
    }

    /**
     * app会议室预约列表
     *
     * @param appointmentParam
     *
     * @return
     */
    @GetMapping("/book-room/appointmentInfo")
    public BookRoomAppointmentVo bookRoomAppointmentInfo(AppointmentParam appointmentParam) {
        var parameterError = appointmentParam == null ||
                             StringUtils.isEmpty(appointmentParam.getDate()) ||
                             appointmentParam.getConferenceId() == null;
        Assert.isParameterError(parameterError, "会议室信息有误！");
        return bookRoomService.bookRoomAppointmentInfo(appointmentParam);
    }

    /**
     * 获取会议室预约情况（外部调用）
     *
     * @param appointmentParam
     *
     * @return
     */
    @GetMapping("/public/book-room/appointmentInfo")
    public BookRoomAppointmentVo publicBookRoomAppointmentInfo(AppointmentParam appointmentParam) {
        var parameterError = appointmentParam == null ||
                             StringUtils.isEmpty(appointmentParam.getDate()) ||
                             appointmentParam.getConferenceId() == null;
        Assert.isParameterError(parameterError, "会议室信息有误！");
        return bookRoomService.bookRoomAppointmentInfo(appointmentParam);
    }

    /**
     * 必输字段列表
     *
     * @param param
     *
     * @return
     */
    @GetMapping("/required/field/configuration")
    public Pagination<FieldConfigurationVO> requiredFieldConfiguration(FieldConfigurationParam param) {

        return fieldConfigurationService.selectFieldConfigurationList(param);
    }

    /**
     * 必输字段新增或修改
     *
     * @param param
     */
    @PostMapping("/update/required/configuration")
    public void requiredFieldConfiguration(@RequestBody FieldConfiguration param) {
        fieldConfigurationService.saveOrUpdateField(param);
    }
}
