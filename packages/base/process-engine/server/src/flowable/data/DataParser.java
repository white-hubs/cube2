package com.guhecloud.cube.base.flow.flowable.data;

import com.guhecloud.cube.base.flow.flowable.process.domain.ProcessInstanceContext;
import com.guhecloud.cube.base.flow.form.resolver.FormResultDataResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DataParser {
    private final FormResultDataResolver formResultDataResolver;

    public String parse(ProcessInstanceContext processInstanceContext) {
        /**
         * 不管流程结果怎样，都要通知业务方处理
         */
        String data = processInstanceContext.getExtProcessInstance().getData();
        //保存表单数据
        String formInstanceId = processInstanceContext.getFormInstanceId();
        // 此流程是否关联了表单
        if (Objects.nonNull(formInstanceId)) {
            data = formResultDataResolver.parse(Long.parseLong(formInstanceId), data);
        }
        return data;
    }
}
