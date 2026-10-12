package com.guhecloud.cube.base.flow.context.submit.interfaces;

import com.guhecloud.cube.base.flow.context.submit.commandgateway.SubmitFormFlowCommandService;
import instance.command.SubmitInitFormCommand;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SubmitFlowFormController {

    private final SubmitFormFlowCommandService submitFormFlowCommandService;

    /**
     * 审批流程发起
     *
     * @param formFlowCommand
     *
     * @return
     */
    @PostMapping("/form-flows/first/submit")
    public String submitFirst(@Valid @RequestBody SubmitInitFormCommand formFlowCommand) {
        return submitFormFlowCommandService.submitFirstForm(formFlowCommand);
    }

    /**
     * 内部服务流程发起
     *
     * @param formFlowCommand
     *
     * @return
     */
    @PostMapping("/form-flows/inner/submit")
    public String submitInnerServer(@Valid @RequestBody SubmitInitFormCommand formFlowCommand) {
        return submitFormFlowCommandService.submitInnerServerForm(formFlowCommand);
    }
}


