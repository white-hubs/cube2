package com.guhecloud.cube.base.flow.form.commandgateway.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.base.flow.form.commandgateway.FormDefinitionCommandService;
import com.guhecloud.cube.base.flow.form.commandgateway.command.PublishFormDefinitionCommand;
import com.guhecloud.cube.base.flow.form.dao.FormControlBusinessMapper;
import com.guhecloud.cube.base.flow.form.dao.FormDefinitionMapper;
import com.guhecloud.cube.base.flow.form.po.FormControlBusinessPO;
import com.guhecloud.cube.base.flow.form.po.FormDefinitionPO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.standard.exception.ServiceException;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author bq
 * @date 2022/09/16
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FormDefinitionCommandServiceImpl extends ServiceImpl<FormDefinitionMapper, FormDefinitionPO> implements FormDefinitionCommandService {

    private final FormControlBusinessMapper formControlBusinessMapper;

    @Override
    public Long publish(PublishFormDefinitionCommand publishFormDefinitionCommand) {
        Assert.isParameterError(StringUtils.isEmpty(publishFormDefinitionCommand.getJsonSchema()), "不能保存空表单");
        FormDefinitionPO definitionPO = ObjectUtils.copying(publishFormDefinitionCommand, FormDefinitionPO.class);
        // 查询定义key是否存在，存在版本加1，不存在则新增
        int version = this.generateVersion(publishFormDefinitionCommand.getKey());
        definitionPO.setVersion(version);
        try {
            this.save(definitionPO);
        } catch (DuplicateKeyException e) {
            throw new ServiceException("操作太频繁了!");
        }
        //自动表单配置
        this.autoBindBizField(definitionPO.getId(), publishFormDefinitionCommand.getKey(), version);
        return definitionPO.getId();
    }

    /**
     * 当第一个版本进行了流程配置后，再次编辑表单流程，由于表单升级，造成表单ID发生变化，导致后续业务问题
     * 这里将会进行升级后的自动绑定；
     * 但是第一个版本都未进行配置或是表单控件被重新拖动编辑时，自动绑定将会失效；只能从界面上进行重新配置，方能生效
     *
     * @param newFormId
     * @param formKey
     * @param version
     */
    private void autoBindBizField(Long newFormId, String formKey, int version) {
        version = version <= 1 ? 1 : version - 1;
        FormDefinitionPO formDef = this.lambdaQuery().eq(FormDefinitionPO::getKey, formKey)
                .eq(FormDefinitionPO::getVersion, version).one();
        if (formDef != null) {
            List<FormControlBusinessPO> formBizList = formControlBusinessMapper.selectList(Wrappers.<FormControlBusinessPO>lambdaQuery()
                                                                                                   .eq(FormControlBusinessPO::getFormDefinitionId, formDef.getId()));
            if (CollectionUtils.isNotEmpty(formBizList)) {
                formBizList.stream().forEach(fb -> {
                    fb.setId(null);
                    fb.setFormDefinitionId(newFormId);
                    formControlBusinessMapper.insert(fb);
                });
            }
        }
    }

    /**
     * 生成表单定义的版本号
     *
     * @param key 表单定义key
     *
     * @return
     */
    private synchronized int generateVersion(String key) {
        // 版本号
        int version = 1;
        List<FormDefinitionPO> definitionPOS = this.lambdaQuery().eq(FormDefinitionPO::getKey, key)
                .orderByDesc(FormDefinitionPO::getVersion).list();
        if (!definitionPOS.isEmpty()) {
            version = definitionPOS.getFirst().getVersion() + 1;
        }
        return version;
    }

}
