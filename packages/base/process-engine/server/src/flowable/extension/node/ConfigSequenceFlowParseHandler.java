package com.guhecloud.cube.base.flow.flowable.extension.node;

import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.impl.bpmn.parser.BpmnParse;
import org.activiti.engine.impl.bpmn.parser.handler.SequenceFlowParseHandler;
import org.springframework.stereotype.Component;

/**
 * @author pengjian
 * @date 2022/8/31 14:00
 * @description
 */
@Component
public class ConfigSequenceFlowParseHandler extends SequenceFlowParseHandler {
    @Override
    protected void executeParse(BpmnParse bpmnParse, SequenceFlow sequenceFlow) {
        super.executeParse(bpmnParse, sequenceFlow);
    }
}
