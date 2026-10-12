package com.guhecloud.cube.biz.ouaa.server.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.AppointmentMapper;
import com.guhecloud.cube.biz.ouaa.server.dto.AppointmentDetailVO;
import com.guhecloud.cube.biz.ouaa.server.entity.Appointment;
import com.guhecloud.cube.biz.ouaa.server.param.AppointmentQueryParam;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ConvertUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.entity.BasicEntity;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author bq
 * @date 2022/11/08
 * @description 职位查询
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AppointmentQueryService extends ServiceImpl<AppointmentMapper, Appointment> {

    private final AppointmentMapper appointmentMapper;

    public AppointmentDetailVO get(Long id) {
        return appointmentMapper.getById(id);
    }

    public List<AppointmentDetailVO> get(List<Long> ids) {
        return CollectionUtils.isEmpty(ids) ? Collections.emptyList() : appointmentMapper.getByIds(ids);
    }

    public List<AppointmentDetailVO> getList(AppointmentQueryParam appointmentQueryParam) {
        List<Appointment> appointmentPOS = this.getQueryChainWrapper(appointmentQueryParam).list();
        return ObjectUtils.copying(appointmentPOS, AppointmentDetailVO.class);
    }

    public Pagination<AppointmentDetailVO> getPage(AppointmentQueryParam appointmentQueryParam) {
        Page<Appointment> page = this.getQueryChainWrapper(appointmentQueryParam)
                .page(PagingUtils.convert(appointmentQueryParam));
        return PagingUtils.convert(page, AppointmentDetailVO.class);
    }

    public List<AppointmentDetailVO> getAppointmentByIds(String ids) {
        if (ObjectUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        List<Appointment> appointments = this.baseMapper.selectList(new LambdaQueryWrapper<Appointment>()
                                                                            .in(Appointment::getId, Arrays.stream(ids.split(","))
                                                                                    .map(Long::parseLong)
                                                                                    .collect(Collectors.toList())));
        return ConvertUtils.toList(AppointmentDetailVO.class, appointments);
    }

    private LambdaQueryChainWrapper<Appointment> getQueryChainWrapper(AppointmentQueryParam param) {
        return this.lambdaQuery()
                .like(StringUtils.isNotEmpty(param.getKeyword()), Appointment::getName, param.getKeyword())
                .eq(null != param.getEnable(), Appointment::getEnable, param.getEnable())
                .in(CollectionUtils.isNotEmpty(param.getIds()), Appointment::getId, param.getIds())
                .orderByDesc(BasicEntity::getUpdateTime);
    }

    /**
     * 职位信息查询
     *
     * @param appointments 职位id
     *
     * @return 职位信息
     */
    public List<AppointmentDetailVO> listAppointment(List<Long> appointments) {
        if (ObjectUtils.isEmpty(appointments)) {
            return Collections.emptyList();
        }
        List<Appointment> list = this.baseMapper.selectList(new LambdaQueryWrapper<Appointment>()
                                                                    .in(Appointment::getId, appointments));
        return ConvertUtils.toList(AppointmentDetailVO.class, list);
    }
}
