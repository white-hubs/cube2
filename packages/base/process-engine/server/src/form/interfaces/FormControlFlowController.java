package com.guhecloud.cube.base.flow.form.interfaces;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.base.flow.form.commandgateway.FormControlFlowCommandService;
import com.guhecloud.cube.base.flow.form.commandgateway.command.FormControlFlowCommand;
import com.guhecloud.cube.base.flow.form.dao.FormControlFlowMapper;
import com.guhecloud.cube.base.flow.form.po.FormControlFlowPO;
import lombok.RequiredArgsConstructor;
import com.guhecloud.platform.base.util.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormControlFlowController {

    private final FormControlFlowMapper formControlFlowMapper;
    private final FormControlFlowCommandService formControlFlowCommandService;

    @GetMapping("/form-control-flow/visibility")
    public List<FormControlFlowPO> get(String flowKey, String taskKey) {
        return formControlFlowMapper.selectList(Wrappers.<FormControlFlowPO>lambdaQuery().eq(FormControlFlowPO::getFlowKey, flowKey)
                                                        .eq(StringUtils.isNotEmpty(taskKey), FormControlFlowPO::getTaskKey, taskKey));
    }

    @PostMapping("/form-control-flow/visibility")
    public Long save(@Valid @RequestBody FormControlFlowCommand formControlFlowCommand) {
        return formControlFlowCommandService.save(formControlFlowCommand);
    }

}
