package com.guhecloud.cube.srv.gen.securitymanagement.server.information.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.dao.ProgramScheduleMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.entity.ProgramScheduleEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.param.ProgramScheduleParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.service.ProgramScheduleService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.vo.ProgramSchedulePageVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProgramScheduleServiceImpl extends ServiceImpl<ProgramScheduleMapper, ProgramScheduleEntity> implements ProgramScheduleService {

    private final ProgramScheduleMapper programScheduleMapper;

    @Override
    public void insertOrUpdateProgramSchedule(List<ProgramScheduleEntity> dataList) {
        programScheduleMapper.insertOrUpdateProgramSchedule(dataList);
    }

    @Override
    public Pagination<ProgramSchedulePageVo> getProgramSchedulePage(ProgramScheduleParam param) {
        return PagingUtils.convert(programScheduleMapper.getPlayRecordPage(PagingUtils.convert(param), param));
    }

    @Override
    public Integer getProgramScheduleCount(ProgramScheduleParam param) {
        return programScheduleMapper.getCountByParam(param);
    }
}
