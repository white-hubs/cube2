package com.guhecloud.cube.base.flow.flowable.task.application;

import base.constant.VariableConstant;
import com.guhecloud.cube.base.flow.flowable.scene.approval.domain.enumeration.ApprovalResult;
import org.activiti.bpmn.model.Activity;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.impl.bpmn.behavior.AbstractBpmnActivityBehavior;
import org.activiti.engine.impl.bpmn.behavior.SequentialMultiInstanceBehavior;

/**
 * @author pengjian
 * @date 2022/9/7 21:51
 * @description
 */
public class CustomMultiInstanceActivityBehavior extends SequentialMultiInstanceBehavior {

    public CustomMultiInstanceActivityBehavior(Activity activity, AbstractBpmnActivityBehavior innerActivityBehavior) {
        super(activity, innerActivityBehavior);
    }

    @Override
    protected boolean completionConditionSatisfied(DelegateExecution execution) {
        boolean result = super.completionConditionSatisfied(execution);
        if (result) {
            execution.setVariable(VariableConstant.RESULT, ApprovalResult.PASS);
        }
        return result;
    }
}
