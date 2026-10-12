package com.guhecloud.cube.srv.gen.park.management.server.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.TypeDataCommandService;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command.TypeDataParam;
import com.guhecloud.cube.srv.gen.park.management.server.dao.TypeDataMapper;
import com.guhecloud.cube.srv.gen.park.management.server.po.TypeDataPO;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TypeDataCommandServiceImpl extends ServiceImpl<TypeDataMapper, TypeDataPO> implements TypeDataCommandService {

    @Override
    public void save(String tag, TypeDataParam param) {
        // 验证 同tag下 名称不能重复
        long count = this.lambdaQuery()
                .eq(TypeDataPO::getTag, tag)
                .eq(TypeDataPO::getName, param.getName())
                .count();
        Assert.isConflict(count != 0, "已存在相同的名称");

        // 保存
        TypeDataPO po = new TypeDataPO();
        po.setTag(tag);
        po.setName(param.getName());

        this.save(po);
    }

    @Override
    public void update(Long id, TypeDataParam param) {
        TypeDataPO po = this.getById(id);
        Assert.isNotFound(po, "未找到指定的类型!");

        long count = this.lambdaQuery()
                .eq(TypeDataPO::getTag, po.getTag())
                .eq(TypeDataPO::getTag, param.getName())
                .ne(TypeDataPO::getId, id)
                .count();

        Assert.isConflict(count != 0, "已存在相同的名称");
        po.setName(param.getName());
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
