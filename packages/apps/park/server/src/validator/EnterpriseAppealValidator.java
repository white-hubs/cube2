package com.guhecloud.cube.srv.gen.park.management.server.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.park.management.server.dao.EnterpriseAppealMapper;
import com.guhecloud.cube.srv.gen.park.management.server.po.EnterpriseAppealPO;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EnterpriseAppealValidator {

    private final EnterpriseAppealMapper mapper;

    /**
     * 验证相同名称
     *
     * @param name 名称
     */
    public void verifyIsExistByName(String name) {

        EnterpriseAppealPO po = mapper.selectOne(Wrappers.<EnterpriseAppealPO>lambdaQuery()
                                                         .eq(EnterpriseAppealPO::getName, name)
                                                );
        Assert.isConflict(Objects.nonNull(po), "存在相同名称!");
    }

    /**
     * 验证更新操作相同名称
     *
     * @param id   主键
     * @param name 名称
     */
    public void verifyIsExistUpdateByName(Long id, String name) {
        EnterpriseAppealPO po = mapper.selectOne(Wrappers.<EnterpriseAppealPO>lambdaQuery()
                                                         .eq(EnterpriseAppealPO::getName, name)
                                                );
        Assert.isConflict(Objects.nonNull(po) && !Objects.equals(id, po.getId()), "存在相同名称!");
    }

}
