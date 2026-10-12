package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.dao.HiddenDangerLevelMapper;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerLevel;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 隐患程度验证器
 *
 * @author lzy
 * @date 2022/4/24
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HiddenDangerLevelValidator {

    private final HiddenDangerLevelMapper hiddenDangerLevelMapper;

    /**
     * 验证隐患程度名称是否相同
     *
     * @param name 隐患程度名称
     *
     * @return 隐患程度信息
     */
    public void verifyIsExistHiddenDangerLevelByName(String name) {
        HiddenDangerLevel hiddenDangerLevel = this.hiddenDangerLevelMapper.selectOne(
                Wrappers.<HiddenDangerLevel>lambdaQuery().eq(HiddenDangerLevel::getName, name)
                                                                                    );
        Assert.isConflict(Objects.nonNull(hiddenDangerLevel), "存在相同隐患程度名称!");
    }

    /**
     * 验证更新隐患程度名称是否相同
     *
     * @param name 隐患程度名称
     *
     * @return 隐患程度信息
     */
    public void verifyIsExistUpdateHiddenDangerLevelByName(Long id, String name) {
        // 查询是否有相同名称
        HiddenDangerLevel hiddenDangerLevel = this.hiddenDangerLevelMapper.selectOne(
                Wrappers.<HiddenDangerLevel>lambdaQuery().eq(HiddenDangerLevel::getName, name)
                                                                                    );
        var resultIdNotEqual = Objects.nonNull(hiddenDangerLevel) && !Objects.equals(id, hiddenDangerLevel.getId());
        Assert.isConflict(resultIdNotEqual, "存在相同隐患程度名称!");
    }

}
