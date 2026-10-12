package com.guhecloud.cube.base.flow.form.commandgateway.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.flow.form.commandgateway.FormControlFlowCommandService;
import com.guhecloud.cube.base.flow.form.commandgateway.command.FormControlFlowCommand;
import com.guhecloud.cube.base.flow.form.dao.FormControlFlowMapper;
import com.guhecloud.cube.base.flow.form.po.FormControlFlowPO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author bq
 * @date 2022/10/25
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormControlFlowCommandServiceImpl extends ServiceImpl<FormControlFlowMapper, FormControlFlowPO> implements FormControlFlowCommandService {

    @Override
    public Long save(FormControlFlowCommand formControlFlowCommand) {
        // 删除旧的，保存新的
        this.remove(Wrappers.<FormControlFlowPO>lambdaQuery().eq(FormControlFlowPO::getFlowKey, formControlFlowCommand.getFlowKey())
                            .eq(FormControlFlowPO::getTaskKey, formControlFlowCommand.getTaskKey()));
        FormControlFlowPO formControlFlowPO = ObjectUtils.copying(formControlFlowCommand, FormControlFlowPO.class);
        this.save(formControlFlowPO);
        return formControlFlowPO.getId();
    }

}
