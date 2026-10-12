package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectStandardMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectStandardPO;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectStandardValidator {

    private final InspectStandardMapper mapper;

    /**
     * 验证相同名称
     *
     * @param name 名称
     */
    public void verifyIsExistByName(String name) {
        InspectStandardPO typePO = mapper.selectOne(Wrappers.<InspectStandardPO>lambdaQuery()
                                                            .eq(InspectStandardPO::getName, name)
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
        InspectStandardPO typePO = mapper.selectOne(Wrappers.<InspectStandardPO>lambdaQuery()
                                                            .eq(InspectStandardPO::getName, name)
                                                   );
        Assert.isConflict(Objects.nonNull(typePO) && !Objects.equals(id, typePO.getId()), "存在相同名称!");
    }

}
