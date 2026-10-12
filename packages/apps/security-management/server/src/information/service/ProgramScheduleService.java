package com.guhecloud.cube.srv.gen.securitymanagement.server.information.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.entity.ProgramScheduleEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.param.ProgramScheduleParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.vo.ProgramSchedulePageVo;
import com.guhecloud.platform.base.model.Pagination;

import java.util.List;

public interface ProgramScheduleService extends IService<ProgramScheduleEntity> {

    void insertOrUpdateProgramSchedule(List<ProgramScheduleEntity> dataList);

    Pagination<ProgramSchedulePageVo> getProgramSchedulePage(ProgramScheduleParam param);

    Integer getProgramScheduleCount(ProgramScheduleParam param);
}
