package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectItemTemplateCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectItemTemplateMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectItemBatchCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectItemCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectItemTemplatePO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectItemTemplateCommandServiceImpl extends ServiceImpl<InspectItemTemplateMapper, InspectItemTemplatePO> implements InspectItemTemplateCommandService {

    @Override
    public void addInspectItem(InspectItemBatchCommandModel command) {
        if (!CollectionUtils.isEmpty(command.getItems())) {
            command.getItems().forEach(c -> {
                InspectItemTemplatePO copying = ObjectUtils.copying(c, InspectItemTemplatePO.class);
                copying.setAlias(command.getAlias());
                copying.setReferType(command.getReferType());
                copying.setIsResultItem(command.getIsResultItem());
                this.baseMapper.insert(copying);
            });
        }
    }

    @Override
    public void updateInspectItem(Long id, InspectItemCommandModel command) {
        InspectItemTemplatePO po = this.baseMapper.selectById(id);
        Assert.isNotFound(po, "未找到相关信息，无法修改");
        ObjectUtils.copying(command, po);
        this.baseMapper.updateById(po);
    }

    @Override
    public void deleteInspectItem(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }
}
