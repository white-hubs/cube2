package com.guhecloud.cube.srv.gen.securitymanagement.server.information.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.entity.ProgramEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.param.DownloadMaterialsParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.param.ProgramPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.vo.DownloadMaterialVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.vo.ProgramPageVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.subsystem.requester.sdk.param.information.SendProgramToTerminalParam;

import java.util.List;

public interface ProgramService extends IService<ProgramEntity> {

    void insertOrUpdateProgram(List<ProgramEntity> programEntities);

    Pagination<ProgramPageVo> getProgramPage(ProgramPageParam param);

    List<DownloadMaterialVo> getMaterialsByProgramIds(DownloadMaterialsParam param);

    void sendProgramToTerminal(SendProgramToTerminalParam param) throws JsonProcessingException;
}
