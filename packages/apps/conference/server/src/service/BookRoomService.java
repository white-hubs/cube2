package com.guhecloud.cube.srv.gen.conferencemanage.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.conferencemanage.server.entity.BookRoom;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.AppointmentParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.param.BookRoomParam;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.AppointmentRecordVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.AppointmentVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.BookRoomAppVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.BookRoomAppointmentVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.BookRoomVo;
import com.guhecloud.cube.srv.gen.conferencemanage.server.vo.MobileAppointmentVo;
import com.guhecloud.platform.base.model.Pagination;

import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

public interface BookRoomService extends IService<BookRoom> {
    void addBookRoom(BookRoomParam bookRoomParam);

    void cancelBookRoom(Long bookId, Boolean isCancel, String cancelReason);

    /**
     * 更新预约记录
     *
     * @param processInstanceId
     */
    void updateBookRoom(String processInstanceId);

    Pagination<AppointmentVo> getAppointmentVos(AppointmentParam param);

    Pagination<MobileAppointmentVo> getAppAppointmentVos(AppointmentParam param);

    Pagination<MobileAppointmentVo> getNewAppAppointmentVos(AppointmentParam param);

    MobileAppointmentVo getAppAppointmentDetailVos(AppointmentParam param);

    Pagination<AppointmentRecordVo> appointmentPage(AppointmentParam param);

    BookRoomVo getBookRoomVo(Long bookId);

    Map<String, Object> getAppStatistics(String timeFlag);

    Pagination<BookRoomAppVo> myAppointmentPage(AppointmentParam param);

    BookRoomAppointmentVo bookRoomAppointmentInfo(AppointmentParam param);

    /**
     * pc预约记录导出
     *
     * @param param
     */
    void appointmentExportForPC(AppointmentParam param, HttpServletResponse response);

}
