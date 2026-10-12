package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectTypeMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectTypePO;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectTypeValidator {

    private final InspectTypeMapper mapper;

    /**
     * 验证相同巡检类别名称
     *
     * @param name 巡检类型名
     */
    public void verifyIsExistByName(String name) {

        InspectTypePO typePO = mapper.selectOne(Wrappers.<InspectTypePO>lambdaQuery()
                                                        .eq(InspectTypePO::getName, name)
                                               );
        Assert.isConflict(Objects.nonNull(typePO), "存在相同名称!");
    }

    /**
     * 验证更新操作相同巡检类别名称
     *
     * @param id   类别主键
     * @param name 类别名称
     */
    public void verifyIsExistUpdateByName(Long id, String name) {
        InspectTypePO typePO = mapper.selectOne(Wrappers.<InspectTypePO>lambdaQuery()
                                                        .eq(InspectTypePO::getName, name)
                                               );
        Assert.isConflict(Objects.nonNull(typePO) && !Objects.equals(id, typePO.getId()), "存在相同名称!");
    }

}
