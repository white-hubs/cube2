package com.guhecloud.cube.base.message2.server.sat.process.pipeline.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.guhecloud.cube.base.message2.server.base.dto.VarMappingDetailDTO;
import com.guhecloud.cube.base.message2.server.base.entity.VarMapping;
import com.guhecloud.cube.base.message2.server.base.service.VarMappingService;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.BusinessProcess;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.ProcessContext;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.domain.MessageInfo;
import com.guhecloud.cube.base.message2.server.sat.process.pipeline.handler.context.MsgSendContext;
import com.guhecloud.platform.base.util.JsonUtils;
import com.guhecloud.platform.rocketmq.context.RocketMQContextHolder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.guhecloud.platform.base.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.guhecloud.platform.base.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AfterMsgMqSendAction implements BusinessProcess<MsgSendContext> {

    private static final String MSG_SEND_TOPIC = "MSG_SEND_TASK";

    private final VarMappingService varMappingService;

    @Override
    public void process(ProcessContext<MsgSendContext> context) {
        List<VarMappingDetailDTO> varMappingDetailDTOS = getVarMapping();
        if (CollectionUtils.isEmpty(varMappingDetailDTOS)) {
            context.getProcessModel().getMessageInfos().forEach(messageInfo -> {
                messageInfo.setTimestamp(System.currentTimeMillis());
                RocketMQContextHolder.send(MSG_SEND_TOPIC, messageInfo);
            });
        } else {
            context.getProcessModel().getMessageInfos().forEach(messageInfo -> {
                Optional<VarMappingDetailDTO> mappingDetailDTO = varMappingDetailDTOS.stream()
                        .filter(varMappingDetailDTO -> validManufacturerVar(varMappingDetailDTO, messageInfo))
                        .findFirst();
                mappingDetailDTO.ifPresent(manufacturerVar ->
                                                   messageInfo.setTemplateContentParams(varReplace(messageInfo.getTemplateContentParams(), manufacturerVar.getVarMapping()))
                                          );
                messageInfo.setTimestamp(System.currentTimeMillis());
                RocketMQContextHolder.send(MSG_SEND_TOPIC, messageInfo);
            });
        }
    }

    public List<VarMappingDetailDTO> getVarMapping() {
        List<VarMappingDetailDTO> varMappingDetailDTOS = null;
        try {
            Optional<VarMapping> varMapping = this.varMappingService.getVarMapping();
            if (varMapping.isPresent() && StringUtils.isNotBlank(varMapping.get().getMappingJson())) {
                varMappingDetailDTOS = JsonUtils.deserialize(varMapping.get()
                                                                     .getMappingJson(), new TypeReference<List<VarMappingDetailDTO>>() {});
            }
        } catch (Exception e) {
            log.error("message get var_mapping error", e);
        }
        return varMappingDetailDTOS;
    }

    public boolean validManufacturerVar(VarMappingDetailDTO varMappingDetail, MessageInfo messageInfo) {
        return messageInfo.getChannelCode().name()
                       .equals(varMappingDetail.getChannelCode()) &&
               messageInfo.getManufacturerCode().name()
                       .equals(varMappingDetail.getMsgManufacturer());
    }

    public Map<String, String> varReplace(Map<String, String> vars, Map<String, String> manufacturerVars) {
        Map<String, String> result = new HashMap<>();
        for (String key : vars.keySet()) {
            String resultKey = key;
            if (manufacturerVars.containsKey(key)) {
                resultKey = manufacturerVars.get(key);
            }
            result.put(resultKey, vars.get(key));
        }
        return result;
    }
}
