package com.guhecloud.cube.biz.ouaa.server.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guhecloud.cube.biz.ouaa.server.dao.PasswordSettingMapper;
import com.guhecloud.cube.biz.ouaa.server.entity.PasswordSetting;
import com.guhecloud.cube.biz.ouaa.server.service.PasswordService;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

/**
 * @author pengjian
 * @date 2022/10/12 15:10
 * @description
 */
@Service
public class PasswordServiceImpl extends ServiceImpl<PasswordSettingMapper, PasswordSetting> implements PasswordService {

    @Override
    public void setting(Integer usefulLife, Integer dueToPromptDay, Boolean forced) {
        PasswordSetting passwordSetting = this.getOne(Wrappers.lambdaQuery());
        if (Objects.isNull(passwordSetting)) {
            passwordSetting = new PasswordSetting(usefulLife, dueToPromptDay, forced);
        } else {
            passwordSetting.setUsefulLife(usefulLife);
            passwordSetting.setDueToPromptDay(dueToPromptDay);
            passwordSetting.setForced(forced);
        }

        super.saveOrUpdate(passwordSetting);

    }

    @Override
    public void setting(Integer usefulLife, Integer dueToPromptDay, Boolean forced, Boolean manyLoginFailuresCalibrated,
                        Integer accountLoginLockingDuration, Integer accountLoginAllowedFailNum, Integer accountLoginAllowedFailDuration
                       ) {
        PasswordSetting passwordSetting = this.getOne(Wrappers.lambdaQuery());
        if (Objects.isNull(passwordSetting)) {
            passwordSetting = new PasswordSetting(usefulLife, dueToPromptDay, forced, manyLoginFailuresCalibrated, accountLoginLockingDuration, accountLoginAllowedFailNum, accountLoginAllowedFailDuration);
        } else {
            passwordSetting.setUsefulLife(usefulLife);
            passwordSetting.setDueToPromptDay(dueToPromptDay);
            passwordSetting.setForced(forced);
        }
        super.saveOrUpdate(passwordSetting);
    }

    @Override
    public Optional<PasswordSetting> get() {
        PasswordSetting passwordSetting = this.getOne(Wrappers.lambdaQuery());
        if (Objects.isNull(passwordSetting)) {
            return Optional.empty();
        }
        return Optional.of(passwordSetting);
    }
}
