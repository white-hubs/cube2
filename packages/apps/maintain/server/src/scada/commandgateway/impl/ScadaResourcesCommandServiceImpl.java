package com.guhecloud.cube.srv.gen.maintain.server.scada.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.scada.commandgateway.ScadaResourcesCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.scada.commandgateway.command.ScadaResourcesCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.scada.dao.ScadaResourcesMapper;
import com.guhecloud.cube.srv.gen.maintain.server.scada.po.ScadaResourcesPO;
import com.guhecloud.cube.srv.gen.maintain.server.scada.validator.ScadaResourcesValidator;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScadaResourcesCommandServiceImpl extends ServiceImpl<ScadaResourcesMapper, ScadaResourcesPO> implements ScadaResourcesCommandService {

    private final ScadaResourcesValidator validator;

    @Override
    public Long save(ScadaResourcesCommandModel commandModel) {
        ScadaResourcesPO po = ObjectUtils.copying(commandModel, ScadaResourcesPO.class);
        validator.verifyIsExistByName(commandModel.getName());

        this.save(po);
        return po.getId();
    }

    @Override
    public void update(Long id, ScadaResourcesCommandModel commandModel) {
        ScadaResourcesPO po = this.getById(id);
        Assert.isNotFound(po, "未找到指定的巡检类型!");

        validator.verifyIsExistUpdateByName(id, commandModel.getName());

        // 更新
        ObjectUtils.copying(commandModel, po);
        this.updateById(po);
    }

    @Override
    public void remove(Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

    @Override
    public void removeBatch(String ids) {
        if (ids.contains(",")) {
            for (String id : ids.split(",")) {
                this.remove(Long.valueOf(id));
            }
        }
    }

}
