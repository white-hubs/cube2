package com.guhecloud.cube.srv.gen.park.management.server.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.FileCommonApi;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.RoomCommandService;
import com.guhecloud.cube.srv.gen.park.management.server.commandgateway.command.RoomParam;
import com.guhecloud.cube.srv.gen.park.management.server.dao.RoomMapper;
import com.guhecloud.cube.srv.gen.park.management.server.dao.RoomRentRecordMapper;
import com.guhecloud.cube.srv.gen.park.management.server.po.RoomPO;
import com.guhecloud.cube.srv.gen.park.management.server.validator.RoomValidator;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(rollbackFor = Exception.class)
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoomCommandServiceImpl extends ServiceImpl<RoomMapper, RoomPO> implements RoomCommandService {

    private final RoomValidator validator;

    private final FileCommonApi fileCommonApi;

    private final RoomRentRecordMapper roomRentRecordMapper;

    @Override
    public void save(RoomParam param) {
        // 校验名称重复
        validator.verifyIsExistByName(param.getName());
        RoomPO po = ObjectUtils.copying(param, RoomPO.class);
        Assert.isParameterError(po.getFloorageBilling() > po.getFloorage(), "计费面积不能大于建筑面积");
        // 保存
        this.save(po);

        // 保存文件
        fileCommonApi.saveFiles(po.getId(), param.getFileIds());

    }

    @Override
    public void update(Long id, RoomParam param) {
        RoomPO po = this.getById(id);
        Assert.isNotFound(po, "未找到指定的房间!");

        validator.verifyIsExistUpdateByName(id, param.getName(), param);
        // 更新
        ObjectUtils.copying(param, po);
        Assert.isParameterError(po.getFloorageBilling() > po.getFloorage(), "计费面积不能大于建筑面积");
        this.updateById(po);

        // 保存文件
        fileCommonApi.saveFiles(po.getId(), param.getFileIds());

    }

    @Override
    public void remove(Long id) {

        List<Long> ids = roomRentRecordMapper.getIds(id);
        Assert.isNotAcceptable(!ids.isEmpty(), "已被租赁不能删除");
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
    public void saveOrUpdateEnterprise(List<Long> roomIds, Long enterpriseId) {
        if (CollectionUtils.isNotEmpty(roomIds)) {
            this.lambdaUpdate()
                    .in(RoomPO::getId, roomIds)
                    .set(RoomPO::getEnterpriseId, enterpriseId)
                    .update();
        }

    }
}
