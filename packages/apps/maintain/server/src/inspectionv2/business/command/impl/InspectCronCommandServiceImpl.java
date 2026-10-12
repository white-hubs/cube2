package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.business.command.InspectCronCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectCronMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.param.InspectCronCommandModel;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectCronPO;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.validator.InspectCronValidator;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectCronCommandServiceImpl extends ServiceImpl<InspectCronMapper, InspectCronPO> implements InspectCronCommandService {

    private final InspectCronValidator validator;

    @Override
    public void save(InspectCronCommandModel command) {
        // 验证
        validator.verifyIsExistByName(command.getName());

        // 校验cron表达式
        validator.verifyCron(command.getCron());

        InspectCronPO typePO = InspectCronPO.builder()
                .name(command.getName())
                .cron(command.getCron())
                .description(command.getDescription())
                .build();
        this.save(typePO);
    }

    @Override
    public void update(Long id, InspectCronCommandModel param) {
        InspectCronPO po = this.getById(id);
        Assert.isNotFound(po, "未找到指定的巡检周期!");

        validator.verifyIsExistUpdateByName(id, param.getName());

        // 校验cron表达式
        validator.verifyCron(param.getCron());

        // 更新
        ObjectUtils.copying(param, po);
        po.setId(id);
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
        } else {
            this.remove(Long.valueOf(ids));
        }
    }
}
