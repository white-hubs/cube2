package com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.dao.CommandDispatchRecordMapper;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.entity.CommandDispatchRecordEntity;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.query.CommandRecordPageQuery;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.service.CommandDispatchRecordService;
import com.guhecloud.cube.srv.gen.securitymanagement.server.common.vo.CommandRecordPageVo;
import com.guhecloud.platform.base.model.Pagination;
import com.guhecloud.platform.database.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CommandDispatchRecordServiceImpl extends ServiceImpl<CommandDispatchRecordMapper, CommandDispatchRecordEntity> implements CommandDispatchRecordService {

    private final CommandDispatchRecordMapper commandDispatchRecordMapper;

    @Override
    public Pagination<CommandRecordPageVo> getCommandDispatchRecordPage(CommandRecordPageQuery param) {
        Page<CommandDispatchRecordEntity> commandDispatchRecordPage = commandDispatchRecordMapper.getCommandDispatchRecordPage(PagingUtils.convert(param), param);
        Pagination<CommandRecordPageVo> convert = PagingUtils.convert(commandDispatchRecordPage, CommandRecordPageVo.class);
        //根据不同功能转换返回的实体
        ObjectMapper objectMapper = new ObjectMapper();
        convert.getRecords().forEach(item -> {
            try {
                item.setSendDetailsMap(objectMapper.readValue(item.getSendDetails(), Map.class));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            item.setTerminalTotal(StringUtils.isEmpty(item.getTerminalIds()) ? 0 : item.getTerminalIds().split(",").length);
        });
        return convert;
    }

    @Override
    public CommandRecordPageVo getCommandDispatchRecordInfo(Long id) {
        CommandDispatchRecordEntity commandDispatchRecordEntity = commandDispatchRecordMapper.selectById(id);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return CommandRecordPageVo.builder()
                    .id(commandDispatchRecordEntity.getId())
                    .applicationCode(commandDispatchRecordEntity.getApplicationCode())
                    .taskType(commandDispatchRecordEntity.getTaskType())
                    .taskName(commandDispatchRecordEntity.getTaskName())
                    .sendDetails(commandDispatchRecordEntity.getSendDetails())
                    .terminalIds(commandDispatchRecordEntity.getTerminalIds())
                    .sendStatus(commandDispatchRecordEntity.getSendStatus())
                    .sendResult(commandDispatchRecordEntity.getSendResult())
                    .thirdRecordId(commandDispatchRecordEntity.getThirdRecordId())
                    .sendDetailsMap(objectMapper.readValue(commandDispatchRecordEntity.getSendDetails(), Map.class)).build();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
