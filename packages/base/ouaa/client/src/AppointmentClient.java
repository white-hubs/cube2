package com.guhecloud.cube.biz.ouaa.client;

import com.guhecloud.cube.biz.ouaa.client.dto.AppointmentDetailDTO;
import com.guhecloud.cube.biz.ouaa.client.param.AppointmentQueryParam;
import com.guhecloud.cube.biz.ouaa.client.param.SubmitAppointmentCommandParam;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @Author: lijie
 * @CreateTime: 2023-11-07  09:23
 * @Description:
 * @Version: 1.0
 */
@FeignClient(name = "ouaa")
public interface AppointmentClient {
    /**
     * 根据id查询岗位信息
     */
    @GetMapping("/getAppointmentByIds")
    List<AppointmentDetailDTO> getAppointmentByIds(@RequestParam("ids") String ids);

    @PostMapping("/appointment/post/map")
    Map<Long, AppointmentDetailDTO> postAppointmentMap(@RequestBody AppointmentQueryParam appointmentQueryParam);

    @GetMapping("/appointment/list")
    List<AppointmentDetailDTO> getList(@SpringQueryMap AppointmentQueryParam appointmentQueryParam);

    @PostMapping("/appointment")
    Long saveOrUpdate(@Valid @RequestBody SubmitAppointmentCommandParam submitAppointmentCommand);
}
