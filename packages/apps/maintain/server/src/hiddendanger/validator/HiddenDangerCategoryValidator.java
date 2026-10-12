package com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.dao.HiddenDangerCategoryMapper;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.dao.HiddenDangerTypeMapper;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerCategory;
import com.guhecloud.cube.srv.gen.maintain.server.hiddendanger.po.HiddenDangerType;
import com.guhecloud.platform.base.standard.exception.Assert;
import com.guhecloud.platform.base.util.CollectionUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * 隐患分类验证器
 *
 * @author lzy
 * @date 2022/4/26
 */
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class HiddenDangerCategoryValidator {

    private final HiddenDangerCategoryMapper hiddenDangerCategoryMapper;
    private final HiddenDangerTypeMapper hiddenDangerTypeMapper;

    /**
     * 验证隐患分类名称是否相同
     *
     * @param name 名称
     *
     * @return 隐患分类信息
     */
    public void verifyIsExistHiddenDangerCategoryByName(String name) {
        HiddenDangerCategory hiddenDangerCategory = this.hiddenDangerCategoryMapper.selectOne(Wrappers.<HiddenDangerCategory>lambdaQuery()
                                                                                                      .eq(HiddenDangerCategory::getName, name));
        Assert.isConflict(Objects.nonNull(hiddenDangerCategory), "存在相同隐患分类名称!");
    }

    /**
     * 验证更新隐患分类名称是否相同
     *
     * @param name 隐患分类名称
     *
     * @return 隐患分类信息
     */
    public void verifyIsExistUpdateHiddenDangerLevelByName(Long id, String name) {
        // 查询是否有相同名称
        HiddenDangerCategory hiddenDangerCategory = this.hiddenDangerCategoryMapper.selectOne(Wrappers.<HiddenDangerCategory>lambdaQuery()
                                                                                                      .eq(HiddenDangerCategory::getName, name));
        Assert.isConflict(hiddenDangerCategory != null && !Objects.equals(id, hiddenDangerCategory.getId()),
                          "存在相同隐患分类名称!");
    }

    /**
     * 验证该隐患分类是否存在隐患类别信息
     *
     * @param id 隐患分类主键
     */
    public void verifyIsExistHiddenDangerType(Long id) {
        List<HiddenDangerType> hiddenDangerTypes = this.hiddenDangerTypeMapper.selectList(Wrappers.<HiddenDangerType>lambdaQuery()
                                                                                                  .in(HiddenDangerType::getHiddenDangerCategoryId, id));
        Assert.isNotAcceptable(CollectionUtils.isNotEmpty(hiddenDangerTypes), "存在关联隐患类别信息，无法删除!");
    }

}
