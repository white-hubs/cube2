package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.HiddenDangerLevelCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.command.HiddenDangerLevelCommand;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.dao.HiddenDangerLevelMapper;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerLevel;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.validator.HiddenDangerLevelValidator;
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
 * @date 2022/4/24
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HiddenDangerLevelCommandServiceImpl extends ServiceImpl<HiddenDangerLevelMapper, HiddenDangerLevel> implements HiddenDangerLevelCommandService {

    private final HiddenDangerLevelMapper hiddenDangerLevelMapper;

    private final HiddenDangerLevelValidator hiddenDangerLevelValidator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull HiddenDangerLevelCommand command) {
        // 验证名称是否相同
        this.hiddenDangerLevelValidator.verifyIsExistHiddenDangerLevelByName(command.getName());

        // 新增
        HiddenDangerLevel hiddenDangerLevel = ObjectUtils.copying(command, HiddenDangerLevel.class);
        this.hiddenDangerLevelMapper.insert(hiddenDangerLevel);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull HiddenDangerLevelCommand command) {
        // 获取信息
        HiddenDangerLevel hiddenDangerLevel = this.hiddenDangerLevelMapper.selectById(id);
        Assert.isNotFound(hiddenDangerLevel, "不存在隐患程度信息!");

        // 验证
        this.hiddenDangerLevelValidator.verifyIsExistUpdateHiddenDangerLevelByName(id, command.getName());

        // 更新
        ObjectUtils.copying(command, hiddenDangerLevel);
        this.hiddenDangerLevelMapper.updateById(hiddenDangerLevel);

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        DatabaseContextHolder.softDelete(this, id);
    }
}
