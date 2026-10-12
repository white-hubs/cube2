package com.guhecloud.cube.base.message2.server.sat.process.pipeline;

import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 流程控制处理
 *
 * @author lanchun
 * @date 2023/5/8 11:42
 */
@Slf4j
public class ProcessController {

    /**
     * 处理模板
     */
    private Map<String, ProcessTemplate> processTemplateMap = null;

    public void setProcessTemplateMap(Map<String, ProcessTemplate> processTemplateMap) {
        this.processTemplateMap = processTemplateMap;
    }

    public ProcessContext<ProcessModel> process(ProcessContext<ProcessModel> processContext) {
        //前置检查(用于检查是否进行配置)
        this.preCheck(processContext);
        ProcessTemplate processTemplate = this.processTemplateMap.get(processContext.getCode());
        List<BusinessProcess<ProcessModel>> processes = processTemplate.getProcesses();
        for (BusinessProcess<ProcessModel> businessProcess : processes) {
            businessProcess.process(processContext);
            if (Boolean.TRUE.equals(processContext.getNeedBreak())) {
                break;
            }
        }
        return processContext;
    }

    private void preCheck(ProcessContext<ProcessModel> processContext) {
        if (Objects.isNull(processContext)) {
            throw new ServiceException("消息发送流程上下文为空");
        }
        //流程模板key
        String processTemplateCode = processContext.getCode();
        if (StringUtils.isEmpty(processTemplateCode)) {
            throw new ServiceException("流程模板配置编号为空");
        }
        ProcessTemplate processTemplate = this.processTemplateMap.get(processTemplateCode);
        if (Objects.isNull(processTemplate)) {
            throw new ServiceException("消息发送对应的流程模板配置为空");
        }
        List<BusinessProcess<ProcessModel>> processes = processTemplate.getProcesses();
        if (CollectionUtils.isEmpty(processes)) {
            throw new ServiceException("模板配置下的业务处理器为空");
        }
    }
}
