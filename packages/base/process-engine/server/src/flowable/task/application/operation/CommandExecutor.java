package com.guhecloud.cube.base.flow.flowable.task.application.operation;

import com.guhecloud.platform.base.context.ApplicationContextHolder;
import com.guhecloud.platform.base.util.CollectionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class CommandExecutor {

    private List<LocalTaskOperationHandler> localHandlers;

    public void execute(TaskOperationCommand taskOperationCommand) {
        // 执行具体的命令处理器
        Class<? extends TaskOperationCommand> commandType = taskOperationCommand.getClass();
        // 找到接口的所有实现类
        Map<String, ?> beansOfType = ApplicationContextHolder.getBeansOfType(TaskOperation.class);
        beansOfType.values().forEach(value -> {
            Method[] declaredMethods = ReflectionUtils.getDeclaredMethods(value.getClass());
            Method addLocalHandler = ReflectionUtils.findMethod(value.getClass(), "addLocalHandler", LocalTaskOperationHandler.class);

            for (Method declaredMethod : declaredMethods) {
                if (declaredMethod.getName().equals("execute")) {
                    Class<?>[] parameterTypes = declaredMethod.getParameterTypes();
                    if (commandType == parameterTypes[0]) {

                        Class<?> declaringClass = declaredMethod.getDeclaringClass();
                        TaskOperation bean = (TaskOperation) ApplicationContextHolder.getBean(declaringClass);
                        if (CollectionUtils.isNotEmpty(localHandlers)) {
                            localHandlers.forEach(localTaskOperationHandler -> ReflectionUtils.invokeMethod(addLocalHandler, bean, localTaskOperationHandler));
                            localHandlers.clear();
                        }
                        bean.exec(taskOperationCommand);
                        break;
                    }
                }
            }
        });
    }

    /**
     * 添加局部处理器
     *
     * @param localHandler
     */
    public synchronized CommandExecutor addLocalHandler(LocalTaskOperationHandler localHandler) {
        if (CollectionUtils.isEmpty(localHandlers)) {
            localHandlers = new LinkedList<>();
        }
        localHandlers.add(localHandler);
        return this;
    }

}
