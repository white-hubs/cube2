package com.guhecloud.cube.srv.gen.securitymanagement.server.information.service.impl;

import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.CommandDispatchRecordMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.CommandDispatchRecordEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.dao.ProgramMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.entity.ProgramEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.param.DownloadMaterialsParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.param.ProgramPageParam;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.service.ProgramService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.vo.DownloadMaterialVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.vo.MaterialVo;
import com.guhecloud.cube.srv.gen.securitymanagement.server.information.vo.ProgramPageVo;
import com.guhecloud.platform.base.context.SessionContextHolder;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.audit.EntityAuditor;
import com.guhecloud.platform.database.util.PagingUtils;
import com.guhecloud.subsystem.requester.sdk.client.InformationClient;
import com.guhecloud.subsystem.requester.sdk.param.information.DeviceDTO;
import com.guhecloud.subsystem.requester.sdk.param.information.MaterialParam;
import com.guhecloud.subsystem.requester.sdk.param.information.SendProgramToTerminalParam;
import com.guhecloud.subsystem.requester.sdk.vo.information.ProgramVo;
import com.guhecloud.subsystem.requester.sdk.vo.information.SendProgramToTerminalVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ProgramServiceImpl extends ServiceImpl<ProgramMapper, ProgramEntity> implements ProgramService {

    private final ProgramMapper programMapper;

    private final InformationClient informationClient;

    private final CommandDispatchRecordMapper commandDispatchRecordMapper;

    @Override
    public void insertOrUpdateProgram(List<ProgramEntity> programEntities) {
        programMapper.insertOrUpdateProgram(programEntities);
    }

    @Override
    public Pagination<ProgramPageVo> getProgramPage(ProgramPageParam param) {
        return PagingUtils.convert(programMapper.getProgramPage(PagingUtils.convert(param), param));
    }

    @Override
    public List<DownloadMaterialVo> getMaterialsByProgramIds(DownloadMaterialsParam param) {
        List<ProgramVo> programVos = informationClient.getMaterialsByProgramIds(MaterialParam.builder()
                                                                                        .programIds(param.getThirdProgramIds()).build());
        return programVos.stream().map(programVo -> {
            DownloadMaterialVo model = new DownloadMaterialVo();
            model.setThirdProgramId(programVo.getId());
            model.setMaterialVos(programVo.getMaterials().stream().map(materialVo -> {
                MaterialVo vo = new MaterialVo();
                vo.setMaterialId(materialVo.getMaterialId());
                vo.setMaterialName(materialVo.getMaterialName());
                vo.setAddress(materialVo.getAddress());
                return vo;
            }).collect(Collectors.toList()));
            return model;
        }).collect(Collectors.toList());
    }

    @Override
    public void sendProgramToTerminal(SendProgramToTerminalParam param) throws JsonProcessingException {
        //发送节目
        List<SendProgramToTerminalVo> vo = informationClient.sendProgramToTerminal(param);
        for (SendProgramToTerminalVo sendProgramToTerminalVo : vo) {
            //持久化发送结果
            CommandDispatchRecordEntity commandDispatchRecordEntity = this.convertCommandDispatchRecord(sendProgramToTerminalVo, param);
            commandDispatchRecordMapper.insertOrUpdateProgram(commandDispatchRecordEntity);
        }
    }

    private CommandDispatchRecordEntity convertCommandDispatchRecord(SendProgramToTerminalVo vo,
                                                                     SendProgramToTerminalParam param
                                                                    ) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        CommandDispatchRecordEntity commandDispatchRecordEntity = CommandDispatchRecordEntity.builder()
                .id(IdWorker.getId())
                .applicationCode("INFORMATION_PUBLISH")
                .taskType("sendProgramToTerminal")
                .taskName(param.getProgramName())
                .sendDetails(objectMapper.writeValueAsString(param))
                .terminalIds(param.getTerminals().stream().map(DeviceDTO::getTerminalId).collect(Collectors.joining(",")))
                .sendStatus(vo.getSendStatus())
                .sendResult(vo.getSendResult())
                .thirdRecordId(vo.getId())
                .build();
        commandDispatchRecordEntity.setTenant(SessionContextHolder.getRequester().getTenant());
        commandDispatchRecordEntity.setDeleted(Boolean.FALSE);
        EntityAuditor.populateCreateAudit(commandDispatchRecordEntity);
        EntityAuditor.populateUpdateAudit(commandDispatchRecordEntity);
        return commandDispatchRecordEntity;
    }
}
