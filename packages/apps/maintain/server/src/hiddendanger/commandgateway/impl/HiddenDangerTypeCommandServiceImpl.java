package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.HiddenDangerTypeCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.command.HiddenDangerTypeCommand;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.dao.HiddenDangerTypeMapper;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerType;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.validator.HiddenDangerTypeValidator;
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
 * @date 2022/4/26
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HiddenDangerTypeCommandServiceImpl extends ServiceImpl<HiddenDangerTypeMapper, HiddenDangerType> implements HiddenDangerTypeCommandService {

    private final HiddenDangerTypeMapper hiddenDangerTypeMapper;

    private final HiddenDangerTypeValidator hiddenDangerTypeValidator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull HiddenDangerTypeCommand command) {
        // 验证
        this.hiddenDangerTypeValidator.verifyIsExistHiddenDangerTypeByName(command.getName());

        // 新增
        HiddenDangerType hiddenDangerType = ObjectUtils.copying(command, HiddenDangerType.class);
        this.hiddenDangerTypeMapper.insert(hiddenDangerType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull HiddenDangerTypeCommand command) {
        // 获取信息
        HiddenDangerType hiddenDangerType = this.hiddenDangerTypeMapper.selectById(id);
        Assert.isNotFound(hiddenDangerType, "不存在隐患类别信息!");

        // 验证
        this.hiddenDangerTypeValidator.verifyIsExistUpdateHiddenDangerTypeByName(id, command.getName());

        // 更新
        ObjectUtils.copying(command, hiddenDangerType);
        this.hiddenDangerTypeMapper.updateById(hiddenDangerType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }

}
