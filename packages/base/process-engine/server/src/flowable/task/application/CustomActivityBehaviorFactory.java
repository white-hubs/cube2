package com.guhecloud.cube.base.flow.flowable.task.application;

import org.activiti.bpmn.model.Activity;
import org.activiti.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.SequentialMultiInstanceBehavior;
import org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;
import org.springframework.stereotype.Component;

/**
 * @author pengjian
 * @date 2022/9/7 22:02
 * @description
 */
@Component
public class CustomActivityBehaviorFactory extends DefaultActivityBehaviorFactory {

    @Override
    public SequentialMultiInstanceBehavior createSequentialMultiInstanceBehavior(Activity activity,
                                                                                 AbstractBpmnActivityBehavior innerActivityBehavior
                                                                                ) {
        return new CustomMultiInstanceActivityBehavior(activity, innerActivityBehavior);
    }
}
