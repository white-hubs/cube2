package com.guhecloud.cube.base.flow.form.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.flow.form.commandgateway.FormControlBusinessCommandService;
import com.guhecloud.cube.base.flow.form.commandgateway.command.FormControlBusinessCommand;
import com.guhecloud.cube.base.flow.form.dao.FormControlBusinessMapper;
import com.guhecloud.cube.base.flow.form.po.FormControlBusinessPO;
import com.guhecloud.platform.base.util.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormControlBusinessCommandServiceImpl extends ServiceImpl<FormControlBusinessMapper, FormControlBusinessPO> implements FormControlBusinessCommandService {

    @Override
    public Long saveOrUpdate(FormControlBusinessCommand formControlBusinessCommand) {
        FormControlBusinessPO businessPO = ObjectUtils.copying(formControlBusinessCommand, FormControlBusinessPO.class);
        this.save(businessPO);
        return businessPO.getId();
    }

}

