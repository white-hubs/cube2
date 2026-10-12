package com.guhecloud.cube.srv.gen.park.management.server.validator;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.guhecloud.cube.srv.gen.park.management.server.dao.RoomScadaResourcesMapper;
import com.guhecloud.cube.srv.gen.park.management.server.po.RoomScadaResourcesPO;
import com.guhecloud.platform.base.standard.exception.Assert;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoomScadaResourcesValidator {

    private final RoomScadaResourcesMapper mapper;

    /**
     * 验证相同名称
     */
    public void verifyIsExistByName(Long areaId) {

        RoomScadaResourcesPO po = mapper.selectOne(Wrappers.<RoomScadaResourcesPO>lambdaQuery()
                                                           .eq(RoomScadaResourcesPO::getAreaId, areaId)
                                                  );
        Assert.isConflict(Objects.nonNull(po), "区域已存在");
    }

    /**
     * 验证更新操作相同名称
     *
     * @param id 主键
     */
    public void verifyIsExistUpdateByName(Long id, Long areaId) {
        RoomScadaResourcesPO po = mapper.selectOne(Wrappers.<RoomScadaResourcesPO>lambdaQuery()
                                                           .eq(RoomScadaResourcesPO::getAreaId, areaId)
                                                  );
        Assert.isConflict(Objects.nonNull(po) && !Objects.equals(id, po.getId()), "区域已存在!");
    }

}
