package com.guhecloud.cube.base.flow.context.aop;

import com.guhecloud.cube.base.flow.context.config.ActivityDeploymentCache;
import com.guhecloud.cube.base.flow.flowable.resolver.tool.ProcessExecuteContext;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @Author: zengyadong@guhecloud.com
 * @Date: 2026/3/5 17:46
 * @Description: 流程执行上下文切面
 */
@Slf4j
@Aspect
@Component
public class ProcessExecuteContextAspect {


    @Pointcut("execution(* com.guhecloud.cube.base.flow.context.submit.commandgateway.SubmitFormFlowCommandService.submitFirstForm(..)) || " +
              "execution(* com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.ActivatedTaskCommandService.complete(..)) || " +
              "execution(* com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.ActivatedTaskCommandService.copy(..)) || " +
              "execution(* com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.ActivatedTaskCommandService.transfer(..)) || " +
              "execution(* com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.ActivatedTaskCommandService.back(..)) || " +
              "execution(* com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.ActivatedTaskCommandService.undo(..)) || " +
              "execution(* com.guhecloud.cube.base.flow.flowable.task.application.commandgatway.ActivatedTaskCommandService.withdrawTask(..))")
    public void taskOperationPointcut() {}

    @Before("taskOperationPointcut()")
    public void manageProcessExecuteContext(JoinPoint joinPoint){
        ProcessExecuteContext.initLocalCache();
    }

    @After("taskOperationPointcut()")
    public void removeProcessExecuteContext(){
        ProcessExecuteContext.remove();
    }
}