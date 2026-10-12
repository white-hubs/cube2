package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectCronMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectCronPO;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.quartz.CronExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectCronValidator {

    private final InspectCronMapper mapper;

    /**
     * 验证相同名称
     *
     * @param name 名称
     */
    public void verifyIsExistByName(String name) {

        InspectCronPO typePO = mapper.selectOne(Wrappers.<InspectCronPO>lambdaQuery()
                                                        .eq(InspectCronPO::getName, name)
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
        InspectCronPO typePO = mapper.selectOne(Wrappers.<InspectCronPO>lambdaQuery()
                                                        .eq(InspectCronPO::getName, name)
                                               );
        var resultIdNotEqual = Objects.nonNull(typePO) && !Objects.equals(id, typePO.getId());
        Assert.isConflict(resultIdNotEqual, "存在相同名称!");
    }

    public void verifyCron(String cron) {
        Assert.isParameterError(!CronExpression.isValidExpression(cron), "cron表达式格式不正确!");
    }
}
