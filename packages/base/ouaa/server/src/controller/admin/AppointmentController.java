package com.guhecloud.cube.biz.ouaa.server.controller.admin;

import com.guhecloud.cube.biz.ouaa.server.dto.AppointmentDetailVO;
import com.guhecloud.cube.biz.ouaa.server.param.AppointmentQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.PosistionQueryParam;
import com.guhecloud.cube.biz.ouaa.server.param.SubmitAppointmentCommand;
import com.guhecloud.cube.biz.ouaa.server.service.AppointmentCommandService;
import com.guhecloud.cube.biz.ouaa.server.service.AppointmentQueryService;
import com.guhecloud.cube.biz.ouaa.server.service.EnterpriseExportService;
import com.guhecloud.platform.base.model.Pagination;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppointmentController {

    private final AppointmentCommandService appointmentCommandService;
    private final AppointmentQueryService appointmentQueryService;
    private final EnterpriseExportService enterpriseExportService;

    @GetMapping("/appointment/{id}")
    public AppointmentDetailVO getById(@PathVariable("id") Long id) {
        return appointmentQueryService.get(id);
    }

    @PostMapping("/appointment/post/map")
    public Map<Long, AppointmentDetailVO> postAppointmentMap(@RequestBody AppointmentQueryParam appointmentQueryParam) {
        Map<Long, AppointmentDetailVO> map = new HashMap<>();
        var list = this.getList(appointmentQueryParam);
        Optional.ofNullable(list).orElse(Collections.emptyList()).forEach(appointment -> map.put(appointment.getId(), appointment));
        return map;
    }

    @GetMapping("/appointment/list")
    public List<AppointmentDetailVO> getList(AppointmentQueryParam appointmentQueryParam) {
        return appointmentQueryService.getList(appointmentQueryParam);
    }

    @GetMapping("/appointment/page")
    public Pagination<AppointmentDetailVO> getPage(AppointmentQueryParam appointmentQueryParam) {
        return appointmentQueryService.getPage(appointmentQueryParam);
    }

    @PostMapping("/appointment")
    public Long saveOrUpdate(@Valid @RequestBody SubmitAppointmentCommand submitAppointmentCommand) {
        return appointmentCommandService.saveOrUpdate(submitAppointmentCommand);
    }

    @GetMapping("/getAppointmentByIds")
    public List<AppointmentDetailVO> getAppointmentByIds(@RequestParam("ids") String ids) {
        return appointmentQueryService.getAppointmentByIds(ids);
    }

    @DeleteMapping("/appointment/{id}")
    public void deleteAppointment(@PathVariable("id") Long id) {
        appointmentCommandService.deleteAppointment(id);
    }

    /**
     * 职位导出
     *
     * @param param 筛选条件
     */
    @GetMapping("/appointment/export")
    public void export(@SpringQueryMap @Valid PosistionQueryParam param) {
        enterpriseExportService.exportPositions(param);
    }
}
