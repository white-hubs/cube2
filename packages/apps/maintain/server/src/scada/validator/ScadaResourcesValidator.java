package com.guhecloud.cube.srv.gen.maintain.server.scada.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.maintain.server.scada.dao.ScadaResourcesMapper;
import com.guhecloud.cube.srv.gen.maintain.server.scada.po.ScadaResourcesPO;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScadaResourcesValidator {

    private final ScadaResourcesMapper mapper;

    /**
     * 验证相同名称
     *
     * @param name 名称
     */
    public void verifyIsExistByName(String name) {

        ScadaResourcesPO typePO = mapper.selectOne(Wrappers.<ScadaResourcesPO>lambdaQuery()
                                                           .eq(ScadaResourcesPO::getName, name)
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
        ScadaResourcesPO typePO = mapper.selectOne(Wrappers.<ScadaResourcesPO>lambdaQuery()
                                                           .eq(ScadaResourcesPO::getName, name)
                                                  );
        Assert.isConflict(Objects.nonNull(typePO) && !Objects.equals(id, typePO.getId()), "存在相同名称!");
    }

}
