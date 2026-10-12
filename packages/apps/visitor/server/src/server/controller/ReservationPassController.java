package com.guhecloud.cube.srv.gen.visitor.management.server.controller;

import com.guhecloud.cube.srv.gen.visitor.management.server.service.ReservationPassService;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.PassRecordParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.ReservationParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.param.ReservationRecordParam;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.PassRecordResult;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.QoqStatResult;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.ReservationRecordResult;
import com.guhecloud.cube.srv.gen.visitor.management.server.vo.result.ReservationResult;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 预约通行
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ReservationPassController {
    private final ReservationPassService reservationPassService;

    /**
     * 通行记录
     *
     * @param param 入参
     *
     * @return 通行记录
     */
    @GetMapping("/visitor/reservation/passRecordPage")
    public Pagination<PassRecordResult> passRecordPage(PassRecordParam param) {
        return reservationPassService.passRecordPage(param);
    }

    /**
     * 预约
     *
     * @param param 入参
     */
    @PostMapping("/visitor/reservation/reservationAdd")
    public ReservationResult reservation(@RequestBody @Valid ReservationParam param) {
        return reservationPassService.reservation(param);
    }

    /**
     * 预约记录分页列表
     *
     * @param param 入参
     *
     * @return 预约记录
     */
    @GetMapping("/visitor/reservation/reservationRecordPage")
    public Pagination<ReservationRecordResult> reservationRecordPage(ReservationRecordParam param) {
        return reservationPassService.reservationRecordPage(param);
    }

    /**
     * 预约记录详情
     *
     * @param id 主键
     *
     * @return 详情
     */
    @GetMapping("/visitor/reservation/detail")
    public ReservationRecordResult reservationDetail(Long id) {
        return reservationPassService.reservationDetail(id);
    }

    /**
     * 查看预约码
     *
     * @param id id
     *
     * @return 预约码
     */
    @GetMapping("/visitor/reservation/getReservationCode")
    public String getReservationCode(Long id) {
        return reservationPassService.getReservationCode(id);
    }

    /**
     * 取消预约
     *
     * @param param 预约id
     */
    @PostMapping("/visitor/reservation/cancel")
    public void cancelReservation(@RequestBody ReservationRecordParam param) {
        reservationPassService.cancelReservation(param.getId());
    }

    /**
     * 手机号是否预约过
     *
     * @param param 入参
     *
     * @return true表示预约过
     */
    @GetMapping("/visitor/reservation/phoneIsReservation")
    public Boolean phoneIsReservation(ReservationParam param) {
        return reservationPassService.phoneIsReservation(param);
    }

    /**
     * 预约记录环比(日环比+周环比)
     * 游仙区小微产业驾驶舱定制接口
     *
     * @return 环比
     */
    @GetMapping("/visitor/reservation/dayAndWeekRatio")
    public QoqStatResult dayAndWeekRatio() {
        return reservationPassService.dayAndWeekRatio();
    }

    /**
     * 获取访客列表
     */
    /**
     * 获取预约记录的人信息
     */
    @GetMapping("/visitor/reservation/personnel/page")
    Pagination<ReservationRecordResult> reservationPersonnelPage(ReservationRecordParam param) {
        return reservationPassService.reservationPersonnelPage(param);
    }

    /**
     * 获取预约人数
     */
    @GetMapping("/visitor/reservation/count")
    public Long reservationRecordCount(ReservationRecordParam param) {
        return reservationPassService.reservationRecordCount(param);
    }

    /**
     * 查看预约码
     */
    @GetMapping("/visitor/reservation/detail/process")
    public ReservationRecordResult getReservationDetailByProcess(@RequestParam("processInsId") String processInsId) {
        return reservationPassService.getReservationDetailByProcess(processInsId);
    }

}
