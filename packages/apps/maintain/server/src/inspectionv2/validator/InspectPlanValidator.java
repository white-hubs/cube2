package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectPlanMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectPlanPO;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectPlanValidator {

    private final InspectPlanMapper mapper;

    /**
     * 验证相同名称
     *
     * @param name 名称
     */
    public void verifyIsExistByName(String name) {

        InspectPlanPO typePO = mapper.selectOne(Wrappers.<InspectPlanPO>lambdaQuery()
                                                        .eq(InspectPlanPO::getName, name)
                                               );
        Assert.isConflict(Objects.nonNull(typePO), "存在相同名称!");
    }

    /**
     * 验证更新操作相同名称
     *
     * @param id   主键
     * @param name 名称
     */
    public void verifyIsExistUpdateByName(Long id, String name) {
        InspectPlanPO typePO = mapper.selectOne(Wrappers.<InspectPlanPO>lambdaQuery()
                                                        .eq(InspectPlanPO::getName, name)
                                               );
        var resultIdNotEqual = Objects.nonNull(typePO) && !Objects.equals(id, typePO.getId());
        Assert.isConflict(resultIdNotEqual, "存在相同名称!");
    }

    /**
     * 校验 巡检周期选择 循环 必传 cronID
     */
    public void verifyCron(Boolean cycle, Long cronId) {
        if (cycle) {
            Assert.isParameterError(cronId == null, "周期ID不能为空");
        }
    }
}
