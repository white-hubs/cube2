package com.guhecloud.cube.srv.gen.park.management.server.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.RoomScadaResourcesCommandService;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command.RoomScadaResourcesParam;
import com.guhecloud.cube.srv.gen.park.management.server.dao.RoomScadaResourcesMapper;
import com.guhecloud.cube.srv.gen.park.management.server.po.RoomScadaResourcesPO;
import com.guhecloud.cube.srv.gen.park.management.server.validator.RoomScadaResourcesValidator;
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
public class RoomScadaResourcesCommandServiceImpl extends ServiceImpl<RoomScadaResourcesMapper, RoomScadaResourcesPO> implements RoomScadaResourcesCommandService {

    private final RoomScadaResourcesValidator validator;

    @Override
    public void save(RoomScadaResourcesParam param) {
        // 校验名称重复
        validator.verifyIsExistByName(param.getAreaId());

        RoomScadaResourcesPO po = ObjectUtils.copying(param, RoomScadaResourcesPO.class);
        // 保存
        this.save(po);

    }

    @Override
    public void update(Long id, RoomScadaResourcesParam param) {
        RoomScadaResourcesPO po = this.getById(id);
        Assert.isNotFound(po, "未找到指定的房间组态图!");

        validator.verifyIsExistUpdateByName(id, param.getAreaId());

        // 更新
        ObjectUtils.copying(param, po);
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
