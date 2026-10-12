package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.HiddenDangerCategoryCommandService;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.commandgateway.command.HiddenDangerCategoryCommand;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.dao.HiddenDangerCategoryMapper;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerCategory;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.validator.HiddenDangerCategoryValidator;
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
public class HiddenDangerCategoryCommandServiceImpl extends ServiceImpl<HiddenDangerCategoryMapper, HiddenDangerCategory> implements HiddenDangerCategoryCommandService {

    private final HiddenDangerCategoryMapper hiddenDangerCategoryMapper;

    private final HiddenDangerCategoryValidator hiddenDangerCategoryValidator;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(@NonNull HiddenDangerCategoryCommand command) {
        // 验证
        this.hiddenDangerCategoryValidator.verifyIsExistHiddenDangerCategoryByName(command.getName());

        // 新增
        HiddenDangerCategory hiddenDangerCategory = ObjectUtils.copying(command, HiddenDangerCategory.class);
        this.hiddenDangerCategoryMapper.insert(hiddenDangerCategory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(@NonNull Long id, @NonNull HiddenDangerCategoryCommand command) {
        // 获取信息
        HiddenDangerCategory hiddenDangerCategory = this.hiddenDangerCategoryMapper.selectById(id);
        Assert.isNotFound(hiddenDangerCategory, "不存在隐患分类信息!");

        // 验证
        this.hiddenDangerCategoryValidator.verifyIsExistUpdateHiddenDangerLevelByName(id, command.getName());

        // 更新
        ObjectUtils.copying(command, hiddenDangerCategory);
        this.hiddenDangerCategoryMapper.updateById(hiddenDangerCategory);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void remove(@NonNull Long id) {
        // 验证
        this.hiddenDangerCategoryValidator.verifyIsExistHiddenDangerType(id);

        // 删除
        DatabaseContextHolder.softDelete(this, id);
    }
}
