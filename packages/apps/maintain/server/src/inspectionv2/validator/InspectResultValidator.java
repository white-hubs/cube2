package com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.dao.InspectResultMapper;
import com.guhecloud.cube.srv.gen.maintain.server.inspectionv2.model.po.InspectResultPO;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InspectResultValidator {

    private final InspectResultMapper mapper;

    /**
     * 验证相同名称
     *
     * @param name 名称
     */
    public void verifyIsExistByName(String name) {

        Long count = mapper.selectCount(Wrappers.<InspectResultPO>lambdaQuery()
                                                .eq(InspectResultPO::getName, name)
                                       );
        Assert.isConflict(count > 1, "存在相同名称!");
    }

    /**
     * 验证更新操作相同名称
     *
     * @param id   主键
     * @param name 名称
     */
    public void verifyIsExistUpdateByName(Long id, String name) {
        List<InspectResultPO> pos = mapper.selectList(Wrappers.<InspectResultPO>lambdaQuery()
                                                              .eq(InspectResultPO::getName, name)
                                                     );

        if (pos != null && !pos.isEmpty()) {
            InspectResultPO resultPO = pos.getFirst();
            var resultIdNotEqual = Objects.nonNull(resultPO) && !Objects.equals(resultPO.getId(), id);
            Assert.isConflict(resultIdNotEqual, "存在相同名称!");
        }

    }

}
