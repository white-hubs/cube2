package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.AppointmentMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.Appointment;
import com.guhecloud.cube.biz.ouaa.server.param.SubmitAppointmentCommand;
import com.guhecloud.cube.biz.ouaa.server.service.AppointmentCommandService;
import com.guhecloud.cube.biz.ouaa.server.service.UserOrganizationService;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Objects;

/**
 * @author bq
 * @date 2022/11/08
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppointmentCommandServiceImpl extends ServiceImpl<AppointmentMapper, Appointment> implements AppointmentCommandService {

    private final UserOrganizationService userOrganizationService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long saveOrUpdate(SubmitAppointmentCommand submitAppointmentCommand) {
        Long id = submitAppointmentCommand.getId();
        this.verifyIsExistByName(id, submitAppointmentCommand.getName());
        if (Objects.nonNull(id)) {
            Appointment appointmentPO = this.getById(id);
            if (null != appointmentPO) {
                appointmentPO.setEnable(submitAppointmentCommand.getEnable());
                ObjectUtils.copying(submitAppointmentCommand, appointmentPO);
                this.updateById(appointmentPO);
            }
        } else {
            Appointment appointmentPO = ObjectUtils.copying(submitAppointmentCommand, Appointment.class);
            appointmentPO.setEnable(true);
            this.save(appointmentPO);
            id = appointmentPO.getId();
        }
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAppointment(Long id) {
        Integer userCount = this.userOrganizationService.countUserAppointments(Collections.singletonList(id));
        Assert.isNotAcceptable(0 != userCount, "删除失败,该岗位下存在员工");
        DatabaseContextHolder.softDelete(this, id);
    }

    private void verifyIsExistByName(Long id, String name) {
        Appointment po = this.lambdaQuery()
                .eq(Appointment::getName, name)
                .ne(null != id, Appointment::getId, id).one();
        Assert.isConflict(Objects.nonNull(po), "存在相同名称!");
    }

}
