package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.biz.ouaa.server.entity.Appointment;
import com.guhecloud.cube.biz.ouaa.server.param.SubmitAppointmentCommand;

/**
 * @author bq
 * @date 2022/11/08
 * @description
 */
public interface AppointmentCommandService extends IService<Appointment> {

    /**
     * 保存或修改职位
     *
     * @param submitAppointmentCommand 提交数据
     *
     * @return
     */
    Long saveOrUpdate(SubmitAppointmentCommand submitAppointmentCommand);

    /**
     * 删除职位
     *
     * @param id
     */
    void deleteAppointment(Long id);
}
