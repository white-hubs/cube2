package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectTypeCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectTypeMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectTypeCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTypePO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.validator.InspectTypeValidator;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectTypeCommandServiceImpl extends ServiceImpl<InspectTypeMapper, InspectTypePO> implements InspectTypeCommandService {

    private final InspectTypeValidator validator;

    @Override
    public void save(InspectTypeCommandModel command) {
        // 验证
        validator.verifyIsExistByName(command.getName());

        InspectTypePO typePO = InspectTypePO.builder()
                .name(command.getName())
                .build();
        this.save(typePO);
    }

    @Override
    public void update(Long id, InspectTypeCommandModel command) {
        InspectTypePO po = this.getById(id);
        Assert.isNotFound(po, "未找到指定的巡检类型!");

        validator.verifyIsExistUpdateByName(id, command.getName());

        // 更新
        ObjectUtils.copying(command, po);
        this.updateById(po);
    }

    @Override
    public void remove(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }
}
