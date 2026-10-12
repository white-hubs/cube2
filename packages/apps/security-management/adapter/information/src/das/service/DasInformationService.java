package com.guhecloud.cube.srv.gen.adapter.information.server.das.service;

import com.guhecloud.cube.srv.gen.adapter.information.server.param.ProgramScheduleParam;
import com.guhecloud.cube.srv.gen.adapter.information.server.param.SendProgramToTerminalParam;
import com.guhecloud.cube.srv.gen.adapter.information.server.vo.ProgramVo;
import com.guhecloud.cube.srv.gen.adapter.information.server.vo.SendProgramToTerminalVo;

import java.util.List;

public interface DasInformationService {
    List<ProgramVo> getProgramList(ProgramScheduleParam request);

    List<SendProgramToTerminalVo> sendProgramToTerminal(SendProgramToTerminalParam param);

}
