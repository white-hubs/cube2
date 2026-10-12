package com.guhecloud.cube.srv.gen.park.management.server.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.EnterpriseCommandService;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.FileCommonApi;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command.EnterpriseParam;
import com.guhecloud.cube.srv.gen.park.management.server.dao.EnterpriseMapper;
import com.guhecloud.cube.srv.gen.park.management.server.po.EnterprisePO;
import com.guhecloud.cube.srv.gen.park.management.server.validator.EnterpriseValidator;
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
public class EnterpriseCommandServiceImpl extends ServiceImpl<EnterpriseMapper, EnterprisePO> implements EnterpriseCommandService {

    private final EnterpriseValidator validator;

    private final FileCommonApi fileCommonApi;

    @Override
    public void save(EnterpriseParam param) {
        // 校验名称重复
        validator.verifyIsExistByName(param.getName());

        EnterprisePO po = ObjectUtils.copying(param, EnterprisePO.class);
        // 保存
        this.save(po);

        // 保存文件
        fileCommonApi.saveFiles(po.getId(), param.getFileIds());

    }

    @Override
    public void update(Long id, EnterpriseParam param) {
        EnterprisePO po = this.getById(id);
        Assert.isNotFound(po, "未找到指定的企业!");

        validator.verifyIsExistUpdateByName(id, param.getName());

        // 更新
        ObjectUtils.copying(param, po);
        this.updateById(po);
        fileCommonApi.removeFilesByBusinessKey(po.getId());
        // 保存文件
        fileCommonApi.saveFiles(po.getId(), param.getFileIds());

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

    @Override
    public void addRoomRentTime(Long enterpriseId) {
        this.lambdaUpdate()
                .setSql("room_rent_time=room_rent_time+" + 1)
                .eq(EnterprisePO::getId, enterpriseId)
                .update();

    }

    @Override
    public void reduceRoomRentTime(Long enterpriseId) {
        this.lambdaUpdate()
                .setSql("room_rent_time=room_rent_time-" + 1)
                .eq(EnterprisePO::getId, enterpriseId)
                .gt(EnterprisePO::getRoomRentTime, 0)
                .update();

    }

}
