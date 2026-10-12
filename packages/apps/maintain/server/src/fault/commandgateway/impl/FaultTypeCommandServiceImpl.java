package com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.FaultTypeCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.fault.commandgateway.command.FaultTypeCommand;
import com.guhecloud.cube.srv.gen.maintain.server.fault.dao.FaultTypeMapper;
import com.guhecloud.cube.srv.gen.maintain.server.fault.po.FaultType;
import com.guhecloud.cube.srv.gen.maintain.server.fault.validator.FaultTypeValidator;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.ObjectUtils;
import com.guhecloud.platform.database.context.DatabaseContextHolder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author lzy
 * @date 2022/5/3
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FaultTypeCommandServiceImpl extends ServiceImpl<FaultTypeMapper, FaultType> implements FaultTypeCommandService {

    private final FaultTypeValidator faultTypeValidator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull FaultTypeCommand command) {
        // 验证
        this.faultTypeValidator.verifyIsExistFaultTypeName(command.getName());

        // 新增
        FaultType faultType = ObjectUtils.copying(command, FaultType.class);
        this.baseMapper.insert(faultType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull FaultTypeCommand command) {
        // 获取信息
        FaultType faultType = this.baseMapper.selectById(id);
        Assert.isNotFound(faultType, "不存在故障单类型信息!");

        // 验证
        this.faultTypeValidator.verifyIsExistUpdateFaultTypeByName(id, command.getName());

        // 更新
        ObjectUtils.copying(command, faultType);
        this.baseMapper.updateById(faultType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }
}
