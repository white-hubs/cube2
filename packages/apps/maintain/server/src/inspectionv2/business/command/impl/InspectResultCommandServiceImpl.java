package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectResultCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectResultMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectResultCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectResultOptionCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectResultPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.validator.InspectResultValidator;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.BizCodeUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.base.util.StringUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectResultCommandServiceImpl extends ServiceImpl<InspectResultMapper, InspectResultPO> implements InspectResultCommandService {

    private final InspectResultValidator validator;

    @Override
    public void save(InspectResultCommandModel commandModel) {
        // 验证
        validator.verifyIsExistByName(commandModel.getName());

        this.addInspectResult(null, commandModel);
    }

    @Override
    public void update(Long id, InspectResultCommandModel commandModel) {
        Long count = this.lambdaQuery().eq(InspectResultPO::getId, id).count();
        Assert.isNotFound(count == 0, "未找到指定的巡检结果!");

        validator.verifyIsExistUpdateByName(id, commandModel.getName());

        // 更新
        this.lambdaUpdate()
                .eq(InspectResultPO::getId, id)
                .remove();

        this.addInspectResult(id, commandModel);
    }

    @Override
    public void remove(Long id) {
        InspectResultPO po = InspectResultPO.builder().id(id).build();
        DatabaseContextHolder.softDelete(this, Collections.singletonList(po));
    }

    @Override
    public void removeBatch(String ids) {
        if (ids.contains(",")) {
            for (String id : ids.split(",")) {
                this.remove(Long.valueOf(id));
            }
        } else {
            this.remove(Long.valueOf(ids));
        }
    }

    // 添加结果方法
    private void addInspectResult(Long id, InspectResultCommandModel commandModel) {
        InspectResultPO resultPO = ObjectUtils.copying(commandModel, InspectResultPO.class);
        resultPO.setId(id);
        if (StringUtils.isEmpty(resultPO.getCode())) {
            resultPO.setCode(BizCodeUtils.generateNoRandom("XJJG"));
        }
        List<InspectResultOptionCommandModel> optionList = commandModel.getOptions();
        if (null != optionList && optionList.size() > 0) {
            optionList.forEach(optionCommandModel -> {
                resultPO.setItemName(optionCommandModel.getItemName());
                resultPO.setItemValue(optionCommandModel.getItemValue());
                resultPO.setErrorFlag(optionCommandModel.getErrorFlag());
                this.save(resultPO);
            });
        }
    }
}
