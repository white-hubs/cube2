package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.dao.HiddenDangerTypeMapper;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerType;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author lzy
 * @date 2022/4/26
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HiddenDangerTypeValidator {

    private final HiddenDangerTypeMapper hiddenDangerTypeMapper;

    /**
     * 验证是否有相同隐患类别名称
     *
     * @param name 隐患类别名称
     */
    public void verifyIsExistHiddenDangerTypeByName(String name) {
        HiddenDangerType hiddenDangerType = this.hiddenDangerTypeMapper.
                selectOne(Wrappers.<HiddenDangerType>lambdaQuery().eq(HiddenDangerType::getName, name));
        Assert.isConflict(Objects.nonNull(hiddenDangerType), "存在相同名称隐患类别!");
    }

    /**
     * 验证更新操作是否有相同隐患类别名称
     *
     * @param id   隐患类别主键
     * @param name 隐患类别名称
     */
    public void verifyIsExistUpdateHiddenDangerTypeByName(Long id, String name) {
        HiddenDangerType hiddenDangerType = this.hiddenDangerTypeMapper.
                selectOne(Wrappers.<HiddenDangerType>lambdaQuery().eq(HiddenDangerType::getName, name));
        var resultIdNotEqual = Objects.nonNull(hiddenDangerType) && !Objects.equals(id, hiddenDangerType.getId());
        Assert.isConflict(resultIdNotEqual, "存在相同名称隐患类别!");
    }

}
